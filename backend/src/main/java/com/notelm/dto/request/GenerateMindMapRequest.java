package com.notelm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenerateMindMapRequest {

    @NotBlank(message = "导图标题不能为空")
    @Size(max = 255, message = "标题不能超过255个字符")
    private String title;

    @Size(max = 500, message = "描述不能超过500个字符")
    private String description;
}