package com.sms.service.provider.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sms.api.SmsRequestTranslator;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsResponse;
import com.sms.api.domain.SmsWrapperService;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 阿里云发送短信服务
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/18
 * Time: 14:47
 */

@Slf4j
@Service
public class AliyunSenderWrapper implements SmsWrapperService {


    @Autowired
    AliyunSmsProvider aliSmsProvider;

    @Autowired
    private SmsRequestTranslator translator;

    @Autowired
    AliyunSmsProviderProperties smsConfig;


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
            return aliSmsProvider.sendSms(sendSmsRequest).thenApply(resp -> response(resp, sendSmsRequest, uniqueId));

        }).collect(Collectors.toList());

        // 接收一系列的CompletableFuture，并返回一个新的CompletableFuture，它会在所有的输入CompletableFuture都完成时才会完成。
        // 如果任何输入的CompletableFuture异常终止，则新的CompletableFuture也会异常终止。
        // 当原先的CompletableFuture完成的时候.用join()方法获取它的结果并收集

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(item -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    @NotNull
    private static SmsResponse response(SendSmsResponse resp, SendSmsRequest sendSmsRequest, String uniqueId) {
        if (Objects.equals(resp.getBody().getCode(), "OK")) {
            log.info("短信发送成功：{}, BizId：{}, 原因：{}", sendSmsRequest.getPhoneNumbers(), resp.getBody().getBizId(), resp.getBody().getMessage());
            // 成功返回执行结果等信息
            return SmsResponse.sendSuccess(uniqueId, resp.getBody().getMessage(), resp.getBody().getBizId());
        } else {
            log.info("短信发送失败：{}, BizId：{}, 原因：{}", sendSmsRequest.getPhoneNumbers(), resp.getBody().getBizId(), resp.getBody().getMessage());
            // 失败则返回错误码
            return SmsResponse.sendError(uniqueId, resp.getBody().getMessage(), resp.getBody().getBizId());
        }
    }

    @Override
    public CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest) {
        smsRequest.setSignText(smsConfig.getSignName());
        SendSmsRequest sendSmsRequest = (SendSmsRequest) translator.translate(smsRequest);
        return aliSmsProvider.sendSms(sendSmsRequest).thenApply(resp -> response(resp, sendSmsRequest, smsRequest.getUniqueId()));
    }
}
