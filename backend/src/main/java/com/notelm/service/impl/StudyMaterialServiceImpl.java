package com.notelm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notelm.dto.request.CreateStudyMaterialRequest;
import com.notelm.dto.response.StudyMaterialResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.LearningSpace;
import com.notelm.model.StudyMaterial;
import com.notelm.repository.LearningSpaceRepository;
import com.notelm.repository.StudyMaterialRepository;
import com.notelm.service.StudyMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class StudyMaterialServiceImpl implements StudyMaterialService {

    @Autowired
    private StudyMaterialRepository studyMaterialRepository;

    @Autowired
    private LearningSpaceRepository learningSpaceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public StudyMaterialResponse createStudyMaterial(Long learningSpaceId, CreateStudyMaterialRequest request) {
        // 验证学习空间是否存在
        LearningSpace learningSpace = learningSpaceRepository.findById(learningSpaceId)
            .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));

        StudyMaterial material = new StudyMaterial();
        material.setLearningSpaceId(learningSpaceId);
        material.setTitle(request.getTitle());
        material.setContent(request.getContent());
        material.setMaterialType(StudyMaterial.MaterialType.valueOf(request.getMaterialType().toUpperCase()));

        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            try {
                material.setTags(objectMapper.writeValueAsString(request.getTags()));
            } catch (JsonProcessingException e) {
                throw new BusinessException("标签格式错误");
            }
        }

        material.setCreatedAt(LocalDateTime.now());

        StudyMaterial saved = studyMaterialRepository.save(material);
        return convertToResponse(saved);
    }

    @Override
    public StudyMaterialResponse getStudyMaterialById(Long id) {
        StudyMaterial material = studyMaterialRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("学习资料不存在"));
        return convertToResponse(material);
    }

    @Override
    public Page<StudyMaterialResponse> getStudyMaterials(Long learningSpaceId, Pageable pageable) {
        return studyMaterialRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public Page<StudyMaterialResponse> getStudyMaterialsByType(Long learningSpaceId, String materialType, Pageable pageable) {
        StudyMaterial.MaterialType type = StudyMaterial.MaterialType.valueOf(materialType.toUpperCase());
        return studyMaterialRepository.findByLearningSpaceIdAndMaterialType(learningSpaceId, type, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public Page<StudyMaterialResponse> searchStudyMaterials(Long learningSpaceId, String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getStudyMaterials(learningSpaceId, pageable);
        }
        return studyMaterialRepository.findByLearningSpaceIdAndSearch(learningSpaceId, search, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public void deleteStudyMaterial(Long id) {
        StudyMaterial material = studyMaterialRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("学习资料不存在"));
        studyMaterialRepository.deleteById(id);
    }

    private StudyMaterialResponse convertToResponse(StudyMaterial material) {
        StudyMaterialResponse response = new StudyMaterialResponse();
        response.setId(material.getId());
        response.setLearningSpaceId(material.getLearningSpaceId());
        response.setMaterialType(material.getMaterialType().name());
        response.setTitle(material.getTitle());
        response.setContent(material.getContent());
        response.setSourceRefId(material.getSourceRefId());
        response.setCreatedAt(material.getCreatedAt());
        response.setUpdatedAt(material.getUpdatedAt());

        // 解析标签
        if (material.getTags() != null) {
            try {
                response.setTags(objectMapper.readValue(material.getTags(), List.class));
            } catch (JsonProcessingException e) {
                // 如果解析失败，返回空列表
                response.setTags(List.of());
            }
        } else {
            response.setTags(List.of());
        }

        return response;
    }
}