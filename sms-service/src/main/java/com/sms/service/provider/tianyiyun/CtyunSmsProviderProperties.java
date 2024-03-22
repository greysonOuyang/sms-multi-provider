package com.sms.service.provider.tianyiyun;

import com.sms.service.provider.config.SmsProviderProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 天翼云
 */
@Configuration
@PropertySource("classpath:ctyun.properties")
public class CtyunSmsProviderProperties implements SmsProviderProperties {
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