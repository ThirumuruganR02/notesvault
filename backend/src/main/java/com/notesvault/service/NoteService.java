package com.notesvault.service;

import com.notesvault.dto.NoteRequest;
import com.notesvault.model.Note;
import com.notesvault.model.Tag;
import com.notesvault.model.User;
import com.notesvault.repository.NoteRepository;
import com.notesvault.repository.TagRepository;
import com.notesvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public List<Note> findAllForUser(Long userId, String tagFilter, String searchQuery) {
        User owner = resolveOwner(userId);
        Optional<String> searchPattern = buildSearchPattern(searchQuery);
        boolean hasTag = hasTagFilter(tagFilter);
        String normalizedTag = hasTag ? normalizeTag(tagFilter) : "";

        if (hasTag && normalizedTag.isEmpty()) {
            hasTag = false;
        }

        if (searchPattern.isEmpty()) {
            if (!hasTag) {
                return noteRepository.findAllByOwnerOrderByUpdatedAtDesc(owner);
            }
            return noteRepository.findAllByOwnerAndTagNameOrderByUpdatedAtDesc(owner, normalizedTag);
        }

        String pattern = searchPattern.get();
        if (!hasTag) {
            return noteRepository.findAllByOwnerAndTextSearchOrderByUpdatedAtDesc(owner, pattern);
        }
        return noteRepository.findAllByOwnerTagAndTextSearchOrderByUpdatedAtDesc(
                owner, normalizedTag, pattern);
    }

    @Transactional
    public Note create(Long userId, NoteRequest request) {
        User owner = resolveOwner(userId);
        Note note = Note.builder()
                .title(request.title().trim())
                .content(request.content())
                .owner(owner)
                .tags(resolveTags(owner, request.tags()))
                .build();
        return noteRepository.save(note);
    }

    @Transactional
    public Optional<Note> update(Long userId, Long noteId, NoteRequest request) {
        User owner = resolveOwner(userId);
        return noteRepository.findByIdAndOwner(noteId, owner).map(note -> {
            note.setTitle(request.title().trim());
            note.setContent(request.content());
            note.setTags(resolveTags(owner, request.tags()));
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

    private Set<Tag> resolveTags(User owner, List<String> rawTags) {
        Set<Tag> result = new LinkedHashSet<>();
        Set<String> seen = new LinkedHashSet<>();
        for (String raw : rawTags) {
            if (raw == null || raw.isBlank()) {
                continue;
            }
            String name = normalizeTag(raw);
            if (name.isEmpty() || !seen.add(name)) {
                continue;
            }
            Tag tag = tagRepository
                    .findByOwnerAndName(owner, name)
                    .orElseGet(() -> tagRepository.save(Tag.builder().owner(owner).name(name).build()));
            result.add(tag);
        }
        return result;
    }

    private static String normalizeTag(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean hasTagFilter(String tagFilter) {
        return tagFilter != null && !tagFilter.isBlank();
    }

    /**
     * Case-insensitive substring match; strips LIKE wildcards from input so {@code %} / {@code _} are literal-safe.
     */
    private static Optional<String> buildSearchPattern(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        String stripped = raw.trim().replace("%", "").replace("_", "").replace("\\", "");
        if (stripped.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of("%" + stripped.toLowerCase(Locale.ROOT) + "%");
    }

    private User resolveOwner(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists"));
    }
}
