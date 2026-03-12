package com.rev.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RazorpayOrderResponse {
    private String orderId;
    private int amount; // Amount in smallest currency unit (e.g., cents/paise)
    private String currency;
    private String keyId;
}
