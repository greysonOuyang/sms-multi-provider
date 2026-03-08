package org.moretea.sms.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.facade.sender.SmsFacade;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信服务演示类
 * 
 * 展示如何在业务代码中使用 SmsFacade
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsDemoService {

    private final SmsFacade smsFacade;

    /**
     * 发送验证码
     */
    public boolean sendVerificationCode(String phone, String code) {
        // 方式1：直接发送（简单但不够灵活）
        // SmsResponse response = smsFacade.send(phone, "您的验证码是：" + code);
        
        // 方式2：使用模板（推荐）
        SmsRequest request = SmsRequest.builder()
                .phoneNumber(phone)
                .templateCode("verification_code")  // 需要先在 templates.json 中配置
                .templateParams(Map.of("code", code))
                .build();
        
        SmsResponse response = smsFacade.sendTemplate(request);
        
        if (Boolean.TRUE.equals(response.getSendStatus())) {
            log.info("验证码发送成功, phone={}, uniqueId={}", phone, response.getUniqueId());
            return true;
        } else {
            log.error("验证码发送失败, phone={}, error={}", phone, response.getMsg());
            return false;
        }
    }

    /**
     * 发送通知短信
     */
    public boolean sendNotification(String phone, String message) {
        SmsResponse response = smsFacade.send(phone, message);
        return Boolean.TRUE.equals(response.getSendStatus());
    }

    /**
     * 发送订单通知
     */
    public boolean sendOrderNotification(String phone, String orderNo, String status) {
        SmsRequest request = SmsRequest.builder()
                .phoneNumber(phone)
                .templateCode("order_notification")
                .templateParams(Map.of(
                        "orderNo", orderNo,
                        "status", status
                ))
                .build();
        
        SmsResponse response = smsFacade.sendTemplate(request);
        return Boolean.TRUE.equals(response.getSendStatus());
    }
}
