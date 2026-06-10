package com.example.picture.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PictureDTO {
    private Long id;
    private String name;
    private String url;
    private Long size;
    private Date createTime;
    private Date updateTime;
    private List<AlbumSimpleDTO> albums;
    private List<TagDTO> tags;
}
