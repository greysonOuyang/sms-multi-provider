package com.sms.facade;

import com.sms.api.SmsProvider;
import com.sms.loadbalance.LoadBalancer;

import java.util.List;

public class SmsService {
    private final LoadBalancer loadBalancer;

    public SmsService(List<SmsProvider> providerList, LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public void sendSms(String phoneNumber, String message) {
        SmsProvider provider = loadBalancer.getProvider();
        provider.send(phoneNumber, message);
    }
}