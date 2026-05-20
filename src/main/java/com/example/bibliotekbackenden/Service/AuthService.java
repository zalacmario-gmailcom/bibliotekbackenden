package com.example.bibliotekbackenden.Service;

import org.springframework.stereotype.Service;

import com.example.bibliotekbackenden.Configuration.JwtUtil;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {
        if ("admin".equals(username) && "password".equals(password)) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
