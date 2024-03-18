package com.sms.facade.sender;

import com.sms.api.RequestHelperService;
import com.sms.api.TemplateMessageBuilder;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.exception.SmsCommonException;
import com.sms.api.exception.SmsSendException;
import com.sms.facade.queue.KafkaSmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 用户门面，最高一级抽象，用户调用此方法执行发送短信
 */
@Service
public class SmsFacade {

    //获取发送短信是否使用队列的配置
    @Value("${sms.queue}")
    private boolean enableQueue;

    @Autowired
    private SmsTrulySender smsTrulySender;

    @Autowired
    private TemplateMessageBuilder templateMessageBuilder;

    @Autowired
    private RequestHelperService requestHelperService;

    @Autowired
    private KafkaSmsProducer kafkaSmsProducer;

    // TODO 分为模板发送和普通发送以及各种发送

    /**
     * 根据模板发送短信
     * @param smsRequest 请求参数
     * @throws SmsSendException 发送失败异常
     * @throws SmsCommonException 业务异常
     */
    public void sendByTemplateSimple(SmsRequest smsRequest) throws SmsSendException, SmsCommonException {
        if (enableQueue) {
            // 启用队列发送
            kafkaSmsProducer.handlerTemplateSms(smsRequest);
        } else {
            // 直接发送
            smsTrulySender.execute(smsRequest);
        }
    }

    /**
     * 根据模板发送短信
     * @param batchSmsRequest 请求参数 支持批量发送
     * @throws SmsSendException 发送失败异常
     * @throws SmsCommonException 业务异常
     */
    public void sendByTemplateMulti(BatchSmsRequest batchSmsRequest) throws SmsSendException, SmsCommonException {
        if (enableQueue) {
            // 启用队列发送
            kafkaSmsProducer.handlerTemplateSmsMulti(batchSmsRequest);
        } else {
            // 直接发送
            smsTrulySender.executeMulti(batchSmsRequest);
        }
    }
}