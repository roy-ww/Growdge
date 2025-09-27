package com.notelm.repository;

import com.notelm.model.KnowledgeCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeCardRepository extends JpaRepository<KnowledgeCard, Long> {
    
    Page<KnowledgeCard> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    List<KnowledgeCard> findByLearningSpaceId(Long learningSpaceId);
    
    List<KnowledgeCard> findByLearningSpaceIdAndStatus(Long learningSpaceId, KnowledgeCard.Status status);
}