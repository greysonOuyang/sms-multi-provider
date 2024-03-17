package com.sms.service.config;

import com.sms.api.TemplateConfigurationInterface;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageTemplateProviderConfiguration {

    @Bean(name = "serviceProviderAMessageTemplateProvider")
    @ConditionalOnProperty(prefix = "message", name = "provider", havingValue = "ServiceProviderA")
    public TemplateConfigurationInterface serviceProviderAMessageTemplateProvider() {
        return new ServiceProviderAMessageTemplateProvider();
    }

    @Bean(name = "serviceProviderBMessageTemplateProvider")
    @ConditionalOnProperty(prefix = "message", name = "provider", havingValue = "ServiceProviderB")
    public TemplateConfigurationInterface serviceProviderBMessageTemplateProvider() {
        return new ServiceProviderBMessageTemplateProvider();
    }
}