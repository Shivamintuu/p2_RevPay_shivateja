package com.rev.app.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void sendOtpEmail_Success() {
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        emailService.sendOtpEmail("test@example.com", "123456");

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendProfileUpdateNotification_Success() {
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        emailService.sendProfileUpdateNotification("test@example.com", "Password changed");

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendTransactionNotification_Success() {
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        emailService.sendTransactionNotification("test@example.com", "Sent $100");

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
