package com.montbank.ms.transaction.controllers;

import com.montbank.ms.transaction.dtos.TransactionRequestDTO;
import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.services.MessageSenderService;
import com.montbank.ms.transaction.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    MessageSenderService messageSenderService;


    @PostMapping("/new")
    public ResponseEntity<TransactionModel> sendTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO,
                                                            @RequestAttribute String userId) {
            messageSenderService.sendUserValidationRequest(UUID.fromString(userId));
            messageSenderService.sendUserValidationRequest(transactionRequestDTO.receiver());
            var transaction = new TransactionModel();
            BeanUtils.copyProperties(transactionRequestDTO,transaction);
            TransactionModel savedTransaction = transactionService.save(transaction,UUID.fromString(userId));
            messageSenderService.sendProcessedTransactionEvent(savedTransaction.getSender(),
                    savedTransaction.getAmount(),
                    savedTransaction.getReceiver());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedTransaction);
    }
}
