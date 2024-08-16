package com.sms.service.templates;

import com.alibaba.fastjson2.JSON;
import com.sms.api.TemplateConfiguration;
import com.sms.api.domain.SmsTemplateEntity;
import com.sms.service.templates.domain.Configuration;
import com.sms.service.templates.domain.ProviderTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
@EnableScheduling
public class ConfigFileTemplateConfiguration implements TemplateConfiguration {

    private Configuration config = new Configuration();

    public SmsTemplateEntity getTemplate(String businessCode, String provider) {
        Map<String, ProviderTemplate> providerTemplateMap = config.getTemplates().get(provider);
        if (providerTemplateMap == null) {
            log.error("Provider not found when read the template config {}", provider);
            throw new RuntimeException("Provider not found when read the template config file: " + provider);
        }

        ProviderTemplate template = providerTemplateMap.get(businessCode);
        if (template == null) {
            log.error("Template not found for messageType: {}", businessCode);
            throw new RuntimeException("Template not found for messageType: " + businessCode);
        }

        SmsTemplateEntity messageTemplate = new SmsTemplateEntity();
        messageTemplate.setTemplateText(template.getContent());
        messageTemplate.setTemplateId(template.getTemplateId());
        messageTemplate.setSmsProvider(provider);
        messageTemplate.setBusinessCode(businessCode);
        return messageTemplate;
    }

    public Object getConfig() {
        return config;
    }
}