package org.moretea.sms.service.provider.tianyiyun;

import lombok.Data;
import org.moretea.sms.service.provider.config.SmsProviderProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 天翼云短信配置属性
 *
 * @author greyson
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("sms.providers.ctyun")
@Validated
public class CtyunSmsProviderProperties implements SmsProviderProperties {

    /**
     * Access Key
     */
    @NotBlank(message = "天翼云 Access Key 不能为空")
    private String accessKey;

    /**
     * Secret Key
     */
    @NotBlank(message = "天翼云 Secret Key 不能为空")
    private String secretKey;

    /**
     * API URL（可选，默认 https://sms-global.ctapi.ctyun.cn/sms/api/v1/send）
     */
    private String apiUrl = "https://sms-global.ctapi.ctyun.cn/sms/api/v1/send";
    
    /**
     * 短信签名（默认签名，可在发送时覆盖）
     */
    @NotBlank(message = "天翼云短信签名不能为空")
    private String signName;

    /**
     * 负载均衡权重（默认 5）
     */
    @NotNull(message = "权重不能为空")
    private int weight = 5;

    /**
     * 是否启用（默认 true）
     */
    @NotNull(message = "enabled 不能为空")
    private boolean enabled = true;

    @Override
    public String getApiUrl() {
        return apiUrl;
    }
}
