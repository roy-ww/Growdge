package com.notelm.repository;

import com.notelm.model.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    
    Page<ChatSession> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    List<ChatSession> findByLearningSpaceId(Long learningSpaceId);
}