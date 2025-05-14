package com.montbank.ms.transaction.controllers;

import com.montbank.ms.transaction.dtos.TransactionDTO;
import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<TransactionModel> sendTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        TransactionModel transactionModel = new TransactionModel();
        BeanUtils.copyProperties(transactionDTO, transactionModel);
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.sendTransaction(transactionModel));
    }

}
