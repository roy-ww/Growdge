package com.notelm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddTextSourceRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题不能超过255个字符")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;
}