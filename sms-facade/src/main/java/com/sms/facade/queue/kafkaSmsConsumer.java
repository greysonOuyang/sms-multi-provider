package com.sms.facade.queue;

/**
 * kafka 消费者 执行发送消息逻辑
 */

import com.sms.facade.sender.SmsTrulySender;
import com.sms.api.domain.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
public class kafkaSmsConsumer {
    @Autowired
    private SmsTrulySender smsTrulySender;

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(SmsRequest smsRequest) throws InterruptedException {
        smsTrulySender.execute(smsRequest);
    }
}