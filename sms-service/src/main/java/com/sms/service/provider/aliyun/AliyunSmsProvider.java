package com.sms.service.provider.aliyun;

import com.sms.api.SmsProvider;
import org.springframework.stereotype.Service;

@Service("aliyun")
public class AliyunSmsProvider implements SmsProvider {
    @Override
    public void send(String phoneNumber, String message){
        // ... 
    }
}

