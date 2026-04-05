package com.notesvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NoteRequest(
        @NotBlank @Size(max = 200) String title,
        @NotNull @Size(max = 50000) String content,
        @Size(max = 30) List<@NotBlank @Size(max = 50) String> tags
) {
    public NoteRequest(String title, String content, List<String> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags == null ? List.of() : List.copyOf(tags);
    }
}
