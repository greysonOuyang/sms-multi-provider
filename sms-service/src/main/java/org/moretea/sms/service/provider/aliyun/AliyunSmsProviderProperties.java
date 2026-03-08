package org.moretea.sms.service.provider.aliyun;

import lombok.Data;
import org.moretea.sms.service.provider.config.SmsProviderProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 阿里云短信配置属性
 *
 * @author greyson
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("sms.providers.aliyun")
@Validated
public class AliyunSmsProviderProperties implements SmsProviderProperties {

    /**
     * Access Key ID
     */
    @NotBlank(message = "阿里云 Access Key ID 不能为空")
    private String accessKeyId;

    /**
     * Access Key Secret
     */
    @NotBlank(message = "阿里云 Access Key Secret 不能为空")
    private String accessKeySecret;

    /**
     * API 域名（可选，默认 dysmsapi.aliyuncs.com）
     */
    private String domain = "dysmsapi.aliyuncs.com";

    /**
     * 短信签名（默认签名，可在发送时覆盖）
     */
    @NotBlank(message = "阿里云短信签名不能为空")
    private String signName;

    /**
     * 负载均衡权重（默认 10）
     */
    @NotNull(message = "权重不能为空")
    private int weight = 10;

    /**
     * 是否启用（默认 true）
     */
    @NotNull(message = "enabled 不能为空")
    private boolean enabled = true;

    @Override
    public String getApiUrl() {
        return domain;
    }
}
