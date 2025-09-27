package com.notelm.controller;

import com.notelm.dto.request.CreateLearningSpaceRequest;
import com.notelm.dto.request.UpdateLearningSpaceRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.LearningSpaceResponse;
import com.notelm.service.LearningSpaceService;
import com.notelm.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learning-spaces")
public class LearningSpaceController {

    @Autowired
    private LearningSpaceService learningSpaceService;

    @PostMapping
    public ApiResponse<LearningSpaceResponse> createLearningSpace(
            @Valid @RequestBody CreateLearningSpaceRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        LearningSpaceResponse response = learningSpaceService.createLearningSpace(currentUserId, request);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<LearningSpaceResponse>> getUserLearningSpaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LearningSpaceResponse> spaces;
        if (search != null && !search.isEmpty()) {
            spaces = learningSpaceService.searchUserLearningSpaces(currentUserId, search, pageable);
        } else {
            spaces = learningSpaceService.getUserLearningSpaces(currentUserId, pageable);
        }

        return ApiResponse.success(spaces);
    }

    @GetMapping("/{id}")
    public ApiResponse<LearningSpaceResponse> getLearningSpace(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        LearningSpaceResponse response = learningSpaceService.getLearningSpaceWithStats(id, currentUserId);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<LearningSpaceResponse> updateLearningSpace(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLearningSpaceRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        LearningSpaceResponse response = learningSpaceService.updateLearningSpace(id, currentUserId, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLearningSpace(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        learningSpaceService.deleteLearningSpace(id, currentUserId);
        return ApiResponse.success("学习空间删除成功");
    }
}