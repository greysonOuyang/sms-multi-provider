package org.moretea.sms.facade.sender;

import org.moretea.sms.api.RequestHelperService;
import org.moretea.sms.api.SmsProducer;
import org.moretea.sms.api.TemplateMessageBuilder;
import org.moretea.sms.api.domain.BatchSmsRequest;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.exception.SmsCommonException;
import org.moretea.sms.api.exception.SmsSendException;
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