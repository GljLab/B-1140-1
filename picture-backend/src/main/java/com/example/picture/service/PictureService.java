package com.example.picture.service;

import com.example.picture.dto.*;
import com.example.picture.entity.Album;
import com.example.picture.entity.Picture;
import com.example.picture.entity.Tag;
import com.example.picture.repository.AlbumRepository;
import com.example.picture.repository.PictureRepository;
import com.example.picture.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private TagService tagService;

    @Value("${upload.path:/app/images/}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Transactional
    public PictureDTO upload(MultipartFile file, List<Long> albumIds, List<String> tagNames) throws IOException {
        String originalName = file.getOriginalFilename();
        String suffix = "";
        if (originalName != null && originalName.contains(".")) {
            suffix = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + suffix;
        Path path = Paths.get(uploadPath + fileName);
        Files.write(path, file.getBytes());

        Picture picture = new Picture();
        picture.setName(originalName);
        picture.setUrl("/images/" + fileName);
        picture.setSize(file.getSize());

        Set<Album> albums = new HashSet<>();
        if (albumIds != null && !albumIds.isEmpty()) {
            for (Long aid : albumIds) {
                albumRepository.findById(aid).ifPresent(albums::add);
            }
        }
        if (albums.isEmpty()) {
            albums.add(albumService.getDefaultAlbum());
        }
        picture.setAlbums(albums);

        Set<Tag> tags = new HashSet<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            for (String tagName : tagNames) {
                if (tagName == null) continue;
                String[] parts = tagName.split("[,，]");
                for (String part : parts) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        Tag tag = tagService.getOrCreateTag(trimmed);
                        if (tag != null) {
                            tags.add(tag);
                        }
                    }
                }
            }
        }
        for (Tag tag : tags) {
            tagService.incrementReferenceCount(tag);
        }
        picture.setTags(tags);

        Picture saved = pictureRepository.save(picture);
        return toDTO(saved);
    }

    public List<PictureDTO> listPictures(Long albumId, Long tagId, String keyword) {
        List<Picture> pictures;
        if (albumId != null) {
            pictures = pictureRepository.findByAlbumId(albumId);
        } else if (tagId != null) {
            pictures = pictureRepository.findByTagId(tagId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            pictures = pictureRepository.findByNameContainingKeyword(keyword.trim());
        } else {
            pictures = pictureRepository.findAllByOrderByCreateTimeDesc();
        }
        return pictures.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PictureDTO getPicture(Long id) {
        Picture picture = pictureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图片不存在"));
        return toDTO(picture);
    }

    @Transactional
    public PictureDTO updatePicture(Long id, PictureUpdateRequest request) {
        Picture picture = pictureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图片不存在"));

        if (request.getAlbumIds() != null) {
            Set<Album> newAlbums = new HashSet<>();
            for (Long aid : request.getAlbumIds()) {
                albumRepository.findById(aid).ifPresent(newAlbums::add);
            }
            if (newAlbums.isEmpty()) {
                newAlbums.add(albumService.getDefaultAlbum());
            }
            picture.setAlbums(newAlbums);
        }

        if (request.getTagNames() != null) {
            Set<Tag> oldTags = new HashSet<>(picture.getTags());
            Set<String> newTagNames = new HashSet<>();
            for (String tn : request.getTagNames()) {
                if (tn == null) continue;
                String[] parts = tn.split("[,，]");
                for (String part : parts) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) newTagNames.add(trimmed);
                }
            }
            Set<Tag> newTags = new HashSet<>();
            for (String name : newTagNames) {
                Tag tag = tagService.getOrCreateTag(name);
                if (tag != null) {
                    newTags.add(tag);
                    if (!oldTags.contains(tag)) {
                        tagService.incrementReferenceCount(tag);
                    }
                }
            }
            for (Tag oldTag : oldTags) {
                if (!newTags.contains(oldTag)) {
                    tagService.decrementReferenceCount(oldTag);
                }
            }
            picture.setTags(newTags);
        }

        Picture saved = pictureRepository.save(picture);
        return toDTO(saved);
    }

    @Transactional
    public void deletePicture(Long id) {
        pictureRepository.findById(id).ifPresent(picture -> {
            for (Tag tag : picture.getTags()) {
                tagService.decrementReferenceCount(tag);
            }
            String fileName = picture.getUrl().replace("/images/", "");
            File file = new File(uploadPath + fileName);
            if (file.exists()) {
                file.delete();
            }
            pictureRepository.delete(picture);
        });
    }

    @Transactional
    public void batchAddTags(BatchTagRequest request) {
        if (request.getPictureIds() == null || request.getTagNames() == null) return;
        List<Tag> tagsToAdd = new ArrayList<>();
        Set<String> processed = new HashSet<>();
        for (String tn : request.getTagNames()) {
            if (tn == null) continue;
            String[] parts = tn.split("[,，]");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty() && !processed.contains(trimmed)) {
                    processed.add(trimmed);
                    Tag tag = tagService.getOrCreateTag(trimmed);
                    if (tag != null) tagsToAdd.add(tag);
                }
            }
        }
        for (Long pid : request.getPictureIds()) {
            pictureRepository.findById(pid).ifPresent(picture -> {
                for (Tag tag : tagsToAdd) {
                    if (!picture.getTags().contains(tag)) {
                        picture.getTags().add(tag);
                        tagService.incrementReferenceCount(tag);
                    }
                }
                pictureRepository.save(picture);
            });
        }
    }

    @Transactional
    public void batchAddToAlbum(BatchAlbumRequest request) {
        if (request.getPictureIds() == null || request.getAlbumId() == null) return;
        Album album = albumRepository.findById(request.getAlbumId()).orElse(null);
        if (album == null) return;
        for (Long pid : request.getPictureIds()) {
            pictureRepository.findById(pid).ifPresent(picture -> {
                if (!picture.getAlbums().contains(album)) {
                    picture.getAlbums().add(album);
                    pictureRepository.save(picture);
                }
            });
        }
    }

    @Transactional
    public void batchDelete(BatchDeleteRequest request) {
        if (request.getPictureIds() == null) return;
        for (Long pid : request.getPictureIds()) {
            deletePicture(pid);
        }
    }

    public PictureDTO toDTO(Picture picture) {
        PictureDTO dto = new PictureDTO();
        dto.setId(picture.getId());
        dto.setName(picture.getName());
        dto.setUrl(picture.getUrl());
        dto.setSize(picture.getSize());
        dto.setCreateTime(picture.getCreateTime());
        dto.setUpdateTime(picture.getUpdateTime());
        List<AlbumSimpleDTO> albumDTOs = picture.getAlbums().stream()
                .sorted(Comparator.comparing(Album::getDisplayOrder).thenComparing(Album::getCreateTime))
                .map(this::albumToSimpleDTO)
                .collect(Collectors.toList());
        dto.setAlbums(albumDTOs);
        List<TagDTO> tagDTOs = picture.getTags().stream()
                .sorted(Comparator.comparing(Tag::getReferenceCount).reversed().thenComparing(Tag::getCreateTime))
                .map(tagService::toDTO)
                .collect(Collectors.toList());
        dto.setTags(tagDTOs);
        return dto;
    }

    private AlbumSimpleDTO albumToSimpleDTO(Album album) {
        AlbumSimpleDTO dto = new AlbumSimpleDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setCoverUrl(album.getCoverUrl());
        dto.setIsDefault(album.getIsDefault());
        dto.setDisplayOrder(album.getDisplayOrder());
        dto.setCreateTime(album.getCreateTime());
        return dto;
    }
}
