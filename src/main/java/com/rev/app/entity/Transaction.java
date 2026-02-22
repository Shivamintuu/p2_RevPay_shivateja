package com.rev.app.entity;

import com.rev.app.enums.TransactionStatus;
import com.rev.app.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String note;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction status is required")
    private TransactionStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @NotNull(message = "Sender is required")
    private User sender;

    @ManyToOne
    @NotNull(message = "Receiver is required")
    private User receiver;
}
