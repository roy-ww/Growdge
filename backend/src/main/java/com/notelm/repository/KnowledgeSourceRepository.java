package com.notelm.repository;

import com.notelm.model.KnowledgeSource;
import com.notelm.model.KnowledgeSource.SourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSource, Long> {
    
    Page<KnowledgeSource> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    Page<KnowledgeSource> findByLearningSpaceIdAndSourceType(Long learningSpaceId, SourceType sourceType, Pageable pageable);
    
    @Query("SELECT ks FROM KnowledgeSource ks WHERE ks.learningSpaceId = :learningSpaceId AND " +
           "(LOWER(ks.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(ks.content) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<KnowledgeSource> findByLearningSpaceIdAndSearch(@Param("learningSpaceId") Long learningSpaceId, 
                                                        @Param("search") String search, 
                                                        Pageable pageable);
    
    List<KnowledgeSource> findByLearningSpaceId(Long learningSpaceId);
    
    @Query("SELECT COUNT(ks) FROM KnowledgeSource ks WHERE ks.learningSpaceId = :learningSpaceId")
    Long countByLearningSpaceId(@Param("learningSpaceId") Long learningSpaceId);
}