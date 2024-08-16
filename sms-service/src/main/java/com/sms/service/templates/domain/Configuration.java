package com.sms.service.templates.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "templates")
public class Configuration {
    /**
     * key1是服务商名称，key2是模版的自定义业务code
     */
//    private Map<String, Map<String, ProviderTemplate>> templates;

    private Map<String, Map<String, Map<String, String>>> templates;

    public Map<String, Map<String, Map<String, String>>> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, Map<String, Map<String, String>>> templates) {
        this.templates = templates;
    }

}

