package com.notelm.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GenerateKnowledgeCardRequest {

    @Min(value = 1, message = "卡片数量至少为1")
    @Max(value = 50, message = "卡片数量最多为50")
    private Integer cardCount = 10;

    @Min(value = 1, message = "难度级别至少为1")
    @Max(value = 5, message = "难度级别最多为5")
    private Integer difficultyLevel = 3;
}