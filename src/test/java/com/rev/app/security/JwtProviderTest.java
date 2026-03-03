package com.rev.app.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

class JwtProviderTest {

    private JwtProvider jwtProvider;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        userDetails = new User("test@test.com", "password", Collections.emptyList());
    }

    @Test
    void generateToken_Success() {
        String token = jwtProvider.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_Success() {
        String token = jwtProvider.generateToken(userDetails);
        String username = jwtProvider.extractUsername(token);
        assertEquals("test@test.com", username);
    }

    @Test
    void validateToken_Success() {
        String token = jwtProvider.generateToken(userDetails);
        assertTrue(jwtProvider.validateToken(token, userDetails));
    }
}
