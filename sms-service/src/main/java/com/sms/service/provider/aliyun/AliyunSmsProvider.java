package com.sms.service.provider.aliyun;

import com.sms.api.SmsProvider;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service("aliyunSmsProvider")
public class AliyunSmsProvider implements SmsProvider {
    public AliyunSmsProvider(Properties properties) {
        // 这个构造函数中，你可以从properties对象中获取配置项，完成服务提供商的初始化
    }
    @Override
    public void send(String phoneNumber, String message){
        // ... 
    }
}

