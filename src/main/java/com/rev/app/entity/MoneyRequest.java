package com.rev.app.entity;

import com.rev.app.enums.MoneyRequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class MoneyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private MoneyRequestStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @NotNull(message = "Requester is required")
    private User requester;

    @ManyToOne
    @NotNull(message = "Receiver is required")
    private User receiver;
}
