package com.example.picture.dto;

import lombok.Data;

@Data
public class AlbumCreateRequest {
    private String name;
    private String description;
    private Boolean isPublic = true;
    private Integer displayOrder = 0;
}
