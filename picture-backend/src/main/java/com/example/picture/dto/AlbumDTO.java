package com.example.picture.dto;

import lombok.Data;
import java.util.Date;

@Data
public class AlbumDTO {
    private Long id;
    private String name;
    private String description;
    private String coverUrl;
    private Long coverPictureId;
    private Boolean isPublic;
    private Boolean isDefault;
    private Integer displayOrder;
    private Date createTime;
    private Date updateTime;
    private Integer pictureCount;
    private Long totalSize;
    private Date lastUploadTime;
}
