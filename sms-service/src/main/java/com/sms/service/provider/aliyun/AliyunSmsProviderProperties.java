package com.sms.service.provider.aliyun;

import com.sms.service.provider.config.SmsProviderProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 阿里云
 */
@Data
@Configuration
@ConfigurationProperties("sms.provider.aliyun")
public class AliyunSmsProviderProperties implements SmsProviderProperties {
    private String accessKeyId;

    private String accessKeySecret;

    private String domain;

    private String signName;


    @Override
    public String getApiUrl() {
        return domain;
    }
}