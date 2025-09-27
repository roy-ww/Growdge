package com.notelm.service;

import com.notelm.dto.request.CreateAIQuestionRequest;
import com.notelm.dto.response.AIQuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AIQuestionService {
    
    AIQuestionResponse askAIQuestion(Long learningSpaceId, CreateAIQuestionRequest request);
    
    Page<AIQuestionResponse> getAIQuestions(Long learningSpaceId, Pageable pageable);
    
    AIQuestionResponse getAIQuestionById(Long id);
}