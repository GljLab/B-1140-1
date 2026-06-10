package com.example.picture.controller;

import com.example.picture.dto.ApiResponse;
import com.example.picture.dto.PictureDTO;
import com.example.picture.dto.TagDTO;
import com.example.picture.service.PictureService;
import com.example.picture.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private PictureService pictureService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagDTO>>> list(@RequestParam(defaultValue = "hot") String sortBy) {
        return ResponseEntity.ok(ApiResponse.success(tagService.listTags(sortBy)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(tagService.getTag(id)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<TagDTO>> getByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(ApiResponse.success(tagService.getTagByName(name)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            tagService.deleteTag(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TagDTO>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(tagService.searchTags(keyword)));
    }

    @GetMapping("/{id}/pictures")
    public ResponseEntity<ApiResponse<List<PictureDTO>>> getPictures(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(pictureService.listPictures(null, id, null)));
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<TagDTO>>> topTags(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(tagService.getTopTags(limit)));
    }
}
