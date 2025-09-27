package com.notelm.service;

import com.notelm.dto.request.CreateNoteRequest;
import com.notelm.dto.request.UpdateNoteRequest;
import com.notelm.dto.response.NoteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    
    NoteResponse createNote(CreateNoteRequest request);
    
    NoteResponse getNoteById(Long id);
    
    Page<NoteResponse> getNotesByLearningSpaceId(Long learningSpaceId, Pageable pageable);
    
    NoteResponse updateNote(Long id, UpdateNoteRequest request);
    
    void deleteNote(Long id);
    
    Page<NoteResponse> searchNotes(Long learningSpaceId, String search, Pageable pageable);
}