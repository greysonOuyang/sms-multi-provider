package com.sms.service.factory;

import com.sms.api.SmsProvider;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SmsProviderFactory implements FactoryBean<SmsProvider>, ApplicationContextAware {

    private ApplicationContext applicationContext;
    // 默认的提供商名称
    private String providerName = "aliyun";

    @Override·
    public SmsProvider getObject() {
        return applicationContext.getBean(providerName, SmsProvider.class);
    }

    @Override
    public Class<?> getObjectType() {
        return SmsProvider.class;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}