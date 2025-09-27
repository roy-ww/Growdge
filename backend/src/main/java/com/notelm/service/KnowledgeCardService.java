package com.notelm.service;

import com.notelm.dto.request.GenerateKnowledgeCardRequest;
import com.notelm.dto.response.KnowledgeCardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KnowledgeCardService {
    
    Page<KnowledgeCardResponse> getKnowledgeCards(Long learningSpaceId, Pageable pageable);
    
    KnowledgeCardResponse getKnowledgeCardById(Long id);
    
    Page<KnowledgeCardResponse> getKnowledgeCardsByStatus(Long learningSpaceId, String status, Pageable pageable);
    
    void markCardAsReviewed(Long id);
    
    KnowledgeCardResponse generateKnowledgeCard(Long id);
    
    void generateKnowledgeCards(Long learningSpaceId, GenerateKnowledgeCardRequest request);
}