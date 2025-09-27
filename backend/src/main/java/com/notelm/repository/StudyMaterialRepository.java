package com.notelm.repository;

import com.notelm.model.StudyMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
    
    Page<StudyMaterial> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    Page<StudyMaterial> findByLearningSpaceIdAndMaterialType(Long learningSpaceId, StudyMaterial.MaterialType materialType, Pageable pageable);
    
    @Query("SELECT sm FROM StudyMaterial sm WHERE sm.learningSpaceId = :learningSpaceId AND " +
           "(LOWER(sm.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(sm.content) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<StudyMaterial> findByLearningSpaceIdAndSearch(@Param("learningSpaceId") Long learningSpaceId, 
                                                      @Param("search") String search, 
                                                      Pageable pageable);
    
    List<StudyMaterial> findByLearningSpaceId(Long learningSpaceId);
    
    @Query("SELECT COUNT(sm) FROM StudyMaterial sm WHERE sm.learningSpaceId = :learningSpaceId")
    Long countByLearningSpaceId(@Param("learningSpaceId") Long learningSpaceId);
}