package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.WalletDTO;
import com.rev.app.entity.User;
import com.rev.app.entity.Wallet;

class WalletMapperTest {

    private WalletMapper walletMapper;

    @BeforeEach
    void setUp() {
        walletMapper = new WalletMapper();
    }

    @Test
    void nntDTO_Success() {
        User user = new User();
        user.setId(5L);
        
        Wallet wallet = new Wallet();
        wallet.setId(10L);
        wallet.setBalance(new BigDecimal("100.50"));
        wallet.setUser(user);

        WalletDTO dto = walletMapper.toDTO(wallet);

        assertNotNull(dto);
        assertEquals(wallet.getId(), dto.getId());
        assertEquals(wallet.getBalance(), dto.getBalance());
        assertEquals(5L, dto.getUserId());
    }

    @Test
    void toEntity_Success() {
        WalletDTO dto = new WalletDTO();
        dto.setId(10L);
        dto.setBalance(new BigDecimal("200.75"));

        Wallet wallet = walletMapper.toEntity(dto);

        assertNotNull(wallet);
        assertEquals(dto.getId(), wallet.getId());
        assertEquals(dto.getBalance(), wallet.getBalance());
    }
}
