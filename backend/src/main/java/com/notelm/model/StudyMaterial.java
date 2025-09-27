package com.notelm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_materials")
@Data
public class StudyMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "learning_space_id", nullable = false)
    private Long learningSpaceId;

    @Column(name = "material_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "source_ref_id")
    private Long sourceRefId; // 关联来源ID

    @Column(columnDefinition = "jsonb")
    private String tags; // JSONB格式存储标签

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public enum MaterialType {
        AI_ANSWER, KNOWLEDGE_CARD, MIND_MAP, QUIZ, CUSTOM
    }
}