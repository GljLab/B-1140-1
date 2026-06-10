package com.example.picture.controller;

import com.example.picture.dto.ApiResponse;
import com.example.picture.dto.GlobalStatsDTO;
import com.example.picture.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public ResponseEntity<ApiResponse<GlobalStatsDTO>> getGlobalStats() {
        return ResponseEntity.ok(ApiResponse.success(statsService.getGlobalStats()));
    }
}
