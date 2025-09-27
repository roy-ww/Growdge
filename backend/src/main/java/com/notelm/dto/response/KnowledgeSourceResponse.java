package com.notelm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KnowledgeSourceResponse {

    private Long id;
    private Long learningSpaceId;
    private String sourceType;
    private String title;
    private String content;
    private String url;
    private String filePath;
    private Long fileSize;
    private Integer pageCount;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}