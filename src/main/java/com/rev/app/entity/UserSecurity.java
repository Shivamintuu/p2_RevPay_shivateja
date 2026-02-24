package com.rev.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class UserSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pin hash is required")
    private String pinHash;

    @NotBlank(message = "Security question is required")
    private String securityQuestion;

    @NotBlank(message = "Security answer is required")
    private String securityAnswer;

    private LocalDateTime updatedAt;

    @OneToOne
    @NotNull(message = "User is required")
    private User user;
}
