package org.moretea.sms.queue.kafka;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.exception.SmsSendException;
import com.sms.facade.sender.SmsTrulySender;
import com.sms.api.domain.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * kafka 消费者 执行发送消息逻辑
 */
@Service
public class kafkaSmsConsumer {

    private final SmsTrulySender smsTrulySender;

    @Autowired
    public kafkaSmsConsumer(SmsTrulySender smsTrulySender) {
        this.smsTrulySender = smsTrulySender;
    }

    @KafkaListener(topics = "kafka.topic.single")
    public void consume(SmsRequest smsRequest) throws SmsSendException {
        smsTrulySender.execute(smsRequest);
    }

    @KafkaListener(topics = "kafka.topic.multi")
    public void consumeMulti(BatchSmsRequest batchSmsRequest) {
        smsTrulySender.executeMulti(batchSmsRequest);
    }
}