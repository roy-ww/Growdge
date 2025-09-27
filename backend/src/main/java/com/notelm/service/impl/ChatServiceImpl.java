package com.notelm.service.impl;

import com.notelm.dto.request.CreateChatSessionRequest;
import com.notelm.dto.request.SendMessageRequest;
import com.notelm.dto.response.ChatMessageResponse;
import com.notelm.dto.response.ChatSessionResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.ChatMessage;
import com.notelm.model.ChatSession;
import com.notelm.model.KnowledgeSource;
import com.notelm.repository.ChatMessageRepository;
import com.notelm.repository.ChatSessionRepository;
import com.notelm.repository.KnowledgeSourceRepository;
import com.notelm.service.ChatService;
import com.notelm.ai.ChatServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    @Autowired
    private ChatServiceAdapter chatServiceAdapter;

    @Override
    public ChatSessionResponse createChatSession(Long learningSpaceId, CreateChatSessionRequest request) {
        // 验证学习空间中是否有知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        if (knowledgeSources.isEmpty()) {
            throw new BusinessException("当前学习空间没有可用的知识来源，无法创建聊天会话");
        }

        ChatSession session = new ChatSession();
        session.setLearningSpaceId(learningSpaceId);
        session.setTitle(request.getTitle());
        session.setStatus(ChatSession.Status.ACTIVE);

        ChatSession saved = chatSessionRepository.save(session);
        return convertToSessionResponse(saved);
    }

    @Override
    public ChatSessionResponse getChatSessionById(Long id) {
        ChatSession session = chatSessionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("聊天会话不存在"));
        return convertToSessionResponse(session);
    }

    @Override
    public Page<ChatSessionResponse> getChatSessions(Long learningSpaceId, Pageable pageable) {
        return chatSessionRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToSessionResponse);
    }

    @Override
    public ChatMessageResponse sendMessage(Long sessionId, SendMessageRequest request) {
        // 验证聊天会话是否存在且活跃
        ChatSession session = chatSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("聊天会话不存在"));
        
        if (session.getStatus() == ChatSession.Status.CLOSED) {
            throw new BusinessException("聊天会话已关闭，无法发送消息");
        }

        // 保存用户消息
        ChatMessage userMessage = new ChatMessage();
        userMessage.setChatSessionId(sessionId);
        userMessage.setSenderType(ChatMessage.SenderType.USER);
        userMessage.setContent(request.getContent());
        ChatMessage savedUserMessage = chatMessageRepository.save(userMessage);

        // 获取会话历史用于AI回复
        List<ChatMessage> conversationHistory = chatMessageRepository
            .findByChatSessionIdOrderByCreatedAtAsc(sessionId);

        // 构建历史对话字符串
        StringBuilder historyBuilder = new StringBuilder();
        for (ChatMessage msg : conversationHistory) {
            historyBuilder.append(msg.getSenderType())
                         .append(": ")
                         .append(msg.getContent())
                         .append("\n");
        }

        // 调用AI服务获取回复
        String aiReply = chatServiceAdapter.getAIResponse(
            session.getLearningSpaceId(), 
            request.getContent(), 
            historyBuilder.toString()
        );

        // 保存AI回复
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setChatSessionId(sessionId);
        aiMessage.setSenderType(ChatMessage.SenderType.AI);
        aiMessage.setContent(aiReply);
        // 注：这里简化处理，实际实现中需要提取AI回复中引用的来源信息
        ChatMessage savedAiMessage = chatMessageRepository.save(aiMessage);

        return convertToMessageResponse(savedAiMessage);
    }

    @Override
    public Page<ChatMessageResponse> getChatMessages(Long sessionId, Pageable pageable) {
        return chatMessageRepository.findByChatSessionId(sessionId, pageable)
            .map(this::convertToMessageResponse);
    }

    @Override
    public void closeChatSession(Long id) {
        ChatSession session = chatSessionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("聊天会话不存在"));
        
        session.setStatus(ChatSession.Status.CLOSED);
        chatSessionRepository.save(session);
    }

    private ChatSessionResponse convertToSessionResponse(ChatSession session) {
        ChatSessionResponse response = new ChatSessionResponse();
        response.setId(session.getId());
        response.setLearningSpaceId(session.getLearningSpaceId());
        response.setTitle(session.getTitle());
        response.setStatus(session.getStatus().name());
        
        // 设置消息数量（简化处理）
        List<ChatMessage> messages = chatMessageRepository.findByChatSessionId(session.getId());
        response.setMessageCount(messages.size());
        
        // 设置最后消息时间（如果有消息的话）
        if (!messages.isEmpty()) {
            response.setLastMessageAt(messages.get(messages.size() - 1).getCreatedAt());
        }
        
        response.setCreatedAt(session.getCreatedAt());
        response.setUpdatedAt(session.getUpdatedAt());
        return response;
    }

    private ChatMessageResponse convertToMessageResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setChatSessionId(message.getChatSessionId());
        response.setSenderType(message.getSenderType().name());
        response.setContent(message.getContent());
        response.setCreatedAt(message.getCreatedAt());
        
        // 简化处理来源引用，实际实现中需要解析JSON格式的数据
        if (message.getSourcesReferenced() != null) {
            String sourcesStr = message.getSourcesReferenced().replaceAll("[\\[\\]]", "");
            String[] sourceIds = sourcesStr.split(",");
            java.util.List<Long> sources = new java.util.ArrayList<>();
            for (String idStr : sourceIds) {
                try {
                    sources.add(Long.parseLong(idStr.trim()));
                } catch (NumberFormatException e) {
                    // 忽略无法解析的ID
                }
            }
            response.setSourcesReferenced(sources);
        }
        
        return response;
    }
}