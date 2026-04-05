package com.notesvault.dto;

import com.notesvault.model.Role;

public record AuthResponse(
        String token,
        Long id,
        String username,
        String email,
        Role role
) {
}
