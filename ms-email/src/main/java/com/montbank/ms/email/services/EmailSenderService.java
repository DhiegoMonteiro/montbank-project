package com.montbank.ms.email.services;

import com.montbank.ms.email.dtos.EmailInformationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailTo(EmailInformationDTO emailInformationDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailInformationDTO.getEmail());
        message.setFrom("${spring.mail.username}");
        message.setSubject("Boas-Vindas ao MontBank");
        message.setText("Olá " + emailInformationDTO.getName() + " seja bem vindo ao MontBank, muito obrigado por escolher nosso banco!!!");
        javaMailSender.send(message);
    }
}
