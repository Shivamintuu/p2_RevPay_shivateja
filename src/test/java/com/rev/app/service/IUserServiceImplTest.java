package com.rev.app.service;

import com.rev.app.dto.UserDTO;
import com.rev.app.entity.User;
import com.rev.app.exception.BadRequestException;
import com.rev.app.mapper.UserMapper;
import com.rev.app.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IUserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IWalletService walletService;

    @Mock
    private IEmailService emailService;

    @InjectMocks
    private IUserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        UserDTO inputDto = new UserDTO();
        inputDto.setEmail("test@test.com");
        inputDto.setPhoneNumber("1234567890");

        User entity = new User();
        entity.setId(1L);
        entity.setEmail("test@test.com");

        UserDTO outputDto = new UserDTO();
        outputDto.setId(1L);
        outputDto.setEmail("test@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenReturn(entity);
        when(userMapper.toDTO(any(User.class))).thenReturn(outputDto);
        // Do nothing for wallet creation simulation
        doReturn(null).when(walletService).createWallet(anyLong());

        UserDTO result = userService.registerUser(inputDto, "password123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(any(User.class));
        verify(walletService, times(1)).createWallet(1L);
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        UserDTO inputDto = new UserDTO();
        inputDto.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> userService.registerUser(inputDto, "password123"));
        verify(userRepository, never()).save(any(User.class));
    }
}
