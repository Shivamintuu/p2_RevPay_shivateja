package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.NotificationDTO;
import com.rev.app.entity.Notification;
import com.rev.app.entity.Notification.NotificationType;
import com.rev.app.entity.User;

class NotificationMapperTest {

    private NotificationMapper notificationMapper;

    @BeforeEach
    void setUp() {
        notificationMapper = new NotificationMapper();
    }

    @Test
    void toDTO_Success() {
        User user = new User();
        user.setId(1L);

        Notification notif = new Notification();
        notif.setId(400L);
        notif.setMessage("Test Message");
        notif.setType(NotificationType.TRANSACTION);
        notif.setRead(false);
        notif.setCreatedAt(LocalDateTime.now());
        notif.setUser(user);

        NotificationDTO dto = notificationMapper.toDTO(notif);

        assertNotNull(dto);
        assertEquals(notif.getId(), dto.getId());
        assertEquals(1L, dto.getUserId());
        assertEquals("Test Message", dto.getMessage());
    }

    @Test
    void toEntity_Success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(400L);
        dto.setMessage("New Message");
        dto.setType(NotificationType.ALERTS);

        Notification notif = notificationMapper.toEntity(dto);

        assertNotNull(notif);
        assertEquals(dto.getId(), notif.getId());
        assertEquals("New Message", notif.getMessage());
        assertEquals(NotificationType.ALERTS, notif.getType());
    }
}
