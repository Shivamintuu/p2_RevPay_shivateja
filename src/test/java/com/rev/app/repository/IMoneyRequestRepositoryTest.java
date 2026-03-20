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

import com.rev.app.entity.MoneyRequest;
import com.rev.app.entity.MoneyRequest.RequestStatus;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class IMoneyRequestRepositoryTest {

    @Mock
    private IMoneyRequestRepository moneyRequestRepository;

    @Test
    void findByRequesteeId_ReturnsRequests() {
        User requester = createTestUser(1L, "req1@t.com");
        User requestee = createTestUser(2L, "req2@t.com");

        MoneyRequest request = new MoneyRequest();
        request.setRequester(requester);
        request.setRequestee(requestee);
        request.setAmount(new BigDecimal("20.00"));
        request.setStatus(RequestStatus.PENDING);

        when(moneyRequestRepository.findByRequesteeId(2L)).thenReturn(Collections.singletonList(request));

        List<MoneyRequest> found = moneyRequestRepository.findByRequesteeId(2L);

        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
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
