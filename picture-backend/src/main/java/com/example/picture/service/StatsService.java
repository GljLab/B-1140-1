package com.example.picture.service;

import com.example.picture.dto.GlobalStatsDTO;
import com.example.picture.dto.TagDTO;
import com.example.picture.repository.AlbumRepository;
import com.example.picture.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private TagService tagService;

    public GlobalStatsDTO getGlobalStats() {
        GlobalStatsDTO stats = new GlobalStatsDTO();
        stats.setAlbumCount(albumRepository.count());
        stats.setPictureCount(pictureRepository.count());
        Long totalSize = pictureRepository.sumAllSizes();
        stats.setTotalStorageSize(totalSize != null ? totalSize : 0L);
        List<TagDTO> topTags = tagService.getTopTags(10);
        stats.setTopTags(topTags);
        return stats;
    }
}
