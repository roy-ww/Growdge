package com.notelm.service;

import com.notelm.dto.request.AddTextSourceRequest;
import com.notelm.dto.request.AddWebpageSourceRequest;
import com.notelm.dto.response.KnowledgeSourceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface KnowledgeSourceService {
    
    KnowledgeSourceResponse addWebpageSource(Long learningSpaceId, AddWebpageSourceRequest request);
    
    KnowledgeSourceResponse addTextSource(Long learningSpaceId, AddTextSourceRequest request);
    
    KnowledgeSourceResponse uploadPdfFile(Long learningSpaceId, MultipartFile file, String title);
    
    Page<KnowledgeSourceResponse> getKnowledgeSources(Long learningSpaceId, Pageable pageable);
    
    KnowledgeSourceResponse getKnowledgeSourceById(Long id);
    
    void deleteKnowledgeSource(Long id);
    
    Page<KnowledgeSourceResponse> searchKnowledgeSources(Long learningSpaceId, String search, Pageable pageable);
}