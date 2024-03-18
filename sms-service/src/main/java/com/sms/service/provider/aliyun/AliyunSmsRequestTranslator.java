package com.sms.service.provider.aliyun;

import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.sms.api.SmsRequestTranslator;
import com.sms.api.domain.SmsRequest;
import org.springframework.stereotype.Service;

/**
 * 阿里云短信参数转换
 */
@Service
public class AliyunSmsRequestTranslator implements SmsRequestTranslator {
    @Override
    public SendSmsRequest translate(SmsRequest smsRequest) {
        return SendSmsRequest.builder()
                .phoneNumbers(smsRequest.getMobiles())
                .signName(smsRequest.getSignText())
                .templateCode(smsRequest.getReqCode())
                .templateParam(smsRequest.getMsg())
                .build();
    }
}