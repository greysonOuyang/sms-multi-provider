package com.sms.service.config;

import com.sms.api.SmsProvider;
import com.sms.service.factory.SmsProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SmsConfig {
    
    @Autowired
    private SmsProviderFactory smsProviderFactory;

    @Bean
    public SmsProviderFactory smsProviderFactory() {
        return new SmsProviderFactory();
    }

    @Bean
    @Primary
    public SmsProvider smsProvider() {
        return smsProviderFactory.getObject();
    }
}