package com.example.picture.dto;

import lombok.Data;
import java.util.List;

@Data
public class GlobalStatsDTO {
    private Long albumCount;
    private Long pictureCount;
    private Long totalStorageSize;
    private List<TagDTO> topTags;
}
