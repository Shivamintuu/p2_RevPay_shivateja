package com.rev.app.repository;

import com.rev.app.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByBusinessUserId(Long businessUserId);
    List<Invoice> findByPersonalUserId(Long personalUserId);
}
