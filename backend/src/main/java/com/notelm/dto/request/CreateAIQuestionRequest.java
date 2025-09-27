package com.notelm.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAIQuestionRequest {

    @NotBlank(message = "问题内容不能为空")
    private String question;
}