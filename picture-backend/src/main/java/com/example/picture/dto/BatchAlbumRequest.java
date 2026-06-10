package com.example.picture.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchAlbumRequest {
    private List<Long> pictureIds;
    private Long albumId;
}
