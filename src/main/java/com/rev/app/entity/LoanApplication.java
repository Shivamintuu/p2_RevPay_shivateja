package com.rev.app.entity;

import com.rev.app.enums.LoanStatus;
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
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Tenure months are required")
    @Positive(message = "Tenure months must be positive")
    private Integer tenureMonths;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private LoanStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @NotNull(message = "Business profile is required")
    private BusinessProfile businessProfile;
}
