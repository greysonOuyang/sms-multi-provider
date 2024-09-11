package org.moretea.sms.api;

import org.moretea.sms.api.domain.BatchSmsRequest;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.exception.SmsCommonException;

public interface SmsProducer {
    void sendTemplateSms(SmsRequest smsRequest) throws SmsCommonException;
    void sendTemplateSmsMulti(BatchSmsRequest batchSmsRequest) throws SmsCommonException;
}