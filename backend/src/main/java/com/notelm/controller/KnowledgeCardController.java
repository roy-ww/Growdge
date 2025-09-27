package com.notelm.controller;

import com.notelm.dto.request.GenerateKnowledgeCardRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.KnowledgeCardResponse;
import com.notelm.service.KnowledgeCardService;
import com.notelm.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learning-spaces/{spaceId}/knowledge-cards")
public class KnowledgeCardController {

    @Autowired
    private KnowledgeCardService knowledgeCardService;

    @PostMapping("/generate")
    public ApiResponse<Void> generateKnowledgeCards(
            @PathVariable Long spaceId,
            @Valid @RequestBody GenerateKnowledgeCardRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        knowledgeCardService.generateKnowledgeCards(spaceId, request);
        return ApiResponse.success("知识卡片生成成功");
    }

    @GetMapping
    public ApiResponse<Page<KnowledgeCardResponse>> getKnowledgeCards(
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String reviewed,
            @RequestParam(required = false) String status) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<KnowledgeCardResponse> cards;
        if (status != null && !status.isEmpty()) {
            cards = knowledgeCardService.getKnowledgeCardsByStatus(spaceId, status, pageable);
        } else {
            cards = knowledgeCardService.getKnowledgeCards(spaceId, pageable);
        }

        return ApiResponse.success(cards);
    }

    @PostMapping("/{id}/mark-reviewed")
    public ApiResponse<Void> markCardAsReviewed(@PathVariable Long spaceId, @PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        knowledgeCardService.markCardAsReviewed(id);
        return ApiResponse.success("知识卡片已标记为已复习");
    }
}