package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.LoanApplicationDTO;
import com.rev.app.entity.LoanApplication;
import com.rev.app.entity.LoanApplication.LoanStatus;
import com.rev.app.entity.User;

class LoanApplicationMapperTest {

    private LoanApplicationMapper loanMapper;

    @BeforeEach
    void setUp() {
        loanMapper = new LoanApplicationMapper();
    }

    @Test
    void toDTO_Success() {
        User bizUser = new User();
        bizUser.setId(3L);
        bizUser.setBusinessName("Tech Corp");

        LoanApplication loan = new LoanApplication();
        loan.setId(500L);
        loan.setAmount(new BigDecimal("10000.00"));
        loan.setStatus(LoanStatus.PENDING);
        loan.setBusinessUser(bizUser);
        loan.setAppliedAt(LocalDateTime.now());

        LoanApplicationDTO dto = loanMapper.toDTO(loan);

        assertNotNull(dto);
        assertEquals(loan.getId(), dto.getId());
        assertEquals(3L, dto.getBusinessUserId());
        assertEquals("Tech Corp", dto.getBusinessUserName());
    }

    @Test
    void toEntity_Success() {
        LoanApplicationDTO dto = new LoanApplicationDTO();
        dto.setId(500L);
        dto.setAmount(new BigDecimal("15000.00"));
        dto.setStatus(LoanStatus.APPROVED);

        LoanApplication loan = loanMapper.toEntity(dto);

        assertNotNull(loan);
        assertEquals(dto.getId(), loan.getId());
        assertEquals(dto.getAmount(), loan.getAmount());
        assertEquals(LoanStatus.APPROVED, loan.getStatus());
    }
}
