package com.example.accountmicroservice.repository;

import com.example.accountmicroservice.domain.Account;
import com.example.accountmicroservice.domain.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
}
