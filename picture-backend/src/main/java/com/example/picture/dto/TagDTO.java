package com.example.picture.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TagDTO {
    private Long id;
    private String name;
    private Integer referenceCount;
    private Date createTime;
}
