package com.montbank.ms.user.services;

import com.montbank.ms.user.models.TransactionModel;
import com.montbank.ms.user.repositories.TransactionRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserService userService;

    @Transactional
    public TransactionModel sendTransaction(TransactionModel transactionModel){
        userService.updateBalance(transactionModel.getSender(),
                                    transactionModel.getReceiver(),
                                    transactionModel.getAmount());
        return transactionRepository.save(transactionModel);
    }
}
