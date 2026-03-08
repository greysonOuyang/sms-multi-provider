package org.moretea.sms.facade.sender;

import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.SmsProducer;
import org.moretea.sms.api.domain.BatchSmsRequest;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.api.exception.SmsCommonException;
import org.moretea.sms.api.exception.SmsSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 短信发送门面类
 * 
 * 提供简洁的 API 供开发者使用：
 * - send(): 直接发送（最简方式）
 * - sendTemplate(): 模板发送
 * - sendAsync(): 异步批量发送
 * 
 * @author greyson
 * @since 1.0.0
 */
@Slf4j
@Service
public class SmsFacade {

    @Value("${sms.queue.enabled:false}")
    private boolean enableQueue;

    @Autowired
    private SmsTrulySender smsTrulySender;

    @Autowired(required = false)
    private SmsProducer smsProducer;

    /**
     * 发送短信（最简方式）
     * 
     * 直接发送短信，不需要模板。
     * 适用场景：简单通知、验证码等。
     *
     * @param phoneNumber 手机号
     * @param message     短信内容
     * @return 发送结果
     */
    public SmsResponse send(String phoneNumber, String message) {
        SmsRequest request = SmsRequest.builder()
                .phoneNumber(phoneNumber)
                .message(message)
                .uniqueId(generateUniqueId())
                .build();
        return send(request);
    }

    /**
     * 发送短信（完整参数）
     *
     * @param request 短信请求
     * @return 发送结果
     */
    public SmsResponse send(SmsRequest request) {
        try {
            // 补充必要字段
            if (request.getUniqueId() == null) {
                request.setUniqueId(generateUniqueId());
            }
            
            log.info("发送短信, phoneNumber: {}, uniqueId: {}", 
                    request.getPhoneNumber(), request.getUniqueId());
            
            SmsResponse response = smsTrulySender.execute(request);
            log.info("短信发送完成, uniqueId: {}, result: {}", 
                    request.getUniqueId(), response.getSendStatus());
            
            return response;
        } catch (SmsSendException e) {
            log.error("短信发送失败, uniqueId: {}, error: {}", 
                    request.getUniqueId(), e.getMessage());
            return SmsResponse.sendError(request.getUniqueId(), e.getMessage(), null);
        }
    }

    /**
     * 模板发送（简版）
     *
     * @param phoneNumber  手机号
     * @param templateCode 模板代码
     * @param params       模板参数（JSON字符串）
     * @return 发送结果
     */
    public SmsResponse sendTemplate(String phoneNumber, String templateCode, String params) {
        SmsRequest request = SmsRequest.builder()
                .phoneNumber(phoneNumber)
                .templateCode(templateCode)
                .params(params)
                .uniqueId(generateUniqueId())
                .build();
        return sendTemplate(request);
    }

    /**
     * 模板发送（完整参数）
     *
     * @param request 短信请求（需包含 templateCode 和 params/templateParams）
     * @return 发送结果
     */
    public SmsResponse sendTemplate(SmsRequest request) {
        try {
            // 补充必要字段
            if (request.getUniqueId() == null) {
                request.setUniqueId(generateUniqueId());
            }
            
            log.info("模板发送短信, phoneNumber: {}, templateCode: {}, uniqueId: {}", 
                    request.getPhoneNumber(), request.getTemplateCode(), request.getUniqueId());
            
            SmsResponse response = smsTrulySender.executeTemplate(request);
            log.info("模板短信发送完成, uniqueId: {}, result: {}", 
                    request.getUniqueId(), response.getSendStatus());
            
            return response;
        } catch (SmsSendException e) {
            log.error("模板短信发送失败, uniqueId: {}, error: {}", 
                    request.getUniqueId(), e.getMessage());
            return SmsResponse.sendError(request.getUniqueId(), e.getMessage(), null);
        }
    }

    /**
     * 异步批量发送（高级场景）
     *
     * @param batchRequest 批量请求
     * @return 异步结果
     */
    public CompletableFuture<Void> sendAsync(BatchSmsRequest batchRequest) {
        return CompletableFuture.runAsync(() -> {
            try {
                if (enableQueue && smsProducer != null) {
                    smsProducer.sendTemplateSmsMulti(batchRequest);
                } else {
                    smsTrulySender.executeMulti(batchRequest);
                }
            } catch (Exception e) {
                log.error("异步批量发送失败", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 生成唯一追踪ID
     */
    private String generateUniqueId() {
        return System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }

    // ========== 旧方法保留（兼容）==========

    /**
     * @deprecated 使用 {@link #sendTemplate(SmsRequest)} 替代
     */
    @Deprecated
    public void sendByTemplateSimple(SmsRequest smsRequest) throws SmsSendException, SmsCommonException {
        sendTemplate(smsRequest);
    }

    /**
     * @deprecated 使用 {@link #sendAsync(BatchSmsRequest)} 替代
     */
    @Deprecated
    public void sendByTemplateMulti(BatchSmsRequest batchSmsRequest) throws SmsSendException, SmsCommonException {
        try {
            if (enableQueue && smsProducer != null) {
                smsProducer.sendTemplateSmsMulti(batchSmsRequest);
            } else {
                smsTrulySender.executeMulti(batchSmsRequest);
            }
        } catch (Exception e) {
            throw new SmsSendException("批量发送失败", e);
        }
    }
}
