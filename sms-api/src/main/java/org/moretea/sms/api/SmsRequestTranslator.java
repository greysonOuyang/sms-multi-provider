package org.moretea.sms.api;


import org.moretea.sms.api.domain.SmsRequest;

public interface SmsRequestTranslator {
    Object translate(SmsRequest smsRequest);
}