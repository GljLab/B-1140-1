package com.example.picture.dto;

import lombok.Data;
import java.util.List;

@Data
public class PictureUpdateRequest {
    private List<Long> albumIds;
    private List<String> tagNames;
}
