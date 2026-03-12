package com.rev.app.rest;

import com.rev.app.dto.PaymentVerificationRequest;
import com.rev.app.dto.RazorpayOrderRequest;
import com.rev.app.dto.RazorpayOrderResponse;
import com.rev.app.dto.WalletDTO;
import com.rev.app.service.IWalletService;
import com.rev.app.service.RazorpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class RazorpayRestController {

    private final RazorpayService razorpayService;
    private final IWalletService walletService;

    @Autowired
    public RazorpayRestController(RazorpayService razorpayService, IWalletService walletService) {
        this.razorpayService = razorpayService;
        this.walletService = walletService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<RazorpayOrderResponse> createOrder(@RequestBody RazorpayOrderRequest request) {
        log.info("Creating Razorpay order for amount: {}", request.getAmount());
        return ResponseEntity.ok(razorpayService.createOrder(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        log.info("Verifying Razorpay payment signature for orderId: {}", request.getRazorpayOrderId());
        
        boolean isValid = razorpayService.verifySignature(request);
        
        if (isValid) {
            // Add funds to wallet
            try {
                WalletDTO updatedWallet = walletService.razorpayAddFunds(request.getUserId(), request.getAmount());
                return ResponseEntity.ok(Map.of("message", "Payment verified and funds added successfully", "wallet", updatedWallet));
            } catch (Exception e) {
                log.error("Error adding funds after verification", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Payment verified but failed to add funds. Please contact support."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid payment signature"));
        }
    }
}
