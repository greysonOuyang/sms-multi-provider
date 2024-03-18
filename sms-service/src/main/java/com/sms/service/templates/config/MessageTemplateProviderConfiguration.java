package com.sms.service.templates.config;

import com.sms.api.TemplateConfigurationInterface;
import com.sms.service.templates.ConfigFileTemplateConfigurationInterface;
import com.sms.service.templates.DatabaseTemplateConfigurationInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MessageTemplateProviderConfiguration {

    @Bean(name = "serviceProviderAMessageTemplateProvider")
    @ConditionalOnProperty(name = "template.config.type", havingValue = "config-file", matchIfMissing = true)
    public TemplateConfigurationInterface serviceProviderAMessageTemplateProvider() {
        log.info("基础配置——【模板配置形式】，使用文件配置");
        return new ConfigFileTemplateConfigurationInterface();
    }

    @Bean(name = "serviceProviderBMessageTemplateProvider")
    @ConditionalOnProperty(name = "template.config.type", havingValue = "database")
    public TemplateConfigurationInterface serviceProviderBMessageTemplateProvider() {
        log.info("基础配置——【模板配置形式】，使用数据库配置");
        return new DatabaseTemplateConfigurationInterface();
    }
}