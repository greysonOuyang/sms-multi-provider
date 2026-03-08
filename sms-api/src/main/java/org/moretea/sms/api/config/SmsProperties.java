package org.moretea.sms.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * SMS Multi Provider 全局配置属性
 *
 * 配置负载均衡、重试等全局参数
 *
 * @author greyson
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
@Validated
public class SmsProperties {

    /**
     * 负载均衡配置
     */
    private LoadBalanceProperties loadBalance = new LoadBalanceProperties();

    /**
     * 重试配置
     */
    private RetryProperties retry = new RetryProperties();

    /**
     * 负载均衡配置
     */
    @Data
    public static class LoadBalanceProperties {

        /**
         * 是否启用负载均衡（默认 true）
         */
        private boolean enabled = true;

        /**
         * 负载均衡策略：random（随机）、round-robin（轮询）、weighted（加权）
         * 默认 weighted
         */
        private String strategy = "weighted";
    }

    /**
     * 重试配置
     */
    @Data
    public static class RetryProperties {

        /**
         * 是否启用重试（默认 true）
         */
        private boolean enabled = true;

        /**
         * 立即重试次数（失败后立即换服务商重试）
         * 默认 3
         */
        @Min(0)
        @Max(10)
        @NotNull
        private int immediateLimit = 3;

        /**
         * 最大重试次数（包括延迟重试）
         * 默认 10
         */
        @Min(0)
        @Max(50)
        @NotNull
        private int maxRetryTimes = 10;

        /**
         * 延迟重试间隔（毫秒）
         * 默认 5000ms
         */
        @Min(1000)
        @Max(60000)
        @NotNull
        private int fixedDelay = 5000;
    }
}
