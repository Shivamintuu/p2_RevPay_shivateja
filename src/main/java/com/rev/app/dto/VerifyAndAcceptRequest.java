package com.rev.app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VerifyAndAcceptRequest extends PaymentVerificationRequest {
    private Long moneyRequestId;
    private String transactionPin;
}
