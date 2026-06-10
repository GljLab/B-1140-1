package com.example.picture.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchDeleteRequest {
    private List<Long> pictureIds;
}
