package org.moretea.sms.service.templates;

import org.moretea.sms.api.TemplateConfiguration;
import org.moretea.sms.api.domain.SmsTemplateEntity;
import org.moretea.sms.service.templates.domain.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@EnableScheduling
public class ConfigFileTemplateConfiguration implements TemplateConfiguration {

    private Configuration config = new Configuration();

    public SmsTemplateEntity getTemplate(String businessCode, String provider) {
        Map<String, Map<String, String>> providerTemplateMap = config.getTemplates().get(provider);
        if (providerTemplateMap == null) {
            log.error("Provider not found when read the template config {}", provider);
            throw new RuntimeException("Provider not found when read the template config file: " + provider);
        }

        Map template = providerTemplateMap.get(businessCode);
        if (template == null) {
            log.error("Template not found for messageType: {}", businessCode);
            throw new RuntimeException("Template not found for messageType: " + businessCode);
        }

        SmsTemplateEntity messageTemplate = new SmsTemplateEntity();
        messageTemplate.setTemplateText((String) template.get("content"));
        messageTemplate.setTemplateId((String) template.get("templateId"));
        messageTemplate.setSmsProvider(provider);
        messageTemplate.setBusinessCode(businessCode);
        return messageTemplate;
    }

    public Object getConfig() {
        return config;
    }
}