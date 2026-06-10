package com.example.picture.service;

import com.example.picture.dto.AlbumCreateRequest;
import com.example.picture.dto.AlbumDTO;
import com.example.picture.dto.AlbumUpdateRequest;
import com.example.picture.entity.Album;
import com.example.picture.entity.Picture;
import com.example.picture.repository.AlbumRepository;
import com.example.picture.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @PostConstruct
    @Transactional
    public void initDefaultAlbum() {
        Optional<Album> existing = albumRepository.findByIsDefaultTrue();
        if (!existing.isPresent()) {
            Album defaultAlbum = new Album();
            defaultAlbum.setName("默认专辑");
            defaultAlbum.setDescription("系统默认专辑，存放未分类的图片");
            defaultAlbum.setIsDefault(true);
            defaultAlbum.setIsPublic(true);
            defaultAlbum.setDisplayOrder(0);
            albumRepository.save(defaultAlbum);
        }
    }

    public Album getDefaultAlbum() {
        return albumRepository.findByIsDefaultTrue().orElseThrow(() ->
                new RuntimeException("默认专辑不存在"));
    }

    public AlbumDTO createAlbum(AlbumCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("专辑名称不能为空");
        }
        if (request.getName().length() > 100) {
            throw new RuntimeException("专辑名称不能超过100字符");
        }
        Album album = new Album();
        album.setName(request.getName().trim());
        album.setDescription(request.getDescription() != null && request.getDescription().length() <= 500 ?
                request.getDescription() : null);
        album.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);
        album.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        album.setIsDefault(false);
        Album saved = albumRepository.save(album);
        return toDTO(saved);
    }

    public AlbumDTO updateAlbum(Long id, AlbumUpdateRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("专辑不存在"));
        if (album.getIsDefault() && request.getName() != null) {
            throw new RuntimeException("默认专辑名称不可修改");
        }
        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isEmpty()) throw new RuntimeException("专辑名称不能为空");
            if (name.length() > 100) throw new RuntimeException("专辑名称不能超过100字符");
            album.setName(name);
        }
        if (request.getDescription() != null) {
            if (request.getDescription().length() > 500) throw new RuntimeException("描述不能超过500字符");
            album.setDescription(request.getDescription());
        }
        if (request.getCoverUrl() != null) {
            album.setCoverUrl(request.getCoverUrl());
        }
        if (request.getCoverPictureId() != null) {
            album.setCoverPictureId(request.getCoverPictureId());
        }
        if (request.getIsPublic() != null) {
            album.setIsPublic(request.getIsPublic());
        }
        if (request.getDisplayOrder() != null) {
            album.setDisplayOrder(request.getDisplayOrder());
        }
        Album saved = albumRepository.save(album);
        return toDTO(saved);
    }

    @Transactional
    public void deleteAlbum(Long id, boolean deletePictures) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("专辑不存在"));
        if (album.getIsDefault()) {
            throw new RuntimeException("默认专辑不可删除");
        }
        List<Picture> pictures = pictureRepository.findByAlbumId(id);
        if (deletePictures) {
            for (Picture pic : pictures) {
                String fileName = pic.getUrl().replace("/images/", "");
                java.io.File file = new java.io.File("/app/images/" + fileName);
                if (file.exists()) {
                    file.delete();
                }
                pic.getTags().clear();
                pictureRepository.delete(pic);
            }
        } else {
            Album defaultAlbum = getDefaultAlbum();
            for (Picture pic : pictures) {
                pic.getAlbums().remove(album);
                pic.getAlbums().add(defaultAlbum);
                pictureRepository.save(pic);
            }
        }
        albumRepository.delete(album);
    }

    public List<AlbumDTO> listAlbums() {
        List<Album> albums = albumRepository.findAllByOrderByDisplayOrderAscCreateTimeAsc();
        return albums.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AlbumDTO getAlbum(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("专辑不存在"));
        return toDTO(album);
    }

    public AlbumDTO toDTO(Album album) {
        AlbumDTO dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setDescription(album.getDescription());
        dto.setCoverUrl(album.getCoverUrl());
        dto.setCoverPictureId(album.getCoverPictureId());
        dto.setIsPublic(album.getIsPublic());
        dto.setIsDefault(album.getIsDefault());
        dto.setDisplayOrder(album.getDisplayOrder());
        dto.setCreateTime(album.getCreateTime());
        dto.setUpdateTime(album.getUpdateTime());
        Long count = pictureRepository.countByAlbumId(album.getId());
        dto.setPictureCount(count != null ? count.intValue() : 0);
        Long size = pictureRepository.sumSizesByAlbumId(album.getId());
        dto.setTotalSize(size != null ? size : 0L);
        dto.setLastUploadTime(pictureRepository.findLastUploadTimeByAlbumId(album.getId()));
        return dto;
    }

    @Transactional
    public void addPictureToAlbum(Long albumId, Long pictureId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("专辑不存在"));
        Picture picture = pictureRepository.findById(pictureId)
                .orElseThrow(() -> new RuntimeException("图片不存在"));
        picture.getAlbums().add(album);
        pictureRepository.save(picture);
    }

    @Transactional
    public void removePictureFromAlbum(Long albumId, Long pictureId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("专辑不存在"));
        Picture picture = pictureRepository.findById(pictureId)
                .orElseThrow(() -> new RuntimeException("图片不存在"));
        picture.getAlbums().remove(album);
        if (picture.getAlbums().isEmpty()) {
            Album defaultAlbum = getDefaultAlbum();
            picture.getAlbums().add(defaultAlbum);
        }
        pictureRepository.save(picture);
    }

    @Transactional
    public void reorderAlbums(Map<Long, Integer> orderMap) {
        for (Map.Entry<Long, Integer> entry : orderMap.entrySet()) {
            albumRepository.findById(entry.getKey()).ifPresent(album -> {
                album.setDisplayOrder(entry.getValue());
                albumRepository.save(album);
            });
        }
    }

    public List<AlbumDTO> searchAlbums(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return listAlbums();
        }
        List<Album> albums = albumRepository.findByNameContainingIgnoreCase(keyword.trim());
        return albums.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
