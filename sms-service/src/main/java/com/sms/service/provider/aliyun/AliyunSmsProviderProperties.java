package com.sms.service.provider.aliyun;

import com.sms.service.config.SmsProviderProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:aliyun.properties")
public class AliyunSmsProviderProperties implements SmsProviderProperties {
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${apiUrl}")
    private String apiUrl;


    @Override
    public String getApiUrl() {
        return apiUrl;
    }
}