package com.notesvault.service;

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
        return noteRepository.findAll();
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    @Transactional
    public Note create(String title, String content) {
        Note note = Note.builder()
                .title(title)
                .content(content)
                .build();
        return noteRepository.save(note);
    }

    @Transactional
    public Optional<Note> update(Long id, String title, String content) {
        return noteRepository.findById(id).map(note -> {
            note.setTitle(title);
            note.setContent(content);
            return noteRepository.save(note);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }
}
