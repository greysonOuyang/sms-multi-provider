package org.moretea.sms.service.templates.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "message")
public class MessageConfiguration {

    private String provider;
    private String propertiesPath; // 用于存储配置文件路径的属性
//    private DataSource dataSource; // 如果使用数据库，这可能是一个DataSource Bean

}