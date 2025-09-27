package com.notelm.service;

import com.notelm.dto.request.CreateLearningSpaceRequest;
import com.notelm.dto.request.UpdateLearningSpaceRequest;
import com.notelm.dto.response.LearningSpaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LearningSpaceService {
    
    LearningSpaceResponse createLearningSpace(Long userId, CreateLearningSpaceRequest request);
    
    LearningSpaceResponse getLearningSpaceById(Long id, Long userId);
    
    Page<LearningSpaceResponse> getUserLearningSpaces(Long userId, Pageable pageable);
    
    Page<LearningSpaceResponse> searchUserLearningSpaces(Long userId, String search, Pageable pageable);
    
    LearningSpaceResponse updateLearningSpace(Long id, Long userId, UpdateLearningSpaceRequest request);
    
    void deleteLearningSpace(Long id, Long userId);
    
    LearningSpaceResponse getLearningSpaceWithStats(Long id, Long userId);
}