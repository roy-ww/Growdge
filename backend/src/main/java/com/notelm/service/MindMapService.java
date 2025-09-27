package com.notelm.service;

import com.notelm.dto.request.GenerateMindMapRequest;
import com.notelm.dto.response.MindMapResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MindMapService {
    
    MindMapResponse generateMindMap(Long learningSpaceId, GenerateMindMapRequest request);
    
    MindMapResponse getMindMapById(Long id);
    
    Page<MindMapResponse> getMindMaps(Long learningSpaceId, Pageable pageable);
}