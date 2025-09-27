package com.notelm.service;

import com.notelm.dto.request.CreateChatSessionRequest;
import com.notelm.dto.request.SendMessageRequest;
import com.notelm.dto.response.ChatMessageResponse;
import com.notelm.dto.response.ChatSessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatService {
    
    ChatSessionResponse createChatSession(Long learningSpaceId, CreateChatSessionRequest request);
    
    ChatSessionResponse getChatSessionById(Long id);
    
    Page<ChatSessionResponse> getChatSessions(Long learningSpaceId, Pageable pageable);
    
    ChatMessageResponse sendMessage(Long sessionId, SendMessageRequest request);
    
    Page<ChatMessageResponse> getChatMessages(Long sessionId, Pageable pageable);
    
    void closeChatSession(Long id);
}