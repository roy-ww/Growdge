package com.notelm.controller;

import com.notelm.dto.request.CreateStudyMaterialRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.StudyMaterialResponse;
import com.notelm.service.StudyMaterialService;
import com.notelm.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/learning-spaces/{spaceId}/study-materials")
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService studyMaterialService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudyMaterialResponse>> createStudyMaterial(
            @PathVariable Long spaceId,
            @RequestBody CreateStudyMaterialRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.ok(ApiResponse.error("用户未认证"));
        }
        StudyMaterialResponse response = studyMaterialService.createStudyMaterial(spaceId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyMaterialResponse>> getStudyMaterial(
            @PathVariable Long spaceId,
            @PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.ok(ApiResponse.error("用户未认证"));
        }
        StudyMaterialResponse response = studyMaterialService.getStudyMaterialById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudyMaterialResponse>>> getStudyMaterials(
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.ok(ApiResponse.error("用户未认证"));
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<StudyMaterialResponse> responses = studyMaterialService.getStudyMaterials(spaceId, pageable);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudyMaterial(
            @PathVariable Long spaceId,
            @PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.ok(ApiResponse.error("用户未认证"));
        }
        studyMaterialService.deleteStudyMaterial(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}