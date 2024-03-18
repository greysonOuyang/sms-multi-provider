package com.sms.service.provider.aliyun;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.sms.api.SmsProvider;
import com.sms.api.SmsRequestTranslator;
import com.sms.api.SmsResponseTranslator;
import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsResponse;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;


/**
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/18
 * Time: 16:18
 */
@Slf4j
@Service
public class AliTrulySender implements SmsProvider {
    @Autowired
    private AliyunSmsProviderProperties smsConfig;

    @Autowired
    private SmsRequestTranslator requestTranslator;

    @Autowired
    private SmsResponseTranslator responseTranslator;

    private AsyncClient client;

    @PostConstruct
    private void initClient() {
        log.info("阿里云短信平台初始化");
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(smsConfig.getAccessKeyId())
                .accessKeySecret(smsConfig.getAccessKeySecret())
                .build());

        this.client = AsyncClient.builder()
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(smsConfig.getDomain())
                )
                .build();
    }

    @Override
    @Async
    public CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest) {
        SendSmsRequest request = (SendSmsRequest) requestTranslator.translate(smsRequest);
        return client.sendSms(request)
                        .thenApply(resp -> responseTranslator.translate(resp, smsRequest))
                .exceptionally(throwable -> {
                    log.error("调用阿里云服务发送短信出现异常: ", throwable);
                    return null;
                });

    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public String getName() {
        return "aliyun";
    }
}
