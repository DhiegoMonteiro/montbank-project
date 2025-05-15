package com.montbank.ms.transaction.controllers;

import com.montbank.ms.transaction.dtos.TransactionDTO;
import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/new")
    public ResponseEntity<TransactionModel> sendTransaction(@RequestBody @Valid TransactionDTO transactionDTO,
                                                            HttpServletRequest request) {
        String senderId = (String) request.getAttribute("userId");
        if (senderId == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        UUID senderUUID = UUID.fromString(senderId);
        TransactionModel transactionModel = new TransactionModel();
        BeanUtils.copyProperties(transactionDTO, transactionModel);

        return ResponseEntity.status(HttpStatus.OK).body(transactionService.sendTransaction(transactionModel,
                senderUUID));
    }

}
