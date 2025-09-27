package com.notelm.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateChatSessionRequest {

    @Size(max = 255, message = "标题不能超过255个字符")
    private String title;
}