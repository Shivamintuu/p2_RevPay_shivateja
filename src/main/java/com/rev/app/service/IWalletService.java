package com.rev.app.service;

import com.rev.app.dto.WalletDTO;
import java.math.BigDecimal;

public interface IWalletService {
    WalletDTO createWallet(Long userId);
    WalletDTO getWalletByUserId(Long userId);
    WalletDTO addFunds(Long userId, BigDecimal amount, Long paymentMethodId);
    WalletDTO withdrawFunds(Long userId, BigDecimal amount, Long paymentMethodId);
    boolean hasSufficientBalance(Long userId, BigDecimal amount);
}
