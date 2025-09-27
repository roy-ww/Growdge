package com.notelm.service.impl;

import com.notelm.dto.request.CreateLearningSpaceRequest;
import com.notelm.dto.request.UpdateLearningSpaceRequest;
import com.notelm.dto.response.LearningSpaceResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.LearningSpace;
import com.notelm.repository.KnowledgeSourceRepository;
import com.notelm.repository.LearningSpaceRepository;
import com.notelm.repository.StudyMaterialRepository;
import com.notelm.service.LearningSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LearningSpaceServiceImpl implements LearningSpaceService {

    @Autowired
    private LearningSpaceRepository learningSpaceRepository;
    
    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;
    
    @Autowired
    private StudyMaterialRepository studyMaterialRepository;

    @Override
    public LearningSpaceResponse createLearningSpace(Long userId, CreateLearningSpaceRequest request) {
        LearningSpace learningSpace = new LearningSpace();
        learningSpace.setUserId(userId);
        learningSpace.setName(request.getName());
        learningSpace.setDescription(request.getDescription());
        
        if (request.getStatus() != null) {
            learningSpace.setStatus(LearningSpace.Status.valueOf(request.getStatus().toUpperCase()));
        }
        
        LearningSpace saved = learningSpaceRepository.save(learningSpace);
        return convertToResponse(saved);
    }

    @Override
    public LearningSpaceResponse getLearningSpaceById(Long id, Long userId) {
        LearningSpace learningSpace = learningSpaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));
        
        if (!learningSpace.getUserId().equals(userId)) {
            throw new BusinessException("无权限访问此学习空间");
        }
        
        return convertToResponse(learningSpace);
    }

    @Override
    public Page<LearningSpaceResponse> getUserLearningSpaces(Long userId, Pageable pageable) {
        return learningSpaceRepository.findByUserId(userId, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public Page<LearningSpaceResponse> searchUserLearningSpaces(Long userId, String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getUserLearningSpaces(userId, pageable);
        }
        return learningSpaceRepository.findByUserIdAndNameContainingIgnoreCase(userId, search, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public LearningSpaceResponse updateLearningSpace(Long id, Long userId, UpdateLearningSpaceRequest request) {
        LearningSpace learningSpace = learningSpaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));
        
        if (!learningSpace.getUserId().equals(userId)) {
            throw new BusinessException("无权限更新此学习空间");
        }
        
        if (request.getName() != null) {
            learningSpace.setName(request.getName());
        }
        if (request.getDescription() != null) {
            learningSpace.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            learningSpace.setStatus(LearningSpace.Status.valueOf(request.getStatus().toUpperCase()));
        }
        
        LearningSpace updated = learningSpaceRepository.save(learningSpace);
        return convertToResponse(updated);
    }

    @Override
    public void deleteLearningSpace(Long id, Long userId) {
        LearningSpace learningSpace = learningSpaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));
        
        if (!learningSpace.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除此学习空间");
        }
        
        learningSpaceRepository.deleteById(id);
    }

    @Override
    public LearningSpaceResponse getLearningSpaceWithStats(Long id, Long userId) {
        LearningSpaceResponse response = getLearningSpaceById(id, userId);
        
        // 获取相关的统计信息
        Integer sourceCount = knowledgeSourceRepository.countByLearningSpaceId(id).intValue();
        Integer materialCount = studyMaterialRepository.countByLearningSpaceId(id).intValue();
        
        response.setSourceCount(sourceCount);
        response.setMaterialCount(materialCount);
        
        return response;
    }

    private LearningSpaceResponse convertToResponse(LearningSpace learningSpace) {
        LearningSpaceResponse response = new LearningSpaceResponse();
        response.setId(learningSpace.getId());
        response.setName(learningSpace.getName());
        response.setDescription(learningSpace.getDescription());
        response.setUserId(learningSpace.getUserId());
        response.setStatus(learningSpace.getStatus().name());
        response.setCreatedAt(learningSpace.getCreatedAt());
        response.setUpdatedAt(learningSpace.getUpdatedAt());
        
        return response;
    }
}