package com.notelm.controller;

import com.notelm.dto.request.CreateChatSessionRequest;
import com.notelm.dto.request.SendMessageRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.ChatMessageResponse;
import com.notelm.dto.response.ChatSessionResponse;
import com.notelm.service.ChatService;
import com.notelm.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/learning-spaces/{spaceId}/chat-sessions")
    public ApiResponse<ChatSessionResponse> createChatSession(
            @PathVariable Long spaceId,
            @Valid @RequestBody CreateChatSessionRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        ChatSessionResponse response = chatService.createChatSession(spaceId, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/learning-spaces/{spaceId}/chat-sessions")
    public ApiResponse<Page<ChatSessionResponse>> getChatSessions(
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ChatSessionResponse> sessions = chatService.getChatSessions(spaceId, pageable);
        return ApiResponse.success(sessions);
    }

    @PostMapping("/chat-sessions/{sessionId}/messages")
    public ApiResponse<Object> sendMessage(
            @PathVariable Long sessionId,
            @Valid @RequestBody SendMessageRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        ChatMessageResponse response = chatService.sendMessage(sessionId, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/chat-sessions/{sessionId}/messages")
    public ApiResponse<Page<ChatMessageResponse>> getChatMessages(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ChatMessageResponse> messages = chatService.getChatMessages(sessionId, pageable);
        return ApiResponse.success(messages);
    }
}