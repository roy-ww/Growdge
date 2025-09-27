package com.notelm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatMessageResponse {

    private Long id;
    private Long chatSessionId;
    private String senderType;
    private Long senderId;
    private String content;
    private List<Long> sourcesReferenced;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}