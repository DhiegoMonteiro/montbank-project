package com.montbank.ms.transaction.services;

import com.montbank.ms.transaction.dtos.TransactionRequestDTO;
import com.montbank.ms.transaction.dtos.TransactionUpdateDTO;
import com.montbank.ms.transaction.exceptions.BusinessException;
import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.repositories.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MessageSenderService messageSenderService;

    @Transactional
    public TransactionModel save(TransactionRequestDTO transactionRequestDTO, UUID senderId, String senderName, String senderEmail){
        if (transactionRequestDTO.title() == null ||
                transactionRequestDTO.title().length() < 5 ||
                transactionRequestDTO.title().length() > 25) {
            throw new BusinessException("O título da transação deve ter entre 5 e 25 caracteres.");
        }
        if (transactionRequestDTO.amount() == null || transactionRequestDTO.amount().compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("O valor da transação não pode ser zero.");
        }
        Boolean senderExists = messageSenderService.sendUserValidationRequest(senderId);
        if (senderExists == null) {
            throw new BusinessException("Timeout na validação do usuário remetente");
        }
        if (!senderExists) {
            throw new BusinessException("Usuário remetente não encontrado");
        }
        Boolean receiverExists = messageSenderService.sendUserValidationEmailRequest(transactionRequestDTO.receiver());
        if (receiverExists == null) {
            throw new BusinessException("Timeout na validação do usuário receptor");
        }
        if (!receiverExists) {
            throw new BusinessException("Usuário receptor não encontrado");
        }
        Boolean hasBalance = messageSenderService.sendUserValidationBalanceQueue(senderId, transactionRequestDTO.amount());
        if (hasBalance == null) {
            throw new BusinessException("Erro ou timeout ao validar o saldo do usuário remetente.");
        }
        if (!hasBalance) {
            throw new BusinessException("Saldo insuficiente para realizar a transação.");
        }
        var transactionModel = new TransactionModel();
        BeanUtils.copyProperties(transactionRequestDTO, transactionModel);
        transactionModel.setSender(senderId);
        transactionModel.setSenderName(senderName);
        transactionModel.setSenderEmail(senderEmail);
        TransactionModel savedTransaction = transactionRepository.save(transactionModel);
        messageSenderService.sendProcessedTransactionEvent(savedTransaction.getSender(),
                                                             savedTransaction.getAmount(),
                                                                savedTransaction.getReceiver());
        return savedTransaction;
    }
    public List<TransactionModel> getAllUserTransactions(UUID userId, String userEmail){
        if (userId == null) {
            throw new BusinessException("Usuário não autenticado");
        }
        List<TransactionModel> allUserSendTransactions = transactionRepository.findAllBySender(userId);
        List<TransactionModel> allUserTransactions = transactionRepository.findAllByReceiver(userEmail);
        allUserTransactions.addAll(allUserSendTransactions);
        return allUserTransactions;
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
        if (transactionUpdateDTO.title() == null ||
                transactionUpdateDTO.title().length() < 5 ||
                transactionUpdateDTO.title().length() > 25) {
            throw new BusinessException("O título da transação deve ter entre 5 e 25 caracteres.");
        }
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
