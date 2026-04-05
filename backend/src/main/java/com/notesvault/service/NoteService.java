package com.notesvault.service;

import com.notesvault.dto.NoteRequest;
import com.notesvault.model.Note;
import com.notesvault.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> findAll() {
        return noteRepository.findAllByOrderByUpdatedAtDesc();
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    @Transactional
    public Note create(NoteRequest request) {
        Note note = Note.builder()
                .title(request.title().trim())
                .content(request.content())
                .build();
        return noteRepository.save(note);
    }

    @Transactional
    public Optional<Note> update(Long id, NoteRequest request) {
        return noteRepository.findById(id).map(note -> {
            note.setTitle(request.title().trim());
            note.setContent(request.content());
            return noteRepository.save(note);
        });
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (!noteRepository.existsById(id)) {
            return false;
        }
        noteRepository.deleteById(id);
        return true;
    }
}
