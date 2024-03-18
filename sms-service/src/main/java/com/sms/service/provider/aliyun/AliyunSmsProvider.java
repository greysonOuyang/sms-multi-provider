package com.sms.service.provider.aliyun;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.sms.service.AbstractSmsProvider;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
@Slf4j
@Service("aliyunSmsProvider")
public class AliyunSmsProvider extends AbstractSmsProvider {


    @Autowired
    private AliyunSmsProviderProperties smsConfig;

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

    @Async
    public CompletableFuture<SendSmsResponse> sendSms(SendSmsRequest smsRequest) {
        return client.sendSms(smsRequest)
                .exceptionally(throwable -> {
                    log.error("调用阿里云服务发送短信出现异常: ", throwable);
                    return null;
                });
    }


    @Override
    protected void sendSmsInternal(String phoneNumber, String message) throws Exception {

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

