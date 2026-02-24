package com.rev.app.repository;

import com.rev.app.entity.MoneyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyRequestRepository extends JpaRepository<MoneyRequest, Long> {
    List<MoneyRequest> findByRequesterIdOrReceiverId(Long requesterId, Long receiverId);
}
