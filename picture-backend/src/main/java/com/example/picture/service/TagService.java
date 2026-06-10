package com.example.picture.service;

import com.example.picture.dto.TagDTO;
import com.example.picture.entity.Tag;
import com.example.picture.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public Tag getOrCreateTag(String name) {
        if (name == null) return null;
        String trimmed = name.trim();
        if (trimmed.isEmpty()) return null;
        if (trimmed.length() > 50) {
            trimmed = trimmed.substring(0, 50);
        }
        Optional<Tag> existing = tagRepository.findByName(trimmed);
        if (existing.isPresent()) {
            return existing.get();
        }
        Tag tag = new Tag();
        tag.setName(trimmed);
        tag.setReferenceCount(0);
        return tagRepository.save(tag);
    }

    @Transactional
    public void incrementReferenceCount(Tag tag) {
        tag.setReferenceCount(tag.getReferenceCount() + 1);
        tagRepository.save(tag);
    }

    @Transactional
    public void decrementReferenceCount(Tag tag) {
        int count = tag.getReferenceCount() - 1;
        if (count < 0) count = 0;
        tag.setReferenceCount(count);
        tagRepository.save(tag);
    }

    public List<TagDTO> listTags(String sortBy) {
        List<Tag> tags;
        if ("hot".equals(sortBy)) {
            tags = tagRepository.findAllByOrderByReferenceCountDescCreateTimeDesc();
        } else {
            tags = tagRepository.findAllByOrderByReferenceCountDescCreateTimeDesc();
        }
        return tags.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TagDTO getTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("主题词不存在"));
        return toDTO(tag);
    }

    public TagDTO getTagByName(String name) {
        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("主题词不存在"));
        return toDTO(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("主题词不存在"));
        if (tag.getReferenceCount() > 0) {
            throw new RuntimeException("该主题词仍被引用，无法删除");
        }
        tagRepository.delete(tag);
    }

    public List<TagDTO> searchTags(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return listTags("hot");
        }
        List<Tag> tags = tagRepository.findByNameContainingIgnoreCaseOrderByReferenceCountDesc(keyword.trim());
        return tags.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TagDTO> getTopTags(int limit) {
        List<Tag> tags = tagRepository.findTop10ByOrderByReferenceCountDesc(PageRequest.of(0, limit));
        return tags.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TagDTO> parseAndCreateTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) return new ArrayList<>();
        List<TagDTO> result = new ArrayList<>();
        Set<String> processed = new HashSet<>();
        for (String name : tagNames) {
            if (name == null) continue;
            String[] parts = name.split("[,，]");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty() && !processed.contains(trimmed)) {
                    processed.add(trimmed);
                    Tag tag = getOrCreateTag(trimmed);
                    if (tag != null) {
                        result.add(toDTO(tag));
                    }
                }
            }
        }
        return result;
    }

    public TagDTO toDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setReferenceCount(tag.getReferenceCount());
        dto.setCreateTime(tag.getCreateTime());
        return dto;
    }
}
