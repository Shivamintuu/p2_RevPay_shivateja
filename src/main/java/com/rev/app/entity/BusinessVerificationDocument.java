package com.rev.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class BusinessVerificationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "Document path is required")
    private String documentPath;

    @ManyToOne
    @NotNull(message = "Business profile is required")
    private BusinessProfile businessProfile;
}
