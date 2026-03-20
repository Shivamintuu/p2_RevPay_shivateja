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

import com.rev.app.entity.Invoice;
import com.rev.app.entity.Invoice.InvoiceStatus;
import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;

@ExtendWith(MockitoExtension.class)
class IInvoiceRepositoryTest {

    @Mock
    private IInvoiceRepository invoiceRepository;

    @Test
    void findByBusinessUserId_ReturnsInvoices() {
        User businessUser = new User();
        businessUser.setId(1L);
        businessUser.setEmail("biz2@test.com");
        businessUser.setFullName("Biz User 2");
        businessUser.setPassword("pass");
        businessUser.setRole(Role.BUSINESS);

        Invoice inv = new Invoice();
        inv.setBusinessUser(businessUser);
        inv.setCustomerName("Customer");
        inv.setCustomerEmail("cust@t.com");
        inv.setStatus(InvoiceStatus.DRAFT);
        inv.setDueDate(java.time.LocalDate.now().plusDays(30));

        when(invoiceRepository.findByBusinessUserId(1L)).thenReturn(Collections.singletonList(inv));

        List<Invoice> found = invoiceRepository.findByBusinessUserId(1L);

        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
    }
}
