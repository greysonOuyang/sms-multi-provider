package com.sms.service.templates;

import com.sms.api.MessageTemplate;
import com.sms.api.TemplateConfigurationInterface;
import com.sms.service.config.MessageConfiguration;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class ConfigFileTemplateConfigurationInterface implements TemplateConfigurationInterface {

    private Properties properties;

    public ConfigFileTemplateConfigurationInterface(MessageConfiguration config) {
        String path = config.getPropertiesPath();
        // 读取配置文件，并初始化properties
    }

    @Override
    public MessageTemplate getTemplate(String businessCode, String provider) {
        // 从properties中读取模板并返回
    }
}