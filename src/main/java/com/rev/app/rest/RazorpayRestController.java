package com.rev.app.rest;

import com.rev.app.dto.PaymentVerificationRequest;
import com.rev.app.dto.RazorpayOrderRequest;
import com.rev.app.dto.RazorpayOrderResponse;
import com.rev.app.dto.WalletDTO;
import com.rev.app.dto.VerifyAndAcceptRequest;
import com.rev.app.service.IMoneyRequestService;
import com.rev.app.service.ITransactionService;
import com.rev.app.service.IUserService;
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
    private final IUserService userService;
    private final ITransactionService transactionService;
    private final IMoneyRequestService moneyRequestService; // Added

    @Autowired
    public RazorpayRestController(RazorpayService razorpayService, IWalletService walletService, IUserService userService, ITransactionService transactionService, IMoneyRequestService moneyRequestService) {
        this.razorpayService = razorpayService;
        this.walletService = walletService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.moneyRequestService = moneyRequestService;
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
    @PostMapping("/verify-and-send")
    public ResponseEntity<?> verifyAndSendPayment(@RequestBody com.rev.app.dto.VerifyAndSendRequest request) {
        log.info("Verifying Razorpay payment signature for orderId: {} to send to recipient: {}", request.getRazorpayOrderId(), request.getRecipientIdentifier());
        
        boolean isValid = razorpayService.verifySignature(request);
        
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid payment signature"));
        }

        // Add funds to wallet first
        try {
            walletService.razorpayAddFunds(request.getUserId(), request.getAmount());
        } catch (Exception e) {
            log.error("Error adding funds after verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment verified but failed to add funds. Please contact support."));
        }

        // Attempt to send money using newly added funds
        try {
            com.rev.app.dto.UserDTO recipient = userService.getUserByIdentifier(request.getRecipientIdentifier());
            if (recipient == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "The recipient identifier is invalid. The funds have been safely added to your wallet balance."));
            }

            com.rev.app.dto.TransactionDTO transaction = transactionService.sendMoney(
                    request.getUserId(), 
                    recipient.getId(), 
                    request.getAmount(), 
                    request.getDescription(), 
                    request.getTransactionPin());
            
            return ResponseEntity.ok(Map.of(
                    "message", "Payment verified and funds successfully sent to " + request.getRecipientIdentifier(), 
                    "transaction", transaction));
        } catch (com.rev.app.exception.InvalidTransactionPinException | com.rev.app.exception.BadRequestException e) {
            log.error("Error sending funds after Razorpay payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage() + " The funds have been safely added to your wallet balance."));
        } catch (Exception e) {
            log.error("Unexpected error sending funds after Razorpay payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while sending the money. The funds have been safely added to your wallet balance."));
        }
    }

    @PostMapping("/verify-and-accept")
    public ResponseEntity<?> verifyAndAcceptRequest(@RequestBody VerifyAndAcceptRequest request) {
        log.info("Verifying Razorpay payment signature for orderId: {} to accept money request: {}", request.getRazorpayOrderId(), request.getMoneyRequestId());

        boolean isValid = razorpayService.verifySignature(request);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid payment signature"));
        }

        // Add funds to wallet first
        try {
            walletService.razorpayAddFunds(request.getUserId(), request.getAmount());
        } catch (Exception e) {
            log.error("Error adding funds after verification for money request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment verified but failed to add funds. Please contact support."));
        }

        // Attempt to accept request using newly added funds
        try {
            // Because Razorpay verification provides the funds, we can bypass PIN check by directly using the transaction service if needed,
            // or if the service requires a PIN, we can supply the user's PIN from frontend if they entered it, but usually standard razorpay payments bypass the PIN.
            // But since IMoneyRequestService requires a PIN, we will pass it from the request DTO.
            com.rev.app.dto.MoneyRequestDTO acceptedRequest = moneyRequestService.acceptRequest(
                    request.getMoneyRequestId(),
                    request.getUserId(),
                    request.getTransactionPin());

            return ResponseEntity.ok(Map.of(
                    "message", "Payment verified and request successfully accepted",
                    "moneyRequest", acceptedRequest));
        } catch (com.rev.app.exception.InvalidTransactionPinException | com.rev.app.exception.BadRequestException e) {
            log.error("Error accepting money request after Razorpay payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage() + " The funds have been safely added to your wallet balance."));
        } catch (Exception e) {
            log.error("Unexpected error accepting money request after Razorpay payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while accepting the request. The funds have been safely added to your wallet balance."));
        }
    }
}
