package com.sms.service.provider.tianyiyun;

import com.sms.service.AbstractSmsProvider;
import com.sms.service.config.SmsProviderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ctyunSmsProvider")
public class CtyunSmsProvider extends AbstractSmsProvider {
    private final SmsProviderProperties properties;

    @Autowired
    public CtyunSmsProvider(CtyunSmsProviderProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void sendSmsInternal(String phoneNumber, String message) throws Exception {

    }

    @Override
    public boolean isHealthy() {
        return true;
    }
}