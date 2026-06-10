package com.example.picture.dto;

import lombok.Data;

@Data
public class AlbumUpdateRequest {
    private String name;
    private String description;
    private String coverUrl;
    private Long coverPictureId;
    private Boolean isPublic;
    private Integer displayOrder;
}
