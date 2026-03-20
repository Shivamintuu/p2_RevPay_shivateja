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

import com.rev.app.entity.LoanApplication;
import com.rev.app.entity.LoanApplication.LoanStatus;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class ILoanApplicationRepositoryTest {

    @Mock
    private ILoanApplicationRepository loanRepository;

    @Test
    void findByBusinessUserId_ReturnsLoans() {
        User businessUser = new User();
        businessUser.setId(1L);
        businessUser.setEmail("biz@test.com");
        businessUser.setFullName("Biz User");
        businessUser.setPassword("pass");
        businessUser.setRole(Role.BUSINESS);

        LoanApplication loan = new LoanApplication();
        loan.setBusinessUser(businessUser);
        loan.setAmount(new BigDecimal("5000.00"));
        loan.setTenureMonths(12);
        loan.setStatus(LoanStatus.PENDING);
        loan.setAppliedAt(java.time.LocalDateTime.now());

        when(loanRepository.findByBusinessUserId(1L)).thenReturn(Collections.singletonList(loan));

        List<LoanApplication> found = loanRepository.findByBusinessUserId(1L);

        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }
}
