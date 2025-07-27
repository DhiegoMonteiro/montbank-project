package com.montbank.ms.user.services;

import com.montbank.ms.user.dtos.TransactionCheckBalanceDTO;
import com.montbank.ms.user.dtos.TransactionMessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class MessageListenerService {

    @Autowired
    UserService userService;

    @RabbitListener(queues = "user.validation.queue")
    public boolean handleUserValidationRequest(String userId){
        return userService.userExists(UUID.fromString(userId));
    }

    @RabbitListener(queues = "user.validation.email.queue")
    public boolean handleUserValidationEmailRequest(String userEmail){
        return userService.userExistsByEmail(userEmail);
    }
    @RabbitListener(queues = "user.validation.balance.queue")
    public Boolean handleUserValidationBalanceRequest(TransactionCheckBalanceDTO transactionCheckBalanceDTO){
            UUID sender = transactionCheckBalanceDTO.getSender();
            BigDecimal amount = transactionCheckBalanceDTO.getAmount();
            return userService.getBalanceValidation(sender, amount);
    }
    @RabbitListener(queues = "processed.transaction.queue")
    public void handleProcessedTransaction(TransactionMessageDTO transactionMessageDTO){
        UUID sender = transactionMessageDTO.getSender();
        BigDecimal amount = transactionMessageDTO.getAmount();
        String receiver = transactionMessageDTO.getReceiver();
        userService.updateBalance(sender, amount, receiver);
    }
}
