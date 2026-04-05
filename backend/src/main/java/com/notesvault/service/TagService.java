package com.notesvault.service;

import com.notesvault.model.Tag;
import com.notesvault.model.User;
import com.notesvault.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Set<Tag> resolveTags(User owner, List<String> rawTags) {
        Set<Tag> result = new LinkedHashSet<>();
        Set<String> seen = new LinkedHashSet<>();
        for (String raw : rawTags) {
            if (raw == null || raw.isBlank()) {
                continue;
            }
            String name = normalizeName(raw);
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

    public static String normalizeName(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
