package org.moretea.sms.service.provider.tianyiyun;

import org.moretea.sms.service.provider.config.SmsProviderProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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