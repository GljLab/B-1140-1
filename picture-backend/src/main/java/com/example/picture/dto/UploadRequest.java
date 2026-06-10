package com.example.picture.dto;

import lombok.Data;
import java.util.List;

@Data
public class UploadRequest {
    private List<Long> albumIds;
    private List<String> tagNames;
}
