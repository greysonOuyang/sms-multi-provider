package com.sms.service.provider.tianyiyun;

import com.sms.service.provider.config.SmsProviderProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 天翼云
 */
@Configuration
@ConfigurationProperties("sms.provider.ctyun")
public class CtyunSmsProviderProperties implements SmsProviderProperties {
    private String accessKey;

    private String secretKey;

    private String apiUrl;

    @Override
    public String getApiUrl() {
        return apiUrl;
    }

}