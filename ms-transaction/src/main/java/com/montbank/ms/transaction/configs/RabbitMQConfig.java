package com.montbank.ms.transaction.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue userValidationQueue(){
        return new Queue("user.validation.queue", true, false, false);
    }

    @Bean
    public Queue userValidationEmailQueue(){
        return new Queue("user.validation.email.queue", true, false, false);
    }
    @Bean Queue userValidationBalanceQueue(){
        return new Queue("user.validation.balance.queue", true, false , false);
    }


    @Bean Queue processedTransactionQueue(){
        return new Queue("processed.transaction.queue", true, false , false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setChannelTransacted(false);
        return template;
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
