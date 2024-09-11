package com.sms.api;


import com.sms.api.domain.SmsRequest;

public interface SmsRequestTranslator {
    Object translate(SmsRequest smsRequest);
}