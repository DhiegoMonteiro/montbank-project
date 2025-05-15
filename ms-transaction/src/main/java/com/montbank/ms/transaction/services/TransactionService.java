package com.montbank.ms.transaction.services;

import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.repositories.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public TransactionModel sendTransaction(TransactionModel transactionModel, UUID senderUUID){
        transactionModel.setSender(senderUUID);
        return transactionRepository.save(transactionModel);
    }
}
