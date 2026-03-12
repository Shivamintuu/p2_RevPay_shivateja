package com.rev.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO implements Serializable {
    private Long id;
    private BigDecimal balance;
    private Long userId;
}
