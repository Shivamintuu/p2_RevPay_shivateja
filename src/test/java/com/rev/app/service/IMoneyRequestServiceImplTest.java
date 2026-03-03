package com.rev.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.dto.MoneyRequestDTO;
import com.rev.app.entity.MoneyRequest;
import com.rev.app.entity.MoneyRequest.RequestStatus;
import com.rev.app.entity.User;
import com.rev.app.exception.BadRequestException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.mapper.MoneyRequestMapper;
import com.rev.app.repository.IMoneyRequestRepository;
import com.rev.app.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
class IMoneyRequestServiceImplTest {

    @Mock
    private IMoneyRequestRepository moneyRequestRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITransactionService transactionService;

    @Mock
    private MoneyRequestMapper moneyRequestMapper;

    @InjectMocks
    private IMoneyRequestServiceImpl moneyRequestService;

    private User requester;
    private User requestee;
    private MoneyRequest request;
    private MoneyRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requester = new User();
        requester.setId(10L);

        requestee = new User();
        requestee.setId(11L);

        request = new MoneyRequest();
        request.setId(30L);
        request.setRequester(requester);
        request.setRequestee(requestee);
        request.setAmount(new BigDecimal("50.00"));
        request.setStatus(RequestStatus.PENDING);

        requestDTO = new MoneyRequestDTO();
        requestDTO.setId(30L);
    }

    @Test
    void sendRequest_Success() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(requester));
        when(userRepository.findById(11L)).thenReturn(Optional.of(requestee));
        when(moneyRequestRepository.save(any(MoneyRequest.class))).thenReturn(request);
        when(moneyRequestMapper.toDTO(any(MoneyRequest.class))).thenReturn(requestDTO);

        MoneyRequestDTO result = moneyRequestService.sendRequest(10L, 11L, new BigDecimal("50.00"), "Lunch");

        assertNotNull(result);
        verify(moneyRequestRepository).save(any(MoneyRequest.class));
    }

    @Test
    void sendRequest_SelfRequest_ThrowsException() {
        assertThrows(BadRequestException.class, () -> moneyRequestService.sendRequest(10L, 10L, new BigDecimal("50.00"), "Lunch"));
    }

    @Test
    void acceptRequest_Success() {
        when(moneyRequestRepository.findById(30L)).thenReturn(Optional.of(request));
        when(transactionService.sendMoney(eq(11L), eq(10L), any(BigDecimal.class), anyString(), anyString())).thenReturn(null);
        when(moneyRequestRepository.save(any(MoneyRequest.class))).thenReturn(request);
        when(moneyRequestMapper.toDTO(request)).thenReturn(requestDTO);

        MoneyRequestDTO result = moneyRequestService.acceptRequest(30L, 11L, "1234");

        assertNotNull(result);
        assertEquals(RequestStatus.ACCEPTED, request.getStatus());
        verify(transactionService).sendMoney(anyLong(), anyLong(), any(), anyString(), anyString());
    }

    @Test
    void declineRequest_Success() {
        when(moneyRequestRepository.findById(30L)).thenReturn(Optional.of(request));
        when(moneyRequestRepository.save(any(MoneyRequest.class))).thenReturn(request);
        when(moneyRequestMapper.toDTO(request)).thenReturn(requestDTO);

        MoneyRequestDTO result = moneyRequestService.declineRequest(30L, 11L);

        assertNotNull(result);
        assertEquals(RequestStatus.DECLINED, request.getStatus());
    }
}
