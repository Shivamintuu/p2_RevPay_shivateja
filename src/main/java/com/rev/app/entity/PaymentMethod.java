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

    @ManyToOne
    @NotNull(message = "User is required")
    private User user;
}
