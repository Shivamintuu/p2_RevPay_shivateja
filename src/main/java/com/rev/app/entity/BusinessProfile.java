package com.rev.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class BusinessProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business type is required")
    private String businessType;

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Verification status is required")
    private String verificationStatus;

    @OneToOne
    @NotNull(message = "User is required")
    private User user;
}
