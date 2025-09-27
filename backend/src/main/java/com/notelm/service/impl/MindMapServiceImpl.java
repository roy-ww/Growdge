package com.notelm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notelm.dto.request.GenerateMindMapRequest;
import com.notelm.dto.response.MindMapResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.KnowledgeSource;
import com.notelm.model.MindMap;
import com.notelm.repository.KnowledgeSourceRepository;
import com.notelm.repository.MindMapRepository;
import com.notelm.service.MindMapService;
import com.notelm.ai.QwenService;
import com.notelm.ai.ContentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MindMapServiceImpl implements MindMapService {

    @Autowired
    private MindMapRepository mindMapRepository;

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    @Autowired
    private QwenService qwenService;

    @Autowired
    private ContentExtractor contentExtractor;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public MindMapResponse generateMindMap(Long learningSpaceId, GenerateMindMapRequest request) {
        // 获取学习空间中的知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        if (knowledgeSources.isEmpty()) {
            throw new BusinessException("当前学习空间没有可用的知识来源，无法生成思维导图");
        }

        // 调用AI服务生成思维导图内容
        String aiResponse = qwenService.generateMindMapContent(learningSpaceId, request.getTitle());

        // 使用内容提取器从AI响应中提取JSON格式的思维导图数据
        JsonNode jsonContent = contentExtractor.extractJSONFromAIResponse(aiResponse);

        // 创建思维导图实体
        MindMap mindMap = new MindMap();
        mindMap.setLearningSpaceId(learningSpaceId);
        mindMap.setTitle(request.getTitle());
        mindMap.setDescription(request.getDescription());

        // 保存JSON格式的节点和边数据
        if (jsonContent != null) {
            try {
                mindMap.setNodes(objectMapper.writeValueAsString(jsonContent.get("nodes")));
                mindMap.setEdges(objectMapper.writeValueAsString(jsonContent.get("edges")));
            } catch (JsonProcessingException e) {
                // 如果序列化失败，保存原始AI响应
                Map<String, Object> fallbackData = new HashMap<>();
                fallbackData.put("raw_response", aiResponse);
                try {
                    mindMap.setNodes(objectMapper.writeValueAsString(fallbackData));
                    mindMap.setEdges(objectMapper.writeValueAsString(new HashMap<>()));
                } catch (JsonProcessingException ex) {
                    // 最后的备选方案
                    mindMap.setNodes("{}");
                    mindMap.setEdges("{}");
                }
            }
        } else {
            // 如果无法从AI响应中提取JSON，保存原始响应
            Map<String, Object> rawData = new HashMap<>();
            rawData.put("raw_response", aiResponse);
            try {
                mindMap.setNodes(objectMapper.writeValueAsString(rawData));
                mindMap.setEdges(objectMapper.writeValueAsString(new HashMap<>()));
            } catch (JsonProcessingException e) {
                mindMap.setNodes("{}");
                mindMap.setEdges("{}");
            }
        }

        mindMap.setCreatedAt(LocalDateTime.now());

        MindMap savedMindMap = mindMapRepository.save(mindMap);
        return convertToResponse(savedMindMap);
    }

    @Override
    public MindMapResponse getMindMapById(Long id) {
        MindMap mindMap = mindMapRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("思维导图不存在"));
        return convertToResponse(mindMap);
    }

    @Override
    public Page<MindMapResponse> getMindMaps(Long learningSpaceId, Pageable pageable) {
        return mindMapRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToResponse);
    }

    private MindMapResponse convertToResponse(MindMap mindMap) {
        MindMapResponse response = new MindMapResponse();
        response.setId(mindMap.getId());
        response.setLearningSpaceId(mindMap.getLearningSpaceId());
        response.setTitle(mindMap.getTitle());
        response.setDescription(mindMap.getDescription());
        response.setCreatedAt(mindMap.getCreatedAt());
        response.setUpdatedAt(mindMap.getUpdatedAt());

        // 解析节点数据
        if (mindMap.getNodes() != null) {
            try {
                JsonNode nodesNode = objectMapper.readTree(mindMap.getNodes());
                response.setNodes(objectMapper.convertValue(nodesNode, Map.class));
            } catch (JsonProcessingException e) {
                response.setNodes(new HashMap<>());
            }
        } else {
            response.setNodes(new HashMap<>());
        }

        // 解析边数据
        if (mindMap.getEdges() != null) {
            try {
                JsonNode edgesNode = objectMapper.readTree(mindMap.getEdges());
                response.setEdges(objectMapper.convertValue(edgesNode, Map.class));
            } catch (JsonProcessingException e) {
                response.setEdges(new HashMap<>());
            }
        } else {
            response.setEdges(new HashMap<>());
        }

        return response;
    }
}