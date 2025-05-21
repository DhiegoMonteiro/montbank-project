package com.montbank.ms.transaction.controllers;

import com.montbank.ms.transaction.dtos.TransactionRequestDTO;
import com.montbank.ms.transaction.dtos.TransactionUpdateDTO;
import com.montbank.ms.transaction.models.TransactionModel;
import com.montbank.ms.transaction.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/history")
    public List<TransactionModel> getUserTransactions(@RequestAttribute String userId,
                                                      @RequestAttribute String userEmail) {
        if (userId == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        return transactionService.getAllUserTransactions(UUID.fromString(userId), userEmail);
    }

    @PostMapping("/history/new")
    public ResponseEntity<TransactionModel> sendTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO,
                                                            @RequestAttribute String userId,
                                                            @RequestAttribute String userEmail,
                                                            @RequestAttribute String name) {
        TransactionModel savedTransaction = transactionService.save(transactionRequestDTO,
                                                                    UUID.fromString(userId),name, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedTransaction);
    }

    @DeleteMapping("/history/{transactionId}/delete")
    public ResponseEntity<Void> deleteTransactionHistory(@PathVariable UUID transactionId,
                                                         @RequestAttribute String userId) {
        transactionService.deleteTransactionHistory(transactionId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/history/{transactionId}/edit")
    public ResponseEntity<Void> editTransactionTitle(@PathVariable UUID transactionId,
                                                     @RequestBody @Valid TransactionUpdateDTO transactionUpdateDTO,
                                                     @RequestAttribute String userId){
        transactionService.updateTransactionTitle(transactionUpdateDTO,UUID.fromString(userId),transactionId);
        return ResponseEntity.noContent().build();
    }
}
