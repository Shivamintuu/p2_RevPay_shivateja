package com.rev.app.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rev.app.dto.UserDTO;
import com.rev.app.service.IUserService;

class UserRestControllerTest {

    private IUserService userService;
    private UserRestController userRestController;

    @BeforeEach
    void setUp() {
        userService = mock(IUserService.class);
        userRestController = new UserRestController(userService);
    }

    @Test
    void getUserById_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFullName("Test User");

        when(userService.getUserById(1L)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userRestController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test User", response.getBody().getFullName());
    }

    @Test
    void getAllUsers_Success() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserDTO>> response = userRestController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void updateUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("Updated Name");

        when(userService.updateUser(anyLong(), any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userRestController.updateUser(1L, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getFullName());
    }

    @Test
    void deleteUser_Success() {
        ResponseEntity<Void> response = userRestController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }
}
