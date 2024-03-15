package com.sms.service.provider.aliyun;

import com.sms.service.AbstractSmsProvider;
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

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public String getName() {
        return "aliyun";
    }
}

