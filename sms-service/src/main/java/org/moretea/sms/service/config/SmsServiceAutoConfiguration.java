package org.moretea.sms.service.config;

import org.moretea.sms.api.SmsProvider;
import org.moretea.sms.service.provider.ProviderManager;
import org.moretea.sms.service.provider.aliyun.AliyunSmsProvider;
import org.moretea.sms.service.provider.aliyun.AliyunSmsProviderProperties;
import org.moretea.sms.service.provider.aliyun.AliyunSmsRequestTranslator;
import org.moretea.sms.service.provider.aliyun.AliyunSmsResponseTranslator;
import org.moretea.sms.service.provider.config.ProviderConfig;
import org.moretea.sms.service.provider.tianyiyun.CtyunSmsProvider;
import org.moretea.sms.service.provider.tianyiyun.CtyunSmsProviderProperties;
import org.moretea.sms.service.provider.tianyiyun.CtyunSmsRequestTranslator;
import org.moretea.sms.service.provider.tianyiyun.CtyunSmsResponseTranslator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 短信服务自动配置类
 * 
 * @author greyson
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnClass(SmsProvider.class)
@EnableConfigurationProperties({
    AliyunSmsProviderProperties.class,
    CtyunSmsProviderProperties.class
})
public class SmsServiceAutoConfiguration {

    /**
     * 阿里云短信服务提供者配置
     */
    @Configuration
    @ConditionalOnClass(name = "com.aliyun.sdk.service.dysmsapi20170525.AsyncClient")
    @ConditionalOnProperty(prefix = "sms.providers.aliyun", name = "access-key-id")
    static class AliyunSmsConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public AliyunSmsRequestTranslator aliyunSmsRequestTranslator() {
            return new AliyunSmsRequestTranslator();
        }

        @Bean
        @ConditionalOnMissingBean
        public AliyunSmsResponseTranslator aliyunSmsResponseTranslator() {
            return new AliyunSmsResponseTranslator();
        }

        @Bean(name = "aliyunSmsProvider")
        @ConditionalOnMissingBean(name = "aliyunSmsProvider")
        public AliyunSmsProvider aliyunSmsProvider() {
            return new AliyunSmsProvider();
        }
    }

    /**
     * 天翼云短信服务提供者配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "sms.providers.ctyun", name = "access-key")
    static class CtyunSmsConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public CtyunSmsRequestTranslator ctyunSmsRequestTranslator() {
            return new CtyunSmsRequestTranslator();
        }

        @Bean
        @ConditionalOnMissingBean
        public CtyunSmsResponseTranslator ctyunSmsResponseTranslator() {
            return new CtyunSmsResponseTranslator();
        }

        @Bean(name = "ctyunSmsProvider")
        @ConditionalOnMissingBean(name = "ctyunSmsProvider")
        public CtyunSmsProvider ctyunSmsProvider() {
            return new CtyunSmsProvider();
        }
    }

    /**
     * Provider 配置
     */
    @Configuration
    @Import(ProviderConfig.class)
    static class ProviderConfiguration {
    }

    /**
     * Provider 管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public ProviderManager providerManager() {
        return new ProviderManager();
    }
}