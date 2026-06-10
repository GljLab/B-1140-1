package com.example.picture.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchTagRequest {
    private List<Long> pictureIds;
    private List<String> tagNames;
}
