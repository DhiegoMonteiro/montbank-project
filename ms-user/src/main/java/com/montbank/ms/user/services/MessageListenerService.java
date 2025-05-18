package com.montbank.ms.user.services;

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
    public void handleUserValidationRequest(String userId){
        boolean userExists = userService.userExists(UUID.fromString(userId));
    }
    @RabbitListener(queues = "processed.transaction.queue")
    public void handleProcessedTransaction(TransactionMessageDTO transactionMessageDTO){
        UUID sender = transactionMessageDTO.getSender();
        BigDecimal amount = transactionMessageDTO.getAmount();
        UUID receiver = transactionMessageDTO.getReceiver();
        userService.updateBalance(sender, amount, receiver);
    }
}
