package com.montbank.ms.transaction.services;

import com.montbank.ms.transaction.dtos.TransactionCheckBalanceDTO;
import com.montbank.ms.transaction.dtos.TransactionMessageDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class MessageSenderService {

    private final RabbitTemplate rabbitTemplate;
    private final Queue userValidationQueue;
    private final Queue userValidationEmailQueue;
    private final Queue processedTransactionQueue;
    private final Queue userValidationBalanceQueue;


    @Autowired
    public MessageSenderService(
            RabbitTemplate rabbitTemplate,
            @Qualifier("userValidationQueue") Queue userValidationQueue,
            @Qualifier("processedTransactionQueue") Queue processedTransactionQueue,
            @Qualifier("userValidationEmailQueue") Queue userValidationEmailQueue,
            @Qualifier("userValidationBalanceQueue") Queue userValidationBalanceQueue
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.userValidationQueue = userValidationQueue;
        this.processedTransactionQueue = processedTransactionQueue;
        this.userValidationEmailQueue = userValidationEmailQueue;
        this.userValidationBalanceQueue = userValidationBalanceQueue;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public Boolean sendUserValidationRequest(UUID userId){
        return (Boolean) rabbitTemplate.convertSendAndReceive(userValidationQueue.getName(), userId);
    }
    public Boolean sendUserValidationEmailRequest(String userEmail){
        return (Boolean) rabbitTemplate.convertSendAndReceive(userValidationEmailQueue.getName(), userEmail);
    }
    public Boolean sendUserValidationBalanceQueue(UUID senderId, BigDecimal amount){
        var transactionCheckBalanceDTO = new TransactionCheckBalanceDTO(senderId, amount);
        return (Boolean) rabbitTemplate.convertSendAndReceive(userValidationBalanceQueue.getName(), transactionCheckBalanceDTO);
    }

    public void sendProcessedTransactionEvent(UUID sender, BigDecimal amount, String receiver){
        var transactionDTO = new TransactionMessageDTO(sender, amount, receiver);
        rabbitTemplate.convertAndSend(processedTransactionQueue.getName(), transactionDTO);
    }
}
