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
import java.io.IOException;
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


    @Value("${template.file:classPath:resources/templates/templates.json}")
    private String templateFilePath;

    private volatile Configuration config = new Configuration();

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    @PostConstruct
    private void onPostConstruct() {
        loadTemplatesFromFile();
    }

    @Scheduled(fixedDelayString = "${config.updateInterval:600000}")
    public void refresh() {
        loadTemplatesFromFile();
    }

    public void loadTemplatesFromFile() {
        rwLock.writeLock().lock();
        try {
            Path path = Paths.get(templateFilePath);
            if (!Files.exists(path)) {
                log.error("Template file does not exist: {}", templateFilePath);
                return;
            }
            String content = new String(Files.readAllBytes(path));
            config = JSON.parseObject(content, Configuration.class);
        } catch (IOException e) {
            log.error("Failed to load templates from config file", e);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public SmsTemplateEntity getTemplate(String businessCode, String provider) {
        rwLock.readLock().lock();
        try {
            refresh();

            Map<String, ProviderTemplate> providerTemplateMap = config.getTemplates().get(provider);
            if (providerTemplateMap == null) {
                log.error("Provider not found when read the template config file: {}", provider);
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
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public Object getConfig() {
        return config;
    }
}