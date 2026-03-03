package com.rev.app.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.rev.app.dto.UserDTO;
import com.rev.app.entity.User;
import com.rev.app.repository.IUserRepository;
import com.rev.app.security.JwtProvider;
import com.rev.app.service.IEmailService;
import com.rev.app.service.IUserService;

class AuthRestControllerTest {

    private IUserService userService;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private IEmailService emailService;
    private IUserRepository userRepository;
    private AuthRestController authRestController;

    @BeforeEach
    void setUp() {
        userService = mock(IUserService.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtProvider = mock(JwtProvider.class);
        emailService = mock(IEmailService.class);
        userRepository = mock(IUserRepository.class);
        authRestController = new AuthRestController(userService, authenticationManager, jwtProvider, emailService, userRepository);
    }

    @Test
    void registerPersonal_Success() {
        AuthRestController.RegisterRequest request = new AuthRestController.RegisterRequest();
        request.setUser(new UserDTO());
        request.setPassword("password");

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@test.com");

        when(userService.registerUser(any(), any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = authRestController.registerPersonal(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test@test.com", response.getBody().getEmail());
    }

    @Test
    void login_Success() {
        AuthRestController.LoginRequest request = new AuthRestController.LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        Authentication auth = new UsernamePasswordAuthenticationToken("test@test.com", "password");
        User user = new User();
        user.setEmail("test@test.com");

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authRestController.authenticateUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("requiresOtp"));
    }
}
