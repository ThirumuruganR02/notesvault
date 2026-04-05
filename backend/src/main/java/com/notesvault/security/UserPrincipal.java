package com.notesvault.security;

public record UserPrincipal(Long id, String email, String username, String role) {
}
