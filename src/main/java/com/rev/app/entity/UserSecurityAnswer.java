package com.rev.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserSecurityAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Answer is required")
    private String answer;

    @ManyToOne
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne
    @NotNull(message = "Security question is required")
    private SecurityQuestion question;
}
