package com.notelm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateStudyMaterialRequest {

    @NotBlank(message = "资料标题不能为空")
    @Size(max = 255, message = "标题不能超过255个字符")
    private String title;

    private String content;

    @NotBlank(message = "资料类型不能为空")
    @Size(max = 20, message = "资料类型不能超过20个字符")
    private String materialType;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> tags;
}