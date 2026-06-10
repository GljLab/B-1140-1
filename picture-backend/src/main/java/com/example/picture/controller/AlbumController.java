package com.example.picture.controller;

import com.example.picture.dto.*;
import com.example.picture.service.AlbumService;
import com.example.picture.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/albums")
@CrossOrigin(origins = "*")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PictureService pictureService;

    @PostMapping
    public ResponseEntity<ApiResponse<AlbumDTO>> create(@RequestBody AlbumCreateRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.success(albumService.createAlbum(request)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumDTO>> update(@PathVariable Long id, @RequestBody AlbumUpdateRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.success(albumService.updateAlbum(id, request)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id,
                                                    @RequestParam(defaultValue = "false") boolean deletePictures) {
        try {
            albumService.deleteAlbum(id, deletePictures);
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AlbumDTO>>> list() {
        return ResponseEntity.ok(ApiResponse.success(albumService.listAlbums()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumDTO>> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(albumService.getAlbum(id)));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/pictures")
    public ResponseEntity<ApiResponse<List<PictureDTO>>> getPictures(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(pictureService.listPictures(id, null, null)));
    }

    @PostMapping("/reorder")
    public ResponseEntity<ApiResponse<Void>> reorder(@RequestBody Map<Long, Integer> orderMap) {
        albumService.reorderAlbums(orderMap);
        return ResponseEntity.ok(ApiResponse.success("排序成功", null));
    }

    @PostMapping("/{albumId}/pictures/{pictureId}")
    public ResponseEntity<ApiResponse<Void>> addPicture(@PathVariable Long albumId, @PathVariable Long pictureId) {
        try {
            albumService.addPictureToAlbum(albumId, pictureId);
            return ResponseEntity.ok(ApiResponse.success("添加成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{albumId}/pictures/{pictureId}")
    public ResponseEntity<ApiResponse<Void>> removePicture(@PathVariable Long albumId, @PathVariable Long pictureId) {
        try {
            albumService.removePictureFromAlbum(albumId, pictureId);
            return ResponseEntity.ok(ApiResponse.success("移除成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AlbumDTO>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(albumService.searchAlbums(keyword)));
    }
}
