package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.UserDTO;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void toDTO_Success() {
        User user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setRole(Role.PERSONAL);

        UserDTO dto = userMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFullName(), dto.getFullName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void toEntity_Success() {
        UserDTO dto = new UserDTO();
        dto.setId(2L);
        dto.setFullName("Jane Doe");
        dto.setEmail("jane@example.com");
        dto.setRole(Role.BUSINESS);

        User user = userMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getFullName(), user.getFullName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(Role.BUSINESS, user.getRole());
    }

    @Test
    void toDTO_Null_ReturnsNull() {
        assertNull(userMapper.toDTO(null));
    }

    @Test
    void toEntity_Null_ReturnsNull() {
        assertNull(userMapper.toEntity(null));
    }
}
