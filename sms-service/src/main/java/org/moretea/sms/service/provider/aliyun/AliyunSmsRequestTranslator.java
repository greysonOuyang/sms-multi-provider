package org.moretea.sms.service.provider.aliyun;

import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import org.moretea.sms.api.SmsRequestTranslator;
import org.moretea.sms.api.domain.SmsRequest;
import org.springframework.stereotype.Service;

/**
 * 阿里云短信参数转换
 */
@Service
public class AliyunSmsRequestTranslator implements SmsRequestTranslator {
    @Override
    public SendSmsRequest translate(SmsRequest smsRequest) {
        return SendSmsRequest.builder()
                .phoneNumbers(smsRequest.getPhoneNumber())
                .signName(smsRequest.getSignName())
                .templateCode(smsRequest.getTemplateCode())
                .templateParam(smsRequest.getParams())
                .build();
    }
}