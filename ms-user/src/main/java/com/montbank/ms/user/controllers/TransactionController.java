package com.montbank.ms.user.controllers;

import com.montbank.ms.user.dtos.TransactionDTO;
import com.montbank.ms.user.models.TransactionModel;
import com.montbank.ms.user.services.TransactionService;
import com.montbank.ms.user.services.UserService;
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
