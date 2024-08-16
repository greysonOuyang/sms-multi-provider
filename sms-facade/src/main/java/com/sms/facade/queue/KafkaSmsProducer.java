package com.sms.facade.queue;

import com.alibaba.fastjson2.JSON;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
public class KafkaSmsProducer {


    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSmsProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    /**
     * 批量根据模板发送短信
     * @param smsTarget
     */
    public void handlerTemplateSms(SmsRequest smsTarget) {
        kafkaTemplate.send("kafka.topic.single", JSON.toJSONString(smsTarget));
    }

    public void handlerTemplateSmsMulti(BatchSmsRequest batchSmsRequest) {
        kafkaTemplate.send("kafka.topic.multi", JSON.toJSONString(batchSmsRequest));
    }
}
