package com.sms.service.provider.aliyun;

import com.sms.api.AbstractSmsProvider;
import com.sms.api.SmsProvider;
import com.sms.service.config.SmsProviderProperties;
import com.sms.service.provider.tianyiyun.CtyunSmsProviderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("aliyunSmsProvider")
public class AliyunSmsProvider extends AbstractSmsProvider {

    private final AliyunSmsProviderProperties properties;

    @Autowired
    public AliyunSmsProvider(AliyunSmsProviderProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void sendSmsInternal(String phoneNumber, String message) throws Exception {

    }
}

