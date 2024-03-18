package com.sms.service.provider.aliyun;

import com.sms.service.provider.config.SmsProviderProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:aliyun.properties")
public class AliyunSmsProviderProperties implements SmsProviderProperties {
    @Value("${accessKeyId}")
    private String accessKeyId;

    @Value("${accessKeySecret}")
    private String accessKeySecret;

    @Value("${domain}")
    private String domain;

    @Value("${sms.aliyun.signName}")
    private String signName;


    @Override
    public String getApiUrl() {
        return domain;
    }
}