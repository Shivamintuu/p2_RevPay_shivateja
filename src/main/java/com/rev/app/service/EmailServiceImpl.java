package com.rev.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    private void sendEmail(String to, String subject, String content) {
        String resendApiKey = System.getenv("RESEND_API_KEY");
        
        if (resendApiKey != null && !resendApiKey.trim().isEmpty()) {
            log.info("[EMAIL SERVICE] Attempting to send email via Resend HTTP API to {}", to);
            try {
                // Escape JSON string content
                String safeContent = content.replace("\\", "\\\\")
                                            .replace("\"", "\\\"")
                                            .replace("\n", "<br>");
                
                String json = "{"
                        + "\"from\":\"onboarding@resend.dev\","
                        + "\"to\":\"" + to + "\","
                        + "\"subject\":\"" + subject + "\","
                        + "\"html\":\"" + safeContent + "\""
                        + "}";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.resend.com/emails"))
                        .header("Authorization", "Bearer " + resendApiKey.trim())
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    log.info("[EMAIL SERVICE] Email sent successfully via Resend to {}", to);
                    return;
                } else {
                    log.error("[EMAIL SERVICE] Resend API returned error status {}: {}", response.statusCode(), response.body());
                }
            } catch (Exception e) {
                log.error("[EMAIL SERVICE] Error sending email via Resend HTTP API", e);
            }
            log.info("[EMAIL SERVICE] Falling back to SMTP...");
        }

        // Fallback to standard SMTP
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("shivamintu964@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            emailSender.send(message);
            log.info("[EMAIL SERVICE] Email sent successfully via SMTP to {}", to);
        } catch (Exception e) {
            log.error("[EMAIL SERVICE] Failed to send email via SMTP to {}", to, e);
        }
    }

    @Override
    @Async
    public void sendOtpEmail(String to, String otp) {
        log.info("[OTP SERVICE] Generated OTP for {}: {}", to, otp);
        String content = "Your OTP for RevPay login is: " + otp + "\n\nThis OTP is valid for 5 minutes.";
        sendEmail(to, "RevPay - Login OTP", content);
    }

    @Override
    @Async
    public void sendProfileUpdateNotification(String to, String changeDetails) {
        log.info("Sending profile update notification to {}", to);
        String content = "Hello,\n\nWe noticed a change in your RevPay account: " + changeDetails + "\n\nIf you did not make this change, please contact support immediately.";
        sendEmail(to, "RevPay - Security Alert: Profile Updated", content);
    }

    @Override
    @Async
    public void sendTransactionNotification(String to, String messageDetails) {
        log.info("Sending transaction notification to {}", to);
        String content = "Hello,\n\nWe wanted to alert you about a recent transaction on your RevPay account:\n\n" + messageDetails + "\n\nIf you did not make this transaction, please contact support immediately.";
        sendEmail(to, "RevPay - Transaction Alert", content);
    }
}
