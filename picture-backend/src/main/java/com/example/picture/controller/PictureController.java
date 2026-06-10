package com.example.picture.controller;

import com.example.picture.dto.*;
import com.example.picture.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<PictureDTO>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "albumIds", required = false) Long[] albumIds,
            @RequestParam(value = "tags", required = false) String[] tags) throws IOException {
        try {
            List<Long> albumIdList = albumIds != null ? Arrays.asList(albumIds) : null;
            List<String> tagList = tags != null ? Arrays.asList(tags) : null;
            return ResponseEntity.ok(ApiResponse.success(pictureService.upload(file, albumIdList, tagList)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PictureDTO>>> list(
            @RequestParam(value = "albumId", required = false) Long albumId,
            @RequestParam(value = "tagId", required = false) Long tagId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(ApiResponse.success(pictureService.listPictures(albumId, tagId, keyword)));
    }

    @GetMapping("/pictures/{id}")
    public ResponseEntity<ApiResponse<PictureDTO>> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(pictureService.getPicture(id)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/pictures/{id}")
    public ResponseEntity<ApiResponse<PictureDTO>> update(@PathVariable Long id, @RequestBody PictureUpdateRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.success(pictureService.updatePicture(id, request)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            pictureService.deletePicture(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/pictures/batch-tags")
    public ResponseEntity<ApiResponse<Void>> batchAddTags(@RequestBody BatchTagRequest request) {
        try {
            pictureService.batchAddTags(request);
            return ResponseEntity.ok(ApiResponse.success("批量添加成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/pictures/batch-album")
    public ResponseEntity<ApiResponse<Void>> batchAddAlbum(@RequestBody BatchAlbumRequest request) {
        try {
            pictureService.batchAddToAlbum(request);
            return ResponseEntity.ok(ApiResponse.success("批量添加成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/pictures/batch-delete")
    public ResponseEntity<ApiResponse<Void>> batchDelete(@RequestBody BatchDeleteRequest request) {
        try {
            pictureService.batchDelete(request);
            return ResponseEntity.ok(ApiResponse.success("批量删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }
}
