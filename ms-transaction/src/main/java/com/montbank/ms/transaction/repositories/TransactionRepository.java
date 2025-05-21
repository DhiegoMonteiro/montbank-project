package com.montbank.ms.transaction.repositories;

import com.montbank.ms.transaction.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {
    List<TransactionModel> findAllBySender(UUID sender);
    List<TransactionModel> findAllByReceiver(String receiver);
}
