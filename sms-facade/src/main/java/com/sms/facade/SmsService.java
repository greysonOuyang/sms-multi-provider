package com.sms.facade;

import com.sms.api.SmsProvider;
import com.sms.api.SmsProviderException;
import com.sms.loadbalance.LoadBalancerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SmsService {

    @Autowired
    private LoadBalancerManager loadBalancerManager;

    @Value("${sms.failure.retry}")
    private boolean retryOnFailure;

    public void sendSms(String phoneNumber, String message) throws SmsProviderException {
        // 从LoadBalancerManager选择一个服务提供商
        SmsProvider chosenProvider = loadBalancerManager.getProvider();

        //如果所有供应商都不可用
        if (chosenProvider == null) {
            throw new SmsProviderException("All sms providers are unavailable");
        }

        // 尝试发送短信。
        try {
            chosenProvider.sendSms(phoneNumber, message);
        } catch (SmsProviderException ex) {
            // 如果发送失败，根据配置决定是否重试
            if (retryOnFailure) {
                sendSms(phoneNumber, message);
            } else {
                // 记录异常并中断操作
            }
        }
    }
}