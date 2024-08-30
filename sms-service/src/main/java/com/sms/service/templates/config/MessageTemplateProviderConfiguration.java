package com.sms.service.templates.config;

import com.sms.api.TemplateConfiguration;
import com.sms.service.templates.ConfigFileTemplateConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MessageTemplateProviderConfiguration {

    @Bean(name = "serviceProviderAMessageTemplateProvider")
    @ConditionalOnProperty(name = "template.config.type", havingValue = "config", matchIfMissing = true)
    public TemplateConfiguration serviceProviderAMessageTemplateProvider() {
        log.info("基础配置——【模板配置形式】，使用文件配置");
        return new ConfigFileTemplateConfiguration();
    }

//    @Bean(name = "serviceProviderBMessageTemplateProvider")
//    @ConditionalOnProperty(name = "template.config.type", havingValue = "database")
//    public TemplateConfiguration serviceProviderBMessageTemplateProvider() {
//        log.info("基础配置——【模板配置形式】，使用数据库配置");
//        return new DatabaseTemplateConfiguration();
//    }
}