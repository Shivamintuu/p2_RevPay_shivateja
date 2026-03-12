package com.rev.app.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private Long userId;
    private BigDecimal amount;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
