package com.rev.app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.entity.Notification;
import com.rev.app.entity.Notification.NotificationType;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class INotificationRepositoryTest {

    @Mock
    private INotificationRepository notificationRepository;

    @Test
    void findByUserId_ReturnsNotifications() {
        User user = new User();
        user.setId(1L);
        user.setEmail("notif@test.com");
        user.setFullName("Notif User");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);

        Notification notif = new Notification();
        notif.setUser(user);
        notif.setMessage("Hello");
        notif.setType(NotificationType.TRANSACTION);
        notif.setRead(false);

        when(notificationRepository.findByUserId(1L)).thenReturn(Collections.singletonList(notif));

        List<Notification> found = notificationRepository.findByUserId(1L);

        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }
}
