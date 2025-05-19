package com.montbank.ms.email.services;

import com.montbank.ms.email.dtos.EmailInformationDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerService {

    @Autowired
    EmailSenderService emailSenderService;


    @RabbitListener(queues = "email.sender.queue")
    public void handleWelcomeEmail(EmailInformationDTO emailInformationDTO){
        emailSenderService.sendEmailTo(emailInformationDTO);
    }
}
