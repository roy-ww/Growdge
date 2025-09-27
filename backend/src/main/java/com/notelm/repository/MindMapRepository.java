package com.notelm.repository;

import com.notelm.model.MindMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MindMapRepository extends JpaRepository<MindMap, Long> {
    
    Page<MindMap> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    List<MindMap> findByLearningSpaceId(Long learningSpaceId);
}