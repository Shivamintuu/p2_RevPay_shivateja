package com.rev.app.entity;

import com.rev.app.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be zero or positive")
    private BigDecimal totalAmount;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private InvoiceStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @NotNull(message = "Business profile is required")
    private BusinessProfile businessProfile;

    @ManyToOne
    @NotNull(message = "Customer is required")
    private Customer customer;
}
