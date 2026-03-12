package com.rev.app.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.rev.app.config.RazorpayConfig;
import com.rev.app.dto.PaymentVerificationRequest;
import com.rev.app.dto.RazorpayOrderRequest;
import com.rev.app.dto.RazorpayOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
@Slf4j
public class RazorpayService {

    private final RazorpayConfig config;
    private RazorpayClient razorpayClient;

    @Autowired
    public RazorpayService(RazorpayConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void init() {
        try {
            this.razorpayClient = new RazorpayClient(config.getKeyId(), config.getKeySecret());
            log.info("Razorpay Client initialized successfully");
        } catch (RazorpayException e) {
            log.error("Failed to initialize Razorpay Client", e);
        }
    }

    public RazorpayOrderResponse createOrder(RazorpayOrderRequest request) {
        try {
            // Amount in paise/cents (multiplying by 100)
            int amountInSmallestUnit = request.getAmount().multiply(new BigDecimal("100")).intValue();

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInSmallestUnit);
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequest);

            return RazorpayOrderResponse.builder()
                    .orderId(order.get("id"))
                    .amount(amountInSmallestUnit)
                    .currency(request.getCurrency())
                    .keyId(config.getKeyId())
                    .build();

        } catch (RazorpayException e) {
            log.error("Error creating Razorpay order", e);
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    public boolean verifySignature(PaymentVerificationRequest request) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpayOrderId());
            options.put("razorpay_payment_id", request.getRazorpayPaymentId());
            options.put("razorpay_signature", request.getRazorpaySignature());

            return Utils.verifyPaymentSignature(options, config.getKeySecret());
        } catch (RazorpayException e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }
}
