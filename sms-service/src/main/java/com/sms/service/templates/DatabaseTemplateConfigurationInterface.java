package com.sms.service.templates;

import com.sms.api.MessageTemplate;
import com.sms.api.TemplateConfigurationInterface;
import com.sms.service.templates.config.MessageConfiguration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseTemplateConfigurationInterface implements TemplateConfigurationInterface {


    @Override
    public MessageTemplate getTemplate(String businessCode, String provider) {
        // 从数据库中读取模板并返回
        return null;
    }
}