package com.notelm.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class AIClient {

    @Autowired
    private AIProperties aiProperties;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    public AIClient() {
        this.restTemplate = new RestTemplate();
    }

    public String callQwenAPI(String prompt) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + aiProperties.getApiKey());

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", aiProperties.getModelName());
            
            Map<String, Object> input = new HashMap<>();
            input.put("prompt", prompt);
            requestBody.put("input", input);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("temperature", aiProperties.getTemperature());
            parameters.put("max_tokens", aiProperties.getMaxTokens());
            requestBody.put("parameters", parameters);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                aiProperties.getApiUrl(),
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            // 解析响应
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                JsonNode outputNode = jsonResponse.get("output");
                if (outputNode != null) {
                    JsonNode textNode = outputNode.get("text");
                    if (textNode != null) {
                        return textNode.asText();
                    }
                }
            }

            throw new RuntimeException("调用阿里通义千问API失败，响应内容：" + response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("调用阿里通义千问API时发生错误：" + e.getMessage(), e);
        }
    }
}