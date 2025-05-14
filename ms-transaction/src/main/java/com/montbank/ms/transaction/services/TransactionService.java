package com.montbank.ms.transaction.services;

import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public TransactionModel sendTransaction(TransactionModel transactionModel){

        return transactionRepository.save(transactionModel);
    }
}
