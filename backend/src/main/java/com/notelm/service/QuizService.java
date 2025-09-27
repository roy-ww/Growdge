package com.notelm.service;

import com.notelm.dto.request.GenerateQuizRequest;
import com.notelm.dto.response.QuizQuestionResponse;
import com.notelm.dto.response.QuizResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuizService {
    
    QuizResponse generateQuiz(Long learningSpaceId, GenerateQuizRequest request);
    
    QuizResponse getQuizById(Long id);
    
    Page<QuizResponse> getQuizzes(Long learningSpaceId, Pageable pageable);
    
    Page<QuizQuestionResponse> getQuizQuestions(Long quizId, Pageable pageable);
    
    Object submitQuiz(Long quizId, Object answers);
}