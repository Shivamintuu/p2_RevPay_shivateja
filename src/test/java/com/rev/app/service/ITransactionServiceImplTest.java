package com.rev.app.service;

import com.rev.app.dto.TransactionDTO;
import com.rev.app.entity.Transaction;
import com.rev.app.entity.Transaction.TransactionType;
import com.rev.app.entity.User;
import com.rev.app.entity.Wallet;
import com.rev.app.exception.InsufficientFundsException;
import com.rev.app.exception.InvalidCredentialsException;
import com.rev.app.mapper.TransactionMapper;
import com.rev.app.repository.ITransactionRepository;
import com.rev.app.repository.IUserRepository;
import com.rev.app.repository.IWalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.rev.app.service.IEmailService;
import com.rev.app.service.INotificationService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ITransactionServiceImplTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IWalletRepository walletRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IEmailService emailService;

    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private ITransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMoney_Success() {
        User sender = new User();
        sender.setId(1L);
        sender.setTransactionPin("encodedPin");

        User recipient = new User();
        recipient.setId(2L);

        Wallet senderWallet = new Wallet(sender);
        senderWallet.setBalance(new BigDecimal("100.0"));

        Wallet recipientWallet = new Wallet(recipient);
        recipientWallet.setBalance(new BigDecimal("50.0"));

        Transaction tx = new Transaction();
        tx.setId(10L);
        tx.setType(TransactionType.SEND);

        TransactionDTO dto = new TransactionDTO();
        dto.setId(10L);
        dto.setType(TransactionType.SEND);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(recipient));
        when(passwordEncoder.matches("123456", "encodedPin")).thenReturn(true);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(recipientWallet));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);
        when(transactionMapper.toDTO(any(Transaction.class))).thenReturn(dto);

        TransactionDTO result = transactionService.sendMoney(1L, 2L, new BigDecimal("40.0"), "Test Note", "123456");

        assertNotNull(result);
        assertEquals(new BigDecimal("60.0"), senderWallet.getBalance());
        assertEquals(new BigDecimal("90.0"), recipientWallet.getBalance());
        verify(walletRepository, times(2)).save(any(Wallet.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testSendMoney_InvalidPin() {
        User sender = new User();
        sender.setId(1L);
        sender.setTransactionPin("encodedPin");
        User recipient = new User();
        recipient.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(recipient));
        when(passwordEncoder.matches("wrongPin", "encodedPin")).thenReturn(false);

        assertThrows(com.rev.app.exception.InvalidTransactionPinException.class, () -> 
            transactionService.sendMoney(1L, 2L, new BigDecimal("40.0"), "Test Note", "wrongPin"));
    }

    @Test
    void testSendMoney_InsufficientFunds() {
        User sender = new User();
        sender.setId(1L);
        sender.setTransactionPin("encodedPin");
        User recipient = new User();
        recipient.setId(2L);
        
        Wallet senderWallet = new Wallet(sender);
        senderWallet.setBalance(new BigDecimal("10.0")); // less than 40.0
        Wallet recipientWallet = new Wallet(recipient);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(recipient));
        when(passwordEncoder.matches("123456", "encodedPin")).thenReturn(true);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserId(2L)).thenReturn(Optional.of(recipientWallet));

        assertThrows(InsufficientFundsException.class, () -> 
            transactionService.sendMoney(1L, 2L, new BigDecimal("40.0"), "Test Note", "123456"));
            
        verify(transactionRepository, times(1)).saveAndFlush(any(Transaction.class)); // Verifies failed tx is saved
        assertEquals(new BigDecimal("10.0"), senderWallet.getBalance()); // Balance unchanged
    }
}
