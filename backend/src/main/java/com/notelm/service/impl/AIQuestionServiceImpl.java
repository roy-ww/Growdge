package com.notelm.service.impl;

import com.notelm.dto.request.CreateAIQuestionRequest;
import com.notelm.dto.response.AIQuestionResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.AIQuestion;
import com.notelm.model.KnowledgeSource;
import com.notelm.repository.AIQuestionRepository;
import com.notelm.repository.KnowledgeSourceRepository;
import com.notelm.service.AIQuestionService;
import com.notelm.ai.QwenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AIQuestionServiceImpl implements AIQuestionService {

    @Autowired
    private AIQuestionRepository aiQuestionRepository;

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    @Autowired
    private QwenService qwenService;

    @Override
    public AIQuestionResponse askAIQuestion(Long learningSpaceId, CreateAIQuestionRequest request) {
        // 验证学习空间中的知识来源是否足够
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        if (knowledgeSources.isEmpty()) {
            throw new BusinessException("当前学习空间没有可用的知识来源，无法回答问题");
        }

        // 调用AI服务获取答案
        String answer = qwenService.generateAnswer(learningSpaceId, request.getQuestion());

        // 保存问题和答案
        AIQuestion aiQuestion = new AIQuestion();
        aiQuestion.setLearningSpaceId(learningSpaceId);
        aiQuestion.setQuestion(request.getQuestion());
        aiQuestion.setAnswer(answer);
        
        // 记录使用的知识来源ID
        List<Long> sourceIds = knowledgeSources.stream()
            .map(KnowledgeSource::getId)
            .collect(Collectors.toList());
        aiQuestion.setSourcesUsed(sourceIds.toString()); // 简化处理，实际可能需要更复杂的JSON序列化

        AIQuestion saved = aiQuestionRepository.save(aiQuestion);
        return convertToResponse(saved);
    }

    @Override
    public Page<AIQuestionResponse> getAIQuestions(Long learningSpaceId, Pageable pageable) {
        return aiQuestionRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public AIQuestionResponse getAIQuestionById(Long id) {
        AIQuestion question = aiQuestionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AI问答不存在"));
        return convertToResponse(question);
    }

    private AIQuestionResponse convertToResponse(AIQuestion question) {
        AIQuestionResponse response = new AIQuestionResponse();
        response.setId(question.getId());
        response.setLearningSpaceId(question.getLearningSpaceId());
        response.setQuestion(question.getQuestion());
        response.setAnswer(question.getAnswer());
        
        // 简化处理来源ID列表，实际实现中需要解析JSON格式的数据
        if (question.getSourcesUsed() != null && !question.getSourcesUsed().isEmpty()) {
            String sourcesStr = question.getSourcesUsed().replaceAll("[\\[\\]]", "");
            String[] sourceIds = sourcesStr.split(",");
            java.util.List<Long> sources = new java.util.ArrayList<>();
            for (String idStr : sourceIds) {
                try {
                    sources.add(Long.parseLong(idStr.trim()));
                } catch (NumberFormatException e) {
                    // 忽略无法解析的ID
                }
            }
            response.setSourcesUsed(sources);
        }
        
        response.setCreatedAt(question.getCreatedAt());
        return response;
    }
}