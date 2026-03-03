package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.TransactionDTO;
import com.rev.app.entity.Transaction;
import com.rev.app.entity.Transaction.TransactionStatus;
import com.rev.app.entity.Transaction.TransactionType;
import com.rev.app.entity.User;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapper();
    }

    @Test
    void toDTO_Success() {
        User sender = new User();
        sender.setId(1L);
        sender.setFullName("Sender Name");

        User recipient = new User();
        recipient.setId(2L);
        recipient.setFullName("Recipient Name");

        Transaction tx = new Transaction();
        tx.setId(100L);
        tx.setAmount(new BigDecimal("50.00"));
        tx.setType(TransactionType.SEND);
        tx.setStatus(TransactionStatus.COMPLETED);
        tx.setTimestamp(LocalDateTime.now());
        tx.setSender(sender);
        tx.setRecipient(recipient);

        TransactionDTO dto = transactionMapper.toDTO(tx);

        assertNotNull(dto);
        assertEquals(tx.getId(), dto.getId());
        assertEquals(1L, dto.getSenderId());
        assertEquals("Sender Name", dto.getSenderName());
        assertEquals(2L, dto.getRecipientId());
        assertEquals("Recipient Name", dto.getRecipientName());
    }

    @Test
    void toEntity_Success() {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(100L);
        dto.setAmount(new BigDecimal("75.00"));
        dto.setType(TransactionType.WITHDRAW);
        dto.setStatus(TransactionStatus.PENDING);

        Transaction tx = transactionMapper.toEntity(dto);

        assertNotNull(tx);
        assertEquals(dto.getId(), tx.getId());
        assertEquals(dto.getAmount(), tx.getAmount());
        assertEquals(TransactionType.WITHDRAW, tx.getType());
    }
}
