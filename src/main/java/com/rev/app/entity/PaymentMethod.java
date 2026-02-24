package com.rev.app.entity;

import com.rev.app.enums.PaymentMethodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Method type is required")
    private PaymentMethodType methodType;

    private boolean isDefault;

    // Bank Account fields (nullable for cards/wallets)
    private String accountNumber;
    private String bankName;
    private String ifscCode;

    // Card Details fields (nullable for banks/wallets)
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String billingAddress;

    @ManyToOne
    @NotNull(message = "User is required")
    private User user;
}
