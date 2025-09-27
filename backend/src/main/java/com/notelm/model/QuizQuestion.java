package com.notelm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_questions")
@Data
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_id", nullable = false)
    private Long quizId;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "question_type", length = 20)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType = QuestionType.MULTIPLE_CHOICE;

    @Column(columnDefinition = "jsonb")
    private String options; // JSONB格式存储选择题选项

    @Column(name = "correct_answer", length = 10)
    private String correctAnswer;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel = 1; // 1-5级难度

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public enum QuestionType {
        MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
    }
}