package com.notelm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class QuizQuestionResponse {

    private Long id;
    private Long quizId;
    private String questionText;
    private String questionType;
    private Map<String, String> options;
    private String correctAnswer;
    private String explanation;
    private Integer difficultyLevel;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}