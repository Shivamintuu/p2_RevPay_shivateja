package com.rev.app.service;

import com.rev.app.dto.WalletDTO;
import com.rev.app.entity.User;
import com.rev.app.entity.Wallet;
import com.rev.app.exception.InsufficientFundsException;
import com.rev.app.mapper.WalletMapper;
import com.rev.app.repository.IUserRepository;
import com.rev.app.repository.IWalletRepository;
import com.rev.app.repository.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IWalletServiceImplTest {

    @Mock
    private IWalletRepository walletRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IEmailService emailService;

    @InjectMocks
    private IWalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWallet() {
        User user = new User();
        user.setId(1L);

        Wallet wallet = new Wallet(user);
        wallet.setBalance(BigDecimal.ZERO);

        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setId(1L);
        walletDTO.setBalance(BigDecimal.ZERO);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletMapper.toDTO(any(Wallet.class))).thenReturn(walletDTO);

        WalletDTO result = walletService.createWallet(1L);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testWithdrawFunds_InsufficientBalance() {
        Wallet wallet = new Wallet(new User());
        wallet.setBalance(new BigDecimal("50.00"));

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientFundsException.class, () -> 
            walletService.withdrawFunds(1L, new BigDecimal("100.00"), 2L));
        
        verify(walletRepository, never()).save(any(Wallet.class));
    }
}
