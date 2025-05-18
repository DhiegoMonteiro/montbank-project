package com.montbank.ms.transaction.services;

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
    private final Queue processedTransactionQueue;

    @Autowired
    public MessageSenderService(
            RabbitTemplate rabbitTemplate,
            @Qualifier("userValidationQueue") Queue userValidationQueue,
            @Qualifier("processedTransactionQueue") Queue processedTransactionQueue
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.userValidationQueue = userValidationQueue;
        this.processedTransactionQueue = processedTransactionQueue;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void sendUserValidationRequest(UUID userId){
        rabbitTemplate.convertAndSend(userValidationQueue.getName(), userId.toString());
    }

    public void sendProcessedTransactionEvent(UUID sender, BigDecimal amount, UUID receiver){
        var transactionDTO = new TransactionMessageDTO(sender, amount, receiver);
        rabbitTemplate.convertAndSend(processedTransactionQueue.getName(), transactionDTO);
    }
}
