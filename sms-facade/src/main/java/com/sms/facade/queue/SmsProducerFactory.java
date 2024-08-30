package com.sms.facade.queue;

import com.sms.api.SmsProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsProducerFactory {

    @Value("${sms.queue.type:}")
    private String queueType;

    @Bean
    public SmsProducer smsProducer() {
        switch (queueType.toLowerCase()) {
            case "kafka":
                return new KafkaSmsProducer();
            case "rabbitmq":
                return new RabbitMQSmsProducer();
            default:
                return null;
        }
    }
}