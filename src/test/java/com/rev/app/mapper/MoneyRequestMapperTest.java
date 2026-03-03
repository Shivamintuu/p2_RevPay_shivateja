package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.MoneyRequestDTO;
import com.rev.app.entity.MoneyRequest;
import com.rev.app.entity.MoneyRequest.RequestStatus;
import com.rev.app.entity.User;

class MoneyRequestMapperTest {

    private MoneyRequestMapper moneyRequestMapper;

    @BeforeEach
    void setUp() {
        moneyRequestMapper = new MoneyRequestMapper();
    }

    @Test
    void toDTO_Success() {
        User requester = new User();
        requester.setId(10L);
        requester.setFullName("Requester Name");

        User requestee = new User();
        requestee.setId(11L);
        requestee.setFullName("Requestee Name");

        MoneyRequest request = new MoneyRequest();
        request.setId(300L);
        request.setAmount(new BigDecimal("50.00"));
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setRequester(requester);
        request.setRequestee(requestee);

        MoneyRequestDTO dto = moneyRequestMapper.toDTO(request);

        assertNotNull(dto);
        assertEquals(request.getId(), dto.getId());
        assertEquals(10L, dto.getRequesterId());
        assertEquals("Requester Name", dto.getRequesterName());
        assertEquals(11L, dto.getRequesteeId());
        assertEquals("Requestee Name", dto.getRequesteeName());
    }

    @Test
    void toEntity_Success() {
        MoneyRequestDTO dto = new MoneyRequestDTO();
        dto.setId(300L);
        dto.setAmount(new BigDecimal("100.00"));
        dto.setStatus(RequestStatus.ACCEPTED);

        MoneyRequest request = moneyRequestMapper.toEntity(dto);

        assertNotNull(request);
        assertEquals(dto.getId(), request.getId());
        assertEquals(dto.getAmount(), request.getAmount());
        assertEquals(RequestStatus.ACCEPTED, request.getStatus());
    }
}
