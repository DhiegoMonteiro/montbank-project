package com.montbank.ms.user.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {

    @Bean
    public Queue userValidationQueue(){
        return new Queue("user.validation.queue", true, false, false);
    }

    @Bean
    public Queue processedTransactionQueue(){
        return new Queue("processed.transaction.queue", true, false , false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
