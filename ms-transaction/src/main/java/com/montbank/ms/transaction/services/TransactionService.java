package com.montbank.ms.transaction.services;

import com.montbank.ms.transaction.dtos.TransactionRequestDTO;
import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.repositories.TransactionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MessageSenderService messageSenderService;

    @Transactional
    public TransactionModel save(TransactionRequestDTO transactionRequestDTO, UUID senderId){
        messageSenderService.sendUserValidationRequest(senderId);
        messageSenderService.sendUserValidationRequest(transactionRequestDTO.receiver());
        var transactionModel = new TransactionModel();
        BeanUtils.copyProperties(transactionRequestDTO, transactionModel);
        transactionModel.setSender(senderId);
        TransactionModel savedTransaction = transactionRepository.save(transactionModel);
        messageSenderService.sendProcessedTransactionEvent(savedTransaction.getSender(),
                                                             savedTransaction.getAmount(),
                                                                savedTransaction.getReceiver());
        return savedTransaction;
    }
}
