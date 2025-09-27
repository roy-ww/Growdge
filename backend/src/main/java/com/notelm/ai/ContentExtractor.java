package com.notelm.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentExtractor {

    @Autowired
    private ObjectMapper objectMapper;

    public String extractAnswerFromAIResponse(String aiResponse) {
        // 简单的提取逻辑，实际可能需要更复杂的解析
        return aiResponse.trim();
    }

    public String extractQuestionFromAIResponse(String aiResponse) {
        // 从AI响应中提取问题部分
        String[] parts = aiResponse.split("\n");
        if (parts.length > 0) {
            return parts[0].replaceAll("^[Qq]uestion:\\s*", "").trim();
        }
        return aiResponse;
    }

    public String extractAnswerFromQA(String aiResponse) {
        // 从问答对中提取答案部分
        String[] parts = aiResponse.split("\n");
        if (parts.length > 1) {
            return parts[1].replaceAll("^[Aa]nswer:\\s*", "").trim();
        }
        return aiResponse;
    }

    public JsonNode extractJSONFromAIResponse(String aiResponse) {
        try {
            // 查找JSON部分，通常在AI响应中包含JSON格式的数据
            int start = aiResponse.indexOf('{');
            int end = aiResponse.lastIndexOf('}');
            if (start != -1 && end != -1 && end > start) {
                String jsonStr = aiResponse.substring(start, end + 1);
                return objectMapper.readTree(jsonStr);
            }
        } catch (Exception e) {
            // 如果解析失败，返回null
        }
        return null;
    }
}