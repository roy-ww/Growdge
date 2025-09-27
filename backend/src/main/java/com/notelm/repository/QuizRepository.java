package com.notelm.repository;

import com.notelm.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    Page<Quiz> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    List<Quiz> findByLearningSpaceId(Long learningSpaceId);
}