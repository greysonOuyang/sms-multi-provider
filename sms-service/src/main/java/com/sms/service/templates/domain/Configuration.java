package com.sms.service.templates.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Configuration {
    /**
     * key1是服务商名称，key2是模版的自定义业务code
     */
    private Map<String, Map<String, ProviderTemplate>> templates;
}

