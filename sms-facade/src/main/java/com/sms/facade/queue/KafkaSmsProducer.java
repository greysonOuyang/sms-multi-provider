package com.sms.facade.queue;

import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * kafka短信生产者
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/18
 * Time: 16:00
 */
@Service
public class KafkaSmsProducer {

    @Autowired
    private KafkaTemplate<String, SmsRequest> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    /**
     * 批量根据模板发送短信
     * @param smsTarget
     */
    public void handlerTemplateSms(SmsRequest smsTarget) {
        kafkaTemplate.send(topic, smsTarget);
    }

}
