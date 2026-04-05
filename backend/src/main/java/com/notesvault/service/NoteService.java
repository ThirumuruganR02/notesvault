package com.notesvault.service;

import com.notesvault.dto.NoteRequest;
import com.notesvault.model.Note;
import com.notesvault.model.User;
import com.notesvault.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final TagService tagService;

    /**
     * Lists notes for the user. Text search runs in memory on decrypted content — DB stores ciphertext, so SQL LIKE
     * on {@code content} is not used.
     */
    public List<Note> findAllForUser(Long userId, String tagFilter, String searchQuery) {
        User owner = userService.requireById(userId);
        Optional<String> searchTerm = normalizeSearchTerm(searchQuery);
        boolean hasTag = hasTagFilter(tagFilter);
        String normalizedTag = hasTag ? TagService.normalizeName(tagFilter) : "";

        if (hasTag && normalizedTag.isEmpty()) {
            hasTag = false;
        }

        List<Note> notes;
        if (!hasTag) {
            notes = noteRepository.findAllByOwnerOrderByUpdatedAtDesc(owner);
        } else {
            notes = noteRepository.findAllByOwnerAndTagNameOrderByUpdatedAtDesc(owner, normalizedTag);
        }

        if (searchTerm.isPresent()) {
            String term = searchTerm.get();
            notes = notes.stream().filter(n -> matchesTextSearch(n, term)).toList();
        }

        log.info(
                "Notes listed: userId={}, count={}, tagFilterActive={}, textSearchActive={}",
                userId,
                notes.size(),
                hasTag,
                searchTerm.isPresent());
        return notes;
    }

    @Transactional
    public Note create(Long userId, NoteRequest request) {
        User owner = userService.requireById(userId);
        Note note = Note.builder()
                .title(request.title().trim())
                .content(request.content())
                .owner(owner)
                .tags(tagService.resolveTags(owner, request.tags()))
                .build();
        Note saved = noteRepository.save(note);
        log.info(
                "Note created: userId={}, noteId={}, titleLength={}, tagCount={}",
                userId,
                saved.getId(),
                saved.getTitle().length(),
                saved.getTags().size());
        return saved;
    }

    @Transactional
    public Optional<Note> update(Long userId, Long noteId, NoteRequest request) {
        User owner = userService.requireById(userId);
        Optional<Note> updated =
                noteRepository.findByIdAndOwner(noteId, owner).map(note -> {
                    note.setTitle(request.title().trim());
                    note.setContent(request.content());
                    note.setTags(tagService.resolveTags(owner, request.tags()));
                    return noteRepository.save(note);
                });
        if (updated.isPresent()) {
            Note n = updated.get();
            log.info(
                    "Note updated: userId={}, noteId={}, titleLength={}, tagCount={}",
                    userId,
                    n.getId(),
                    n.getTitle().length(),
                    n.getTags().size());
        } else {
            log.warn("Note update skipped (not found): userId={}, noteId={}", userId, noteId);
        }
        return updated;
    }

    @Transactional
    public boolean deleteForUser(Long userId, Long noteId) {
        User owner = userService.requireById(userId);
        if (!noteRepository.existsByIdAndOwner(noteId, owner)) {
            log.warn("Note delete skipped (not found): userId={}, noteId={}", userId, noteId);
            return false;
        }
        noteRepository.deleteById(noteId);
        log.info("Note deleted: userId={}, noteId={}", userId, noteId);
        return true;
    }

    private static boolean hasTagFilter(String tagFilter) {
        return tagFilter != null && !tagFilter.isBlank();
    }

    private static Optional<String> normalizeSearchTerm(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        String stripped = raw.trim().replace("%", "").replace("_", "").replace("\\", "");
        if (stripped.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(stripped.toLowerCase(Locale.ROOT));
    }

    private static boolean matchesTextSearch(Note note, String termLower) {
        String title = note.getTitle().toLowerCase(Locale.ROOT);
        String content = note.getContent().toLowerCase(Locale.ROOT);
        return title.contains(termLower) || content.contains(termLower);
    }
}
