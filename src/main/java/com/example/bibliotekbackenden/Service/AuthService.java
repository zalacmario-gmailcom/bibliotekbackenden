package com.example.bibliotekbackenden.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bibliotekbackenden.Configuration.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        if ("admin".equals(username) && "password".equals(password)) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
