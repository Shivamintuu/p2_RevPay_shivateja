package com.rev.app.repository;

import com.rev.app.entity.PersonalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalUserRepository extends JpaRepository<PersonalUser, Long> {
}
