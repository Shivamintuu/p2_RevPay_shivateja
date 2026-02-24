package com.rev.app.repository;

import com.rev.app.entity.BusinessUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUserRepository extends JpaRepository<BusinessUser, Long> {
}
