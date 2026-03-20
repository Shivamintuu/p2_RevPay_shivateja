package com.rev.app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;
import com.rev.app.entity.Wallet;

@ExtendWith(MockitoExtension.class)
class IWalletRepositoryTest {

    @Mock
    private IWalletRepository walletRepository;

    @Test
    void findByUserId_ReturnsWallet() {
        User user = new User();
        user.setId(1L);
        user.setEmail("wallet@test.com");
        user.setFullName("Wallet User");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);

        Wallet wallet = new Wallet(user);
        wallet.setBalance(new BigDecimal("100.00"));

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        Optional<Wallet> found = walletRepository.findByUserId(1L);

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUser().getId());
    }
}
