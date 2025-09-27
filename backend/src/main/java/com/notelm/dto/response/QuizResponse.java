package com.notelm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizResponse {

    private Long id;
    private Long learningSpaceId;
    private String title;
    private String description;
    private Integer questionCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}