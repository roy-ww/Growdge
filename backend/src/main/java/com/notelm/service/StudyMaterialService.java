package com.notelm.service;

import com.notelm.dto.request.CreateStudyMaterialRequest;
import com.notelm.dto.response.StudyMaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyMaterialService {
    
    StudyMaterialResponse createStudyMaterial(Long learningSpaceId, CreateStudyMaterialRequest request);
    
    StudyMaterialResponse getStudyMaterialById(Long id);
    
    Page<StudyMaterialResponse> getStudyMaterials(Long learningSpaceId, Pageable pageable);
    
    Page<StudyMaterialResponse> getStudyMaterialsByType(Long learningSpaceId, String materialType, Pageable pageable);
    
    Page<StudyMaterialResponse> searchStudyMaterials(Long learningSpaceId, String search, Pageable pageable);
    
    void deleteStudyMaterial(Long id);
}