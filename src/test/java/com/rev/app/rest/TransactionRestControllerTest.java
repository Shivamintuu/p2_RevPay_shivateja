package com.rev.app.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rev.app.dto.TransactionDTO;
import com.rev.app.dto.UserDTO;
import com.rev.app.service.ITransactionService;
import com.rev.app.service.IUserService;

class TransactionRestControllerTest {

    private ITransactionService transactionService;
    private IUserService userService;
    private TransactionRestController transactionRestController;

    @BeforeEach
    void setUp() {
        transactionService = mock(ITransactionService.class);
        userService = mock(IUserService.class);
        transactionRestController = new TransactionRestController(transactionService, userService);
    }

    @Test
    void sendMoney_Success() {
        TransactionRestController.SendMoneyRequest request = new TransactionRestController.SendMoneyRequest();
        request.setSenderId(1L);
        request.setRecipientIdentifier("test@test.com");
        request.setAmount(new BigDecimal("50.00"));
        request.setTransactionPin("1234");

        UserDTO recipient = new UserDTO();
        recipient.setId(2L);

        TransactionDTO txDTO = new TransactionDTO();
        txDTO.setId(100L);

        when(userService.getUserByIdentifier(anyString())).thenReturn(recipient);
        when(transactionService.sendMoney(anyLong(), anyLong(), any(), any(), any())).thenReturn(txDTO);

        ResponseEntity<TransactionDTO> response = transactionRestController.sendMoney(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100L, response.getBody().getId());
    }

    @Test
    void getTransactionsByUser_Success() {
        when(transactionService.getTransactionsByUserId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<TransactionDTO>> response = transactionRestController.getTransactionsByUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
}
