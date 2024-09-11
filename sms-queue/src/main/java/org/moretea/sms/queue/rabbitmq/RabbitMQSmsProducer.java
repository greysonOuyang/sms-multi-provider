package org.moretea.sms.queue.rabbitmq;

import com.google.gson.Gson;
import com.sms.api.SmsProducer;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.exception.SmsCommonException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSmsProducer implements SmsProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${sms.rabbitmq.exchange}")
    private String smsExchange;

    @Value("${sms.rabbitmq.routingKey}")
    private String smsRoutingKey;

    @Override
    public void sendTemplateSms(SmsRequest smsRequest) throws SmsCommonException {
        try {
            rabbitTemplate.convertAndSend(smsExchange, smsRoutingKey, new Gson().toJson(smsRequest));
        } catch (Exception e) {
            throw new SmsCommonException("发送短信到RabbitMQ队列失败", e);
        }
    }

    @Override
    public void sendTemplateSmsMulti(BatchSmsRequest batchSmsRequest) throws SmsCommonException {
        try {
            rabbitTemplate.convertAndSend(smsExchange, smsRoutingKey, new Gson().toJson(batchSmsRequest));
        } catch (Exception e) {
            throw new SmsCommonException("发送批量短信到RabbitMQ队列失败", e);
        }
    }
}