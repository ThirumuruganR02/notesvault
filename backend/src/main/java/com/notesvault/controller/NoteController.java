package com.notesvault.controller;

import com.notesvault.config.OpenApiConfig;
import com.notesvault.dto.NoteRequest;
import com.notesvault.model.Note;
import com.notesvault.security.UserPrincipal;
import com.notesvault.service.NoteService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Notes")
@SecurityRequirement(name = OpenApiConfig.BEARER_JWT)
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public List<Note> list(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "search", required = false) String search) {
        return noteService.findAllForUser(user.id(), tag, search);
    }

    @PostMapping
    public ResponseEntity<Note> create(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody NoteRequest request) {
        Note created = noteService.create(user.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @Valid @RequestBody NoteRequest request) {
        return noteService.update(user.id(), id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id) {
        if (!noteService.deleteForUser(user.id(), id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
