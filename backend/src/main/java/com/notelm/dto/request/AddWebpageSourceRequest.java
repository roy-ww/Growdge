package com.notelm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddWebpageSourceRequest {

    @NotBlank(message = \"URL不能为空\")
    @Pattern(regexp = \"^https?://.+\\\\..+\", message = \"URL格式不正确\")
    private String url;

    @Size(max = 255, message = \"标题不能超过255个字符\")
    private String title;
}