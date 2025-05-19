package com.montbank.ms.user.services;

import com.montbank.ms.user.dtos.EmailInformationDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {

    private final RabbitTemplate rabbitTemplate;
    private final Queue emailSenderQueue;

    @Autowired
    public MessageSenderService(
            RabbitTemplate rabbitTemplate,
            @Qualifier("emailSenderQueue") Queue emailSenderQueue
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.emailSenderQueue = emailSenderQueue;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }
    public void sendEmail(String email, String name){
        var emailInformation = new EmailInformationDTO(email, name);
        rabbitTemplate.convertAndSend(emailSenderQueue.getName(), emailInformation);
    }
}
