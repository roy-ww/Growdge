package com.notelm.repository;

import com.notelm.model.LearningSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningSpaceRepository extends JpaRepository<LearningSpace, Long> {
    
    Page<LearningSpace> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT ls FROM LearningSpace ls WHERE ls.userId = :userId AND LOWER(ls.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<LearningSpace> findByUserIdAndNameContainingIgnoreCase(@Param("userId") Long userId, 
                                                               @Param("search") String search, 
                                                               Pageable pageable);
    
    @Query("SELECT ls FROM LearningSpace ls WHERE ls.userId = :userId")
    List<LearningSpace> findByUserId(@Param("userId") Long userId);
}