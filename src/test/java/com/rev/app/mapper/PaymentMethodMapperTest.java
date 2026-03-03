package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.PaymentMethodDTO;
import com.rev.app.entity.PaymentMethod;
import com.rev.app.entity.User;

class PaymentMethodMapperTest {

    private PaymentMethodMapper paymentMethodMapper;

    @BeforeEach
    void setUp() {
        paymentMethodMapper = new PaymentMethodMapper();
    }

    @Test
    void toDTO_Success() {
        User user = new User();
        user.setId(1L);

        PaymentMethod pm = new PaymentMethod();
        pm.setId(600L);
        pm.setType(PaymentMethod.PaymentMethodType.CREDIT_CARD);
        pm.setAccountNumber("1234567890123456");
        pm.setExpiryDate("12/25");
        pm.setDefault(true);
        pm.setUser(user);

        PaymentMethodDTO dto = paymentMethodMapper.toDTO(pm);

        assertNotNull(dto);
        assertEquals(pm.getId(), dto.getId());
        assertEquals("****3456", dto.getAccountNumberMasked());
        assertTrue(dto.isDefault());
    }

    @Test
    void toEntity_Success() {
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setId(600L);
        dto.setType(PaymentMethod.PaymentMethodType.BANK_ACCOUNT);
        dto.setDefault(false);

        PaymentMethod pm = paymentMethodMapper.toEntity(dto);

        assertNotNull(pm);
        assertEquals(dto.getId(), pm.getId());
        assertEquals(PaymentMethod.PaymentMethodType.BANK_ACCOUNT, pm.getType());
        assertFalse(pm.isDefault());
    }
}
