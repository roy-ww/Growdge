package com.notelm.repository;

import com.notelm.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    Page<ChatMessage> findByChatSessionId(Long chatSessionId, Pageable pageable);
    
    List<ChatMessage> findByChatSessionIdOrderByCreatedAtAsc(Long chatSessionId);
}