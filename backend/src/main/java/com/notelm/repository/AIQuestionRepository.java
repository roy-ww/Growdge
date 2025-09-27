package com.notelm.repository;

import com.notelm.model.AIQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIQuestionRepository extends JpaRepository<AIQuestion, Long> {
    
    Page<AIQuestion> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
}