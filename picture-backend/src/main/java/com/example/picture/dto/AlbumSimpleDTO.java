package com.example.picture.dto;

import lombok.Data;
import java.util.Date;

@Data
public class AlbumSimpleDTO {
    private Long id;
    private String name;
    private String coverUrl;
    private Boolean isDefault;
    private Integer displayOrder;
    private Date createTime;
}
