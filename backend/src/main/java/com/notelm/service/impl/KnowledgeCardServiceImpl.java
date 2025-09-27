package com.notelm.service.impl;

import com.notelm.dto.request.GenerateKnowledgeCardRequest;
import com.notelm.dto.response.KnowledgeCardResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.KnowledgeCard;
import com.notelm.model.KnowledgeSource;
import com.notelm.repository.KnowledgeCardRepository;
import com.notelm.repository.KnowledgeSourceRepository;
import com.notelm.service.KnowledgeCardService;
import com.notelm.ai.QwenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class KnowledgeCardServiceImpl implements KnowledgeCardService {

    @Autowired
    private KnowledgeCardRepository knowledgeCardRepository;

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    @Autowired
    private QwenService qwenService;

    @Override
    public Page<KnowledgeCardResponse> getKnowledgeCards(Long learningSpaceId, Pageable pageable) {
        return knowledgeCardRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public KnowledgeCardResponse getKnowledgeCardById(Long id) {
        KnowledgeCard card = knowledgeCardRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("知识卡片不存在"));
        return convertToResponse(card);
    }

    @Override
    public Page<KnowledgeCardResponse> getKnowledgeCardsByStatus(Long learningSpaceId, String status, Pageable pageable) {
        KnowledgeCard.Status cardStatus = KnowledgeCard.Status.valueOf(status.toUpperCase());
        List<KnowledgeCard> cards = knowledgeCardRepository.findByLearningSpaceIdAndStatus(learningSpaceId, cardStatus);
        
        // 因为JPA的分页方法不能直接用于List，我们需要手动处理分页
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cards.size());
        
        if (start > cards.size()) {
            start = cards.size();
        }
        
        List<KnowledgeCard> pagedCards = cards.subList(start, end);
        return Page.empty(); // 这里需要更复杂的实现，暂时返回空页
    }

    @Override
    public void markCardAsReviewed(Long id) {
        KnowledgeCard card = knowledgeCardRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("知识卡片不存在"));
        
        card.setLastReviewedAt(LocalDateTime.now());
        card.setReviewCount(card.getReviewCount() + 1);
        
        // 简单的复习间隔算法（实际实现中可以使用更复杂的算法如SM-2）
        card.setNextReviewAt(LocalDateTime.now().plusDays(1)); // 暂定1天后复习
        
        knowledgeCardRepository.save(card);
    }

    @Override
    public KnowledgeCardResponse generateKnowledgeCard(Long id) {
        // 这个方法用于更新单个卡片（比如重新生成内容）
        // 实际上我们不需要重新生成，只是返回卡片
        return getKnowledgeCardById(id);
    }

    @Override
    public void generateKnowledgeCards(Long learningSpaceId, GenerateKnowledgeCardRequest request) {
        // 获取学习空间中的知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        if (knowledgeSources.isEmpty()) {
            throw new BusinessException("当前学习空间没有可用的知识来源，无法生成知识卡片");
        }

        // 从知识来源中提取关键概念
        StringBuilder content = new StringBuilder();
        for (KnowledgeSource source : knowledgeSources) {
            content.append(source.getContent()).append("\n");
        }

        // 使用AI服务生成多个知识卡片
        // 这里我们模拟生成指定数量的卡片
        for (int i = 0; i < request.getCardCount(); i++) {
            String aiResponse = qwenService.generateKnowledgeCardQuestion(learningSpaceId, "概念" + (i + 1));
            
            // 解析AI响应，提取问题和答案
            String[] parts = aiResponse.split("\n");
            String question = "";
            String answer = "";
            
            for (String part : parts) {
                if (part.toLowerCase().startsWith("question:") || part.toLowerCase().startsWith("问题：")) {
                    question = part.substring(part.indexOf(":") + 1).trim();
                } else if (part.toLowerCase().startsWith("answer:") || part.toLowerCase().startsWith("答案：")) {
                    answer = part.substring(part.indexOf(":") + 1).trim();
                }
            }
            
            // 如果AI响应格式不标准，直接使用整个响应作为答案，并生成一个问题
            if (question.isEmpty()) {
                question = "关于 " + knowledgeSources.get(0).getTitle() + " 的一个知识点";
                answer = aiResponse;
            }

            // 创建知识卡片
            KnowledgeCard card = new KnowledgeCard();
            card.setLearningSpaceId(learningSpaceId);
            card.setQuestion(question);
            card.setAnswer(answer);
            card.setCardType(KnowledgeCard.CardType.QA);
            card.setDifficultyLevel(request.getDifficultyLevel());
            card.setStatus(KnowledgeCard.Status.ACTIVE);

            knowledgeCardRepository.save(card);
        }
    }

    private KnowledgeCardResponse convertToResponse(KnowledgeCard card) {
        KnowledgeCardResponse response = new KnowledgeCardResponse();
        response.setId(card.getId());
        response.setLearningSpaceId(card.getLearningSpaceId());
        response.setQuestion(card.getQuestion());
        response.setAnswer(card.getAnswer());
        response.setCardType(card.getCardType().name());
        response.setDifficultyLevel(card.getDifficultyLevel());
        response.setReviewCount(card.getReviewCount());
        response.setLastReviewedAt(card.getLastReviewedAt());
        response.setNextReviewAt(card.getNextReviewAt());
        response.setStatus(card.getStatus().name());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }
}