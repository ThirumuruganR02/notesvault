package com.notesvault.service;

import com.notesvault.dto.NoteRequest;
import com.notesvault.model.Note;
import com.notesvault.model.User;
import com.notesvault.repository.NoteRepository;
import com.notesvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public List<Note> findAllForUser(Long userId) {
        User owner = resolveOwner(userId);
        return noteRepository.findAllByOwnerOrderByUpdatedAtDesc(owner);
    }

    @Transactional
    public Note create(Long userId, NoteRequest request) {
        User owner = resolveOwner(userId);
        Note note = Note.builder()
                .title(request.title().trim())
                .content(request.content())
                .owner(owner)
                .build();
        return noteRepository.save(note);
    }

    @Transactional
    public Optional<Note> update(Long userId, Long noteId, NoteRequest request) {
        User owner = resolveOwner(userId);
        return noteRepository.findByIdAndOwner(noteId, owner).map(note -> {
            note.setTitle(request.title().trim());
            note.setContent(request.content());
            return noteRepository.save(note);
        });
    }

    @Transactional
    public boolean deleteForUser(Long userId, Long noteId) {
        User owner = resolveOwner(userId);
        if (!noteRepository.existsByIdAndOwner(noteId, owner)) {
            return false;
        }
        noteRepository.deleteById(noteId);
        return true;
    }

    private User resolveOwner(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists"));
    }
}
