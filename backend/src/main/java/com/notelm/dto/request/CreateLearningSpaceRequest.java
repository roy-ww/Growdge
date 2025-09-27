package com.notelm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateLearningSpaceRequest {

    @NotBlank(message = "学习空间名称不能为空")
    @Size(max = 100, message = "学习空间名称不能超过100个字符")
    private String name;

    @Size(max = 500, message = "描述不能超过500个字符")
    private String description;

    private String status = "ACTIVE";
}