package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rev.app.dto.InvoiceDTO;
import com.rev.app.dto.InvoiceItemDTO;
import com.rev.app.entity.Invoice;
import com.rev.app.entity.InvoiceItem;
import com.rev.app.entity.User;

@ExtendWith(MockitoExtension.class)
class InvoiceMapperTest {

    @Mock
    private InvoiceItemMapper invoiceItemMapper;

    @InjectMocks
    private InvoiceMapper invoiceMapper;

    @Test
    void toDTO_Success() {
        User bizUser = new User();
        bizUser.setId(3L);
        bizUser.setBusinessName("Tech Corp");

        Invoice inv = new Invoice();
        inv.setId(700L);
        inv.setBusinessUser(bizUser);
        inv.setItems(Collections.singletonList(new InvoiceItem()));

        when(invoiceItemMapper.toDTO(any())).thenReturn(new InvoiceItemDTO());

        InvoiceDTO dto = invoiceMapper.toDTO(inv);

        assertNotNull(dto);
        assertEquals(700L, dto.getId());
        assertEquals(3L, dto.getBusinessUserId());
        assertFalse(dto.getItems().isEmpty());
    }

    @Test
    void toEntity_Success() {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(700L);
        dto.setItems(Collections.singletonList(new InvoiceItemDTO()));

        when(invoiceItemMapper.toEntity(any())).thenReturn(new InvoiceItem());

        Invoice inv = invoiceMapper.toEntity(dto);

        assertNotNull(inv);
        assertEquals(700L, inv.getId());
        assertFalse(inv.getItems().isEmpty());
    }
}
