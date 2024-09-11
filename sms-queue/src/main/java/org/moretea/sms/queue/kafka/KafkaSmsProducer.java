package org.moretea.sms.queue.kafka;

import com.google.gson.Gson;
import com.sms.api.SmsProducer;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.exception.SmsCommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * kafka短信生产者
 * <p>
 * Author: greyson
 * Email:
 * Date: 2024/3/18
 * Time: 16:00
 */
@Service
public class KafkaSmsProducer implements SmsProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${sms.kafka.topic}")
    private String smsTopic;

    @Override
    public void sendTemplateSms(SmsRequest smsRequest) throws SmsCommonException {
        try {
            kafkaTemplate.send(smsTopic, new Gson().toJson(smsRequest));
        } catch (Exception e) {
            throw new SmsCommonException("发送短信到Kafka队列失败", e);
        }
    }

    @Override
    public void sendTemplateSmsMulti(BatchSmsRequest batchSmsRequest) throws SmsCommonException {
        try {
            kafkaTemplate.send(smsTopic, new Gson().toJson(batchSmsRequest));
        } catch (Exception e) {
            throw new SmsCommonException("发送批量短信到Kafka队列失败", e);
        }
    }
}

