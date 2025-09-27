package com.notelm.controller;

import com.notelm.dto.request.GenerateMindMapRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.MindMapResponse;
import com.notelm.service.MindMapService;
import com.notelm.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learning-spaces/{spaceId}/mind-maps")
public class MindMapController {

    @Autowired
    private MindMapService mindMapService;

    @PostMapping("/generate")
    public ApiResponse<MindMapResponse> generateMindMap(
            @PathVariable Long spaceId,
            @Valid @RequestBody GenerateMindMapRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        MindMapResponse response = mindMapService.generateMindMap(spaceId, request);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<MindMapResponse>> getMindMaps(
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MindMapResponse> mindMaps = mindMapService.getMindMaps(spaceId, pageable);
        return ApiResponse.success(mindMaps);
    }

    @GetMapping("/{id}")
    public ApiResponse<MindMapResponse> getMindMap(@PathVariable Long spaceId, @PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        MindMapResponse response = mindMapService.getMindMapById(id);
        return ApiResponse.success(response);
    }
}