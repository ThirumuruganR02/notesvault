package com.notesvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteRequest(
        @NotBlank @Size(max = 200) String title,
        @NotNull @Size(max = 50000) String content
) {
}
