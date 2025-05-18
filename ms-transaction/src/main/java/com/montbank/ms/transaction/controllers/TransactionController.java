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

    @PostMapping("/new")
    public ResponseEntity<TransactionModel> sendTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO,
                                                            @RequestAttribute String userId) {
            TransactionModel savedTransaction = transactionService.save(transactionRequestDTO,UUID.fromString(userId));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedTransaction);
    }
}
