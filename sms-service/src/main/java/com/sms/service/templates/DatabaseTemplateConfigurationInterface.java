package com.sms.service.templates;

import com.sms.api.MessageTemplate;
import com.sms.api.TemplateConfigurationInterface;
import com.sms.service.config.MessageConfiguration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseTemplateConfigurationInterface implements TemplateConfigurationInterface {

    private DataSource dataSource;

    public DatabaseTemplateConfigurationInterface(MessageConfiguration config) {
        this.dataSource = config.getDataSource();
        // 初始化数据库连接
    }

    @Override
    public MessageTemplate getTemplate(String businessCode, String provider) {
        // 从数据库中读取模板并返回
    }
}