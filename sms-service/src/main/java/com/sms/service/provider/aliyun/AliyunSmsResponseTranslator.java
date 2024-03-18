package com.sms.service.provider.aliyun;

import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.sms.api.SmsRequestTranslator;
import com.sms.api.SmsResponseTranslator;
import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 阿里云短信参数转换
 */
@Slf4j
@Service
public class AliyunSmsResponseTranslator implements SmsResponseTranslator {

    @Override
    public SmsResponse translate(Object  resp, SmsRequest sendSmsRequest) {
        SendSmsResponse smsResp = (SendSmsResponse) resp;
        if (Objects.equals(smsResp.getBody().getCode(), "OK")) {
            log.debug("短信发送成功：{}, BizId：{}, 原因：{}", sendSmsRequest.getPhoneNumber(), smsResp.getBody().getBizId(), smsResp.getBody().getMessage());
            // 成功返回执行结果等信息
            return SmsResponse.sendSuccess(sendSmsRequest.getUniqueId(), smsResp.getBody().getMessage(), smsResp.getBody().getBizId());
        } else {
            log.debug("短信发送失败：{}, BizId：{}, 原因：{}", sendSmsRequest.getPhoneNumber(), smsResp.getBody().getBizId(), smsResp.getBody().getMessage());
            // 失败则返回错误码
            return SmsResponse.sendError(sendSmsRequest.getUniqueId(), smsResp.getBody().getMessage(), smsResp.getBody().getBizId());
        }
    }
}