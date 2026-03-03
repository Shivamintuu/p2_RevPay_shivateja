package com.rev.app.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rev.app.dto.InvoiceItemDTO;
import com.rev.app.entity.Invoice;
import com.rev.app.entity.InvoiceItem;

class InvoiceItemMapperTest {

    private InvoiceItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new InvoiceItemMapper();
    }

    @Test
    void toDTO_Success() {
        Invoice inv = new Invoice();
        inv.setId(700L);

        InvoiceItem item = new InvoiceItem();
        item.setId(800L);
        item.setDescription("Service");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("50.00"));
        item.setInvoice(inv);

        InvoiceItemDTO dto = itemMapper.toDTO(item);

        assertNotNull(dto);
        assertEquals(800L, dto.getId());
        assertEquals(700L, dto.getInvoiceId());
    }

    @Test
    void toEntity_Success() {
        InvoiceItemDTO dto = new InvoiceItemDTO();
        dto.setId(800L);
        dto.setDescription("Product");
        dto.setQuantity(5);

        InvoiceItem item = itemMapper.toEntity(dto);

        assertNotNull(item);
        assertEquals(dto.getId(), item.getId());
        assertEquals("Product", item.getDescription());
    }
}
