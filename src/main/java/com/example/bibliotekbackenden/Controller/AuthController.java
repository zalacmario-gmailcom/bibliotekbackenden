package com.example.bibliotekbackenden.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliotekbackenden.Dto.Auth.AuthRequestDTO;
import com.example.bibliotekbackenden.Dto.Auth.AuthResponseDTO;
import com.example.bibliotekbackenden.Service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "Bearer")
@Tag(name = "Authentication", description = "Endpoints for user authentication and token generation")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login to access endpoints")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        String token = authService.login(
                request.username(),
                request.password());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}