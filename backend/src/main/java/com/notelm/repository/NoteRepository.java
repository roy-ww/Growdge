package com.notelm.repository;

import com.notelm.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    Page<Note> findByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    @Query("SELECT n FROM Note n WHERE n.learningSpaceId = :learningSpaceId AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(n.content) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Note> findByLearningSpaceIdAndSearch(@Param("learningSpaceId") Long learningSpaceId, 
                                             @Param("search") String search, 
                                             Pageable pageable);
    
    List<Note> findByLearningSpaceId(Long learningSpaceId);
}