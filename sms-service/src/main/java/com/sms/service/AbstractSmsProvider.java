package com.sms.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sms.api.SmsProvider;
import com.sms.api.UnavailableHandler;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsResponse;
import com.sms.api.exception.SmsSendException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AbstractSmsProvider  {
    
    @Autowired
    private UnavailableHandler unavailableHandler;

    @Autowired
    private SmsProvider smsProvider;

    /**
     * 服务是否可用
     */
    private boolean available = true;

    @Override
    void sendSms(SmsRequest smsRequest) throws SmsSendException {
        if (!available) {
            throw new SmsSendException("服务不可用。");
        }
        try {
            sendSms(smsRequest);
        } catch (Exception ex) {
            // 出现异常，标记此服务主为不可用，并通知UnavailableHandler
            unavailable();
            throw new SmsSendException("短信发送失败，服务被标记为不可用。", ex);
        }
    }

    @Override
    public CompletableFuture<List<SmsResponse>> sendBatchSms(BatchSmsRequest request) {
        if (CollectionUtils.isEmpty(request.getTargets())) {
            throw new IllegalArgumentException("发送目标不能为空");
        }

        List<CompletableFuture<SmsResponse>> futures = request.getTargets().stream().map(target -> {
            String uniqueId = target.getUniqueId();
            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .phoneNumbers(target.getPhoneNumber())
                    .signName(smsConfig.getSignName())
                    .templateCode(request.getTemplateCode())
                    .templateParam(JSONObject.toJSONString(target.getTemplateParams()))
                    .build();
            // 遍历发送短信
            return trulySender.sendSms(sendSmsRequest).thenApply(resp -> response(resp, sendSmsRequest, uniqueId));

        }).collect(Collectors.toList());

        // 接收一系列的CompletableFuture，并返回一个新的CompletableFuture，它会在所有的输入CompletableFuture都完成时才会完成。
        // 如果任何输入的CompletableFuture异常终止，则新的CompletableFuture也会异常终止。
        // 当原先的CompletableFuture完成的时候.用join()方法获取它的结果并收集

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(item -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }



    protected void unavailable() {
        available = false;
        // 通知UnavailableHandler此服务商状态为不可用
        unavailableHandler.handleUnavailable(this);
    }
}