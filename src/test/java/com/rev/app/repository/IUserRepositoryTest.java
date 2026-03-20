package com.rev.app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class IUserRepositoryTest {

    @Mock
    private IUserRepository userRepository;

    @Test
    void findByEmail_ReturnsUser() {
        User user = new User();
        user.setEmail("repo@test.com");
        user.setFullName("Repo Test");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);

        when(userRepository.findByEmail("repo@test.com")).thenReturn(Optional.of(user));

        Optional<User> found = userRepository.findByEmail("repo@test.com");

        assertTrue(found.isPresent());
        assertEquals("repo@test.com", found.get().getEmail());
    }

    @Test
    void findByPhoneNumber_ReturnsUser() {
        User user = new User();
        user.setEmail("repo2@test.com");
        user.setPhoneNumber("1234567890");
        user.setFullName("Repo Test 2");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);

        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.of(user));

        Optional<User> found = userRepository.findByPhoneNumber("1234567890");

        assertTrue(found.isPresent());
        assertEquals("1234567890", found.get().getPhoneNumber());
    }

    @Test
    void findByFullNameIgnoreCase_ReturnsUser() {
        User user = new User();
        user.setEmail("repo3@test.com");
        user.setFullName("JEAN LUC");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);

        when(userRepository.findByFullNameIgnoreCase("jean luc")).thenReturn(Optional.of(user));

        Optional<User> found = userRepository.findByFullNameIgnoreCase("jean luc");

        assertTrue(found.isPresent());
        assertEquals("JEAN LUC", found.get().getFullName());
    }
}
