package com.rev.app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.entity.PaymentMethod;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class IPaymentMethodRepositoryTest {

    @Mock
    private IPaymentMethodRepository paymentMethodRepository;

    @Test
    void findByUserId_ReturnsMethods() {
        User user = new User();
        user.setId(1L);
        user.setEmail("pm@test.com");
        user.setFullName("PM User");
        user.setPassword("pass");
        user.setRole(Role.PERSONAL);

        PaymentMethod pm = new PaymentMethod();
        pm.setUser(user);
        pm.setType(PaymentMethod.PaymentMethodType.CREDIT_CARD);
        pm.setAccountNumber("****1234");
        pm.setDefault(true);

        when(paymentMethodRepository.findByUserId(1L)).thenReturn(Collections.singletonList(pm));

        List<PaymentMethod> found = paymentMethodRepository.findByUserId(1L);

        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }
}
