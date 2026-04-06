package com.notesvault.service;

import com.notesvault.dto.NoteRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoteService {

    private final List<Map<String, Object>> notes = new ArrayList<>();
    private long currentId = 1;

    public List<Map<String, Object>> getAllNotes(String tag, String search) {
        List<Map<String, Object>> result = new ArrayList<>(notes);

        if (search != null && !search.trim().isEmpty()) {
            String keyword = search.toLowerCase();
            result = result.stream()
                    .filter(note -> {
                        Object title = note.get("title");
                        Object content = note.get("content");
                        return (title != null && title.toString().toLowerCase().contains(keyword))
                                || (content != null && content.toString().toLowerCase().contains(keyword));
                    })
                    .toList();
        }

        return result;
    }

    public Map<String, Object> getNoteById(Long id) {
        return notes.stream()
                .filter(note -> note.get("id").equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
    }

    public Map<String, Object> createNote(NoteRequest request) {
        Map<String, Object> note = new HashMap<>();
        note.put("id", currentId++);
        note.put("title", extractTitle(request));
        note.put("content", extractContent(request));
        notes.add(note);
        return note;
    }

    public Map<String, Object> updateNote(Long id, NoteRequest request) {
        Map<String, Object> note = getNoteById(id);
        note.put("title", extractTitle(request));
        note.put("content", extractContent(request));
        return note;
    }

    public void deleteNote(Long id) {
        Map<String, Object> note = getNoteById(id);
        notes.remove(note);
    }

    private String extractTitle(NoteRequest request) {
        try {
            return (String) request.getClass().getMethod("getTitle").invoke(request);
        } catch (Exception e) {
            return "Untitled";
        }
    }

    private String extractContent(NoteRequest request) {
        try {
            return (String) request.getClass().getMethod("getContent").invoke(request);
        } catch (Exception e) {
            return "";
        }
    }
}