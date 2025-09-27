package com.notelm.controller;

import com.notelm.dto.request.CreateNoteRequest;
import com.notelm.dto.request.UpdateNoteRequest;
import com.notelm.dto.response.ApiResponse;
import com.notelm.dto.response.NoteResponse;
import com.notelm.service.NoteService;
import com.notelm.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learning-notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ApiResponse<NoteResponse> createNote(@Valid @RequestBody CreateNoteRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        NoteResponse response = noteService.createNote(request);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<NoteResponse>> getNotes(
            @RequestParam(required = false) Long spaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search) {
        
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        
        if (spaceId == null) {
            // 如果没有指定学习空间ID，可以返回所有笔记或抛出异常
            // 这里假设需要学习空间ID
            return ApiResponse.error("需要提供学习空间ID参数");
        }
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<NoteResponse> notes;
        if (search != null && !search.isEmpty()) {
            notes = noteService.searchNotes(spaceId, search, pageable);
        } else {
            notes = noteService.getNotesByLearningSpaceId(spaceId, pageable);
        }

        return ApiResponse.success(notes);
    }

    @GetMapping("/{id}")
    public ApiResponse<NoteResponse> getNote(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        NoteResponse response = noteService.getNoteById(id);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<NoteResponse> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNoteRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        NoteResponse response = noteService.updateNote(id, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNote(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            return ApiResponse.error("用户未认证");
        }
        noteService.deleteNote(id);
        return ApiResponse.success("笔记删除成功");
    }
}