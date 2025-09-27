package com.notelm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateNoteRequest {

    @NotBlank(message = "笔记标题不能为空")
    @Size(max = 255, message = "标题不能超过255个字符")
    private String title;

    @NotBlank(message = "笔记内容不能为空")
    private String content;

    @Size(max = 20, message = "格式类型不能超过20个字符")
    private String contentFormat = "MARKDOWN";

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> tags;
}