package com.notelm.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatServiceAdapter {

    @Autowired
    private QwenService qwenService;

    public String getAIResponse(Long learningSpaceId, String userMessage, String conversationHistory) {
        // 根据配置的AI提供商调用相应的服务
        // 目前只支持阿里通义千问
        return qwenService.generateChatReply(learningSpaceId, userMessage, conversationHistory);
    }
}