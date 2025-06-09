package com.montbank.ms.email.services;

import com.montbank.ms.email.dtos.EmailInformationDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmailTo(EmailInformationDTO emailInformationDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailInformationDTO.getEmail());
            helper.setSubject("Boas-Vindas ao MontBank");
            helper.setFrom(fromEmail);

            String htmlContent = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <title>Boas-Vindas ao MontBank</title>
                </head>
                <body style="margin:0; padding:0; font-family:Arial, sans-serif; background-color:#0b1c2c; color:#ffffff;">
                    <div style="max-width:600px; margin:0 auto; background:#12263a; border-radius:8px; overflow:hidden; box-shadow:0 0 10px rgba(0,0,0,0.5);">
                        <div style="padding:30px;">
                            <h1 style="color:#58a6ff;">Seja bem-vindo ao MontBank!</h1>
                            <p style="font-size:16px;">Olá <strong>%s</strong>,</p>
                            <p>É um prazer ter você conosco. No <strong>MontBank</strong>, valorizamos cada cliente e estamos comprometidos em fornecer a melhor experiência bancária digital.</p>
                            <p style="margin-top:20px;">Explore nossos serviços, acompanhe suas transações, solicite seus cartões e tenha o controle financeiro na palma da mão.</p>
                            <p style="margin-top:40px; font-size:14px; color:#aaa;">Obrigado por escolher o MontBank. <br>Estamos sempre à disposição.</p>
                        </div>
                        <div style="background-color:#0e2233; padding:15px; text-align:center; font-size:12px; color:#666;">
                            © 2025 MontBank. Todos os direitos reservados.
                        </div>
                    </div>
                </body>
                </html>
            """.formatted(emailInformationDTO.getName());

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail");
        }
    }
}
