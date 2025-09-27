package com.notelm.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenerateQuizRequest {

    @NotBlank(message = "测验标题不能为空")
    @Size(max = 255, message = "标题不能超过255个字符")
    private String title;

    @Min(value = 1, message = "题目数量至少为1")
    @Max(value = 50, message = "题目数量最多为50")
    private Integer questionCount = 10;

    @Min(value = 1, message = "难度级别至少为1")
    @Max(value = 5, message = "难度级别最多为5")
    private Integer difficultyLevel = 3;
}