package com.example.bibliotekbackenden.Configuration;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class JwtUtil {
    private final long expirationTime = 3600000L;
    private String SECRET;
    private boolean secretLoaded = false;

    @Autowired
    private VaultTemplate vaultTemplate;

    private void loadSecretFromVault() {
        if (secretLoaded)
            return; // Already loaded

        try {
            VaultKeyValueOperations kv = vaultTemplate.opsForKeyValue("secret",
                    VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);
            VaultResponse response = kv.get("jwt-secret");
            this.SECRET = (String) response.getRequiredData().get("value");
            this.secretLoaded = true;
            System.out.println("JWT Secret loaded from Vault");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWT secret from Vault", e);
        }
    }

    public String generateToken(String username) {
        loadSecretFromVault(); // Load on first use
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(generateKey())
                .compact();
    }

    public boolean validateToken(String token) {
        loadSecretFromVault(); // Load on first use
        try {
            extractUsername(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    public String extractUsername(String token) {
        loadSecretFromVault(); // Load on first use
        var parser = Jwts.parserBuilder().setSigningKey(generateKey()).build();
        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    private SecretKey generateKey() {
        String secret = SECRET;
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}