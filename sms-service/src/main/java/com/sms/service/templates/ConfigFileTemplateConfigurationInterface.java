package com.sms.service.templates;

import com.sms.api.domain.MessageTemplate;
import com.sms.api.TemplateConfigurationInterface;
import com.sms.service.templates.domain.Configuration;
import com.sms.service.templates.domain.ProviderTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.alibaba.fastjson.JSON;


@Service
public class ConfigFileTemplateConfigurationInterface implements TemplateConfigurationInterface {

    @Value("${template.file:classPath:resources/templates/templates.json}")
    private String templateFilePath; // 从配置文件中读取

    private volatile Configuration config = new Configuration(); // 当前加载的配置

    @PostConstruct
    private void onPostConstruct() {
        loadTemplatesFromFile();
    }


    private synchronized void loadTemplatesFromFile() {
        try {
            // TODO 校验文件路径是否存在
            String content = new String(Files.readAllBytes(Paths.get(templateFilePath)));
            Configuration newConfig = JSON.parseObject(content, Configuration.class);
            if (!config.getVersion().equals(newConfig.getVersion())) {
                config = newConfig;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load templates from config file", e);
        }
    }

    // 获取模版方法
    public MessageTemplate getTemplate(String businessCode, String provider) {
        // 先获取到对应的服务商
        Map<String, ProviderTemplate> providerTemplateMap = config.getTemplates().get(provider);
        if (providerTemplateMap == null) {
            throw new RuntimeException("Get Template failed.Cause Provider not found, please check the: " + provider);
        }
        // 再获取对应的消息类型的模版
        ProviderTemplate template = providerTemplateMap.get(businessCode);
        if (template == null) {
            throw new RuntimeException("Template not found for messageType: " + businessCode);
        }
        MessageTemplate messageTemplate = new MessageTemplate();
        messageTemplate.setTemplate(template.getContent());
        messageTemplate.setTemplateId(template.getTemplateId());
        messageTemplate.setProvider(provider);
        messageTemplate.setBusinessCode(businessCode);
        return messageTemplate;
    }
}