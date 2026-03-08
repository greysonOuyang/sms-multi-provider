package org.moretea.sms.service.provider.aliyun;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.AbstractSmsProvider;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * 阿里云短信服务商实现
 * 
 * @author greyson
 * @since 1.0.0
 */
@Slf4j
@Service("aliyunSmsProvider")
public class AliyunSmsProvider extends AbstractSmsProvider {
    
    @Autowired
    private AliyunSmsProviderProperties smsConfig;

    @Autowired
    private AliyunSmsRequestTranslator requestTranslator;

    @Autowired
    private AliyunSmsResponseTranslator responseTranslator;

    private AsyncClient client;

    @PostConstruct
    private void initClient() {
        log.info("阿里云短信平台初始化, domain: {}, weight: {}", 
                smsConfig.getDomain(), smsConfig.getWeight());
        
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(smsConfig.getAccessKeyId())
                .accessKeySecret(smsConfig.getAccessKeySecret())
                .build());

        this.client = AsyncClient.builder()
                .credentialsProvider(provider)
                .overrideConfiguration(
                        darabonba.core.client.ClientOverrideConfiguration.create()
                                .setEndpointOverride(smsConfig.getDomain())
                )
                .build();
    }

    @Override
    @Async
    public CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest) {
        // 如果请求没有设置签名，使用配置的默认签名
        if (smsRequest.getSignName() == null || smsRequest.getSignName().isEmpty()) {
            smsRequest.setSignName(smsConfig.getSignName());
        }
        
        log.info("阿里云发送短信, phoneNumber: {}, templateCode: {}", 
                smsRequest.getPhoneNumber(), smsRequest.getTemplateCode());
        
        SendSmsRequest request = (SendSmsRequest) requestTranslator.translate(smsRequest);
        
        return client.sendSms(request)
                .thenApply(resp -> {
                    SmsResponse response = responseTranslator.translate(resp, smsRequest);
                    log.info("阿里云短信发送完成, result: {}", response.getSendStatus());
                    return response;
                })
                .exceptionally(throwable -> {
                    log.error("调用阿里云服务发送短信出现异常: ", throwable);
                    return SmsResponse.httpError(smsRequest.getUniqueId(), throwable.getMessage());
                });
    }

    @Override
    public boolean isHealthy() {
        // 检查配置是否完整且启用
        return smsConfig.isEnabled()
                && smsConfig.getAccessKeyId() != null 
                && !smsConfig.getAccessKeyId().isEmpty()
                && smsConfig.getAccessKeySecret() != null
                && !smsConfig.getAccessKeySecret().isEmpty();
    }

    @Override
    public String getName() {
        return "aliyun";
    }
    
    @Override
    public int getWeight() {
        return smsConfig.getWeight();
    }
}
