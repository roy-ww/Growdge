package com.notelm.controller;

import com.notelm.dto.request.GenerateQuizRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.QuizResponse;
import com.notelm.service.QuizService;
import com.notelm.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learning-spaces/{spaceId}/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/generate")
    public ApiResponse<QuizResponse> generateQuiz(
            @PathVariable Long spaceId,
            @Valid @RequestBody GenerateQuizRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        QuizResponse response = quizService.generateQuiz(spaceId, request);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<QuizResponse>> getQuizzes(
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<QuizResponse> quizzes = quizService.getQuizzes(spaceId, pageable);
        return ApiResponse.success(quizzes);
    }

    @GetMapping("/{id}")
    public ApiResponse<QuizResponse> getQuiz(@PathVariable Long spaceId, @PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        QuizResponse response = quizService.getQuizById(id);
        return ApiResponse.success(response);
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<Object> submitQuiz(@PathVariable Long spaceId, @PathVariable Long id, @RequestBody Object answers) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        Object result = quizService.submitQuiz(id, answers);
        return ApiResponse.success(result);
    }
}