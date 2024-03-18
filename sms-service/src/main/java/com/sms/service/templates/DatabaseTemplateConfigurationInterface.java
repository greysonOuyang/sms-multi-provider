package com.sms.service.templates;

import com.sms.api.domain.MessageTemplate;
import com.sms.api.TemplateConfigurationInterface;
import org.springframework.stereotype.Service;

@Service
public class DatabaseTemplateConfigurationInterface implements TemplateConfigurationInterface {


    @Override
    public MessageTemplate getTemplate(String businessCode, String provider) {
        // 从数据库中读取模板并返回
        return null;
    }
}