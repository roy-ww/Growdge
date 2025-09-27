package com.notelm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudyMaterialResponse {

    private Long id;
    private Long learningSpaceId;
    private String materialType;
    private String title;
    private String content;
    private Long sourceRefId;
    private List<String> tags;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}