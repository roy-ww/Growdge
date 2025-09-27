package com.notelm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notelm.dto.request.CreateNoteRequest;
import com.notelm.dto.request.UpdateNoteRequest;
import com.notelm.dto.response.NoteResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.LearningSpace;
import com.notelm.model.Note;
import com.notelm.repository.LearningSpaceRepository;
import com.notelm.repository.NoteRepository;
import com.notelm.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private LearningSpaceRepository learningSpaceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public NoteResponse createNote(CreateNoteRequest request) {
        // 验证学习空间是否存在（如果指定了学习空间ID）
        if (request.getLearningSpaceId() != null) {
            LearningSpace learningSpace = learningSpaceRepository.findById(request.getLearningSpaceId())
                .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));
        }

        Note note = new Note();
        note.setLearningSpaceId(request.getLearningSpaceId());
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setContentFormat(Note.ContentFormat.valueOf(
            request.getContentFormat() != null ? 
            request.getContentFormat().toUpperCase() : 
            "MARKDOWN"));

        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            try {
                note.setTags(objectMapper.writeValueAsString(request.getTags()));
            } catch (JsonProcessingException e) {
                throw new BusinessException("标签格式错误");
            }
        }

        Note saved = noteRepository.save(note);
        return convertToResponse(saved);
    }

    @Override
    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("笔记不存在"));
        return convertToResponse(note);
    }

    @Override
    public Page<NoteResponse> getNotesByLearningSpaceId(Long learningSpaceId, Pageable pageable) {
        return noteRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public NoteResponse updateNote(Long id, UpdateNoteRequest request) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("笔记不存在"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        if (request.getContentFormat() != null) {
            note.setContentFormat(Note.ContentFormat.valueOf(request.getContentFormat().toUpperCase()));
        }

        // 处理标签
        if (request.getTags() != null) {
            try {
                note.setTags(objectMapper.writeValueAsString(request.getTags()));
            } catch (JsonProcessingException e) {
                throw new BusinessException("标签格式错误");
            }
        }

        Note updated = noteRepository.save(note);
        return convertToResponse(updated);
    }

    @Override
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("笔记不存在"));
        noteRepository.deleteById(id);
    }

    @Override
    public Page<NoteResponse> searchNotes(Long learningSpaceId, String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getNotesByLearningSpaceId(learningSpaceId, pageable);
        }
        return noteRepository.findByLearningSpaceIdAndSearch(learningSpaceId, search, pageable)
            .map(this::convertToResponse);
    }

    private NoteResponse convertToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setLearningSpaceId(note.getLearningSpaceId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setContentFormat(note.getContentFormat() != null ? note.getContentFormat().name() : "MARKDOWN");
        
        // 解析标签
        if (note.getTags() != null) {
            try {
                response.setTags(objectMapper.readValue(note.getTags(), List.class));
            } catch (JsonProcessingException e) {
                // 如果解析失败，返回空列表
                response.setTags(List.of());
            }
        } else {
            response.setTags(List.of());
        }
        
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        return response;
    }
}