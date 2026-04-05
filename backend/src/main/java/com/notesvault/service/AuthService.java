package com.notesvault.service;

import com.notesvault.dto.AuthResponse;
import com.notesvault.dto.LoginRequest;
import com.notesvault.dto.RegisterRequest;
import com.notesvault.model.Role;
import com.notesvault.model.User;
import com.notesvault.repository.UserRepository;
import com.notesvault.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        String username = request.username().trim();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        String token = buildToken(user);
        return toResponse(user, token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = buildToken(user);
        return toResponse(user, token);
    }

    private String buildToken(User user) {
        return jwtUtil.generateToken(
                user.getEmail(),
                Map.of(
                        "uid", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getRole().name()));
    }

    private static AuthResponse toResponse(User user, String token) {
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
