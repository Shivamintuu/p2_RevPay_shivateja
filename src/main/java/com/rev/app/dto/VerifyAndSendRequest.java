package com.rev.app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VerifyAndSendRequest extends PaymentVerificationRequest {
    private String recipientIdentifier;
    private String description;
    private String transactionPin;
}
