package com.montbank.ms.transaction.services;

import com.montbank.ms.transaction.dtos.TransactionRequestDTO;
import com.montbank.ms.transaction.dtos.TransactionUpdateDTO;
import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.repositories.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    public List<TransactionModel> getTransactionsBySender(UUID sender){
        return transactionRepository.findAllBySender(sender);
    }
    @Transactional
    public void deleteTransactionHistory(UUID transactionId, UUID senderId){
        TransactionModel transaction = transactionRepository.findById(transactionId).orElseThrow(()
                -> new EntityNotFoundException("Transação não encontrada"));
        if(transaction.getSender().equals(senderId)){
            transactionRepository.deleteById(transactionId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transação não pertence ao usuário");
        }
    }
    @Transactional
    public void updateTransactionTitle(TransactionUpdateDTO transactionUpdateDTO, UUID userId, UUID transactionId){
        TransactionModel transaction = transactionRepository.findById(transactionId).orElseThrow(()
                -> new EntityNotFoundException("Transação não encontrada"));
        if(transaction.getSender().equals(userId)){
            transaction.setTitle(transactionUpdateDTO.title());
            transactionRepository.save(transaction);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transação não pertence ao usuário");
        }
    }
}
