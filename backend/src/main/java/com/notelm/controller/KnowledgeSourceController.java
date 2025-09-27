package com.notelm.controller;

import com.notelm.dto.request.AddTextSourceRequest;
import com.notelm.dto.request.AddWebpageSourceRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.KnowledgeSourceResponse;
import com.notelm.service.KnowledgeSourceService;
import com.notelm.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/learning-spaces/{spaceId}/sources")
public class KnowledgeSourceController {

    @Autowired
    private KnowledgeSourceService knowledgeSourceService;

    @PostMapping("/webpage")
    public ApiResponse<KnowledgeSourceResponse> addWebpageSource(
            @PathVariable Long spaceId,
            @Valid @RequestBody AddWebpageSourceRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        KnowledgeSourceResponse response = knowledgeSourceService.addWebpageSource(spaceId, request);
        return ApiResponse.success(response);
    }

    @PostMapping("/text")
    public ApiResponse<KnowledgeSourceResponse> addTextSource(
            @PathVariable Long spaceId,
            @Valid @RequestBody AddTextSourceRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        KnowledgeSourceResponse response = knowledgeSourceService.addTextSource(spaceId, request);
        return ApiResponse.success(response);
    }

    @PostMapping("/pdf")
    public ApiResponse<KnowledgeSourceResponse> uploadPdf(
            @PathVariable Long spaceId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        KnowledgeSourceResponse response = knowledgeSourceService.uploadPdfFile(spaceId, file, title);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<KnowledgeSourceResponse>> getKnowledgeSources(
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<KnowledgeSourceResponse> sources;
        if (search != null && !search.isEmpty()) {
            sources = knowledgeSourceService.searchKnowledgeSources(spaceId, search, pageable);
        } else {
            sources = knowledgeSourceService.getKnowledgeSources(spaceId, pageable);
        }

        return ApiResponse.success(sources);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteKnowledgeSource(@PathVariable Long spaceId, @PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        knowledgeSourceService.deleteKnowledgeSource(id);
        return ApiResponse.success("知识来源删除成功");
    }
}