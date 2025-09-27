package com.notelm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIQuestionResponse {

    private Long id;
    private Long learningSpaceId;
    private String question;
    private String answer;
    private List<Long> sourcesUsed;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}