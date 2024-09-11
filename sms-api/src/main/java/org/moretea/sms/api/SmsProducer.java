package com.sms.api;

import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.exception.SmsCommonException;

public interface SmsProducer {
    void sendTemplateSms(SmsRequest smsRequest) throws SmsCommonException;
    void sendTemplateSmsMulti(BatchSmsRequest batchSmsRequest) throws SmsCommonException;
}