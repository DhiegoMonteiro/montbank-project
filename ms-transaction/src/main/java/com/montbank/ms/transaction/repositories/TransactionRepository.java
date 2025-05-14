package com.montbank.ms.transaction.repositories;

import com.montbank.ms.transaction.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {
}
