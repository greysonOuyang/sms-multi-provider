package com.sms.facade.sender;

import com.sms.api.RequestHelperService;
import com.sms.api.SmsProducer;
import com.sms.api.TemplateMessageBuilder;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.exception.SmsCommonException;
import com.sms.api.exception.SmsSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 用户门面，最高一级抽象，用户调用此方法执行发送短信
 */
@Service
public class SmsFacade {

    @Value("${sms.queue.enabled:false}")
    private boolean enableQueue;

    @Autowired
    private SmsTrulySender smsTrulySender;

    @Autowired
    private TemplateMessageBuilder templateMessageBuilder;

    @Autowired
    private RequestHelperService requestHelperService;

    @Autowired(required = false)
    private SmsProducer smsProducer;

    // ... 其他代码保持不变 ...

    public void sendByTemplateSimple(SmsRequest smsRequest) throws SmsSendException, SmsCommonException {
        if (enableQueue && smsProducer != null) {
            smsProducer.sendTemplateSms(smsRequest);
        } else {
            smsTrulySender.execute(smsRequest);
        }
    }

    public void sendByTemplateMulti(BatchSmsRequest batchSmsRequest) throws SmsSendException, SmsCommonException {
        if (enableQueue && smsProducer != null) {
            smsProducer.sendTemplateSmsMulti(batchSmsRequest);
        } else {
            smsTrulySender.executeMulti(batchSmsRequest);
        }
    }
}