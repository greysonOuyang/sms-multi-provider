package com.sms.facade.queue;


import com.sms.facade.RetryHandler;
import com.sms.api.domain.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
public class SmsConsumer {
    @Autowired
    private RetryHandler retryHandler;

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(SmsRequest smsRequest) throws InterruptedException {
        retryHandler.execute(smsRequest);
    }
}