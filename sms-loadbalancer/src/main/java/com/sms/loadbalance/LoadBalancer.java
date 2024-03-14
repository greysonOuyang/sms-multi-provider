package com.sms.loadbalance;

import com.sms.api.SmsProvider;

public interface LoadBalancer {
    SmsProvider getProvider();
}