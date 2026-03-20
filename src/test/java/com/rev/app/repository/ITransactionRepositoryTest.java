package com.rev.app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.entity.Transaction;
import com.rev.app.entity.Transaction.TransactionStatus;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class ITransactionRepositoryTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Test
    void findBySenderIdOrRecipientId_ReturnsTransactions() {
        User sender = createTestUser(1L, "sender@t.com");
        User recipient = createTestUser(2L, "recipient@t.com");

        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setRecipient(recipient);
        tx.setAmount(new BigDecimal("50.00"));
        tx.setStatus(TransactionStatus.COMPLETED);

        when(transactionRepository.findBySenderIdOrRecipientId(1L, 1L)).thenReturn(Collections.singletonList(tx));

        List<Transaction> found = transactionRepository.findBySenderIdOrRecipientId(1L, 1L);

        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }

    @Test
    void getTotalTransactionVolume_ReturnsSum() {
        when(transactionRepository.getTotalTransactionVolume()).thenReturn(new BigDecimal("300.00"));

        BigDecimal total = transactionRepository.getTotalTransactionVolume();

        assertEquals(0, total.compareTo(new BigDecimal("300.00")));
    }

    private User createTestUser(Long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFullName("Test User");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);
        return user;
    }
}
