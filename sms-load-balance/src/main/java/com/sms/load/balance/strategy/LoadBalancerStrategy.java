package com.sms.load.balance.strategy;

import com.sms.api.SmsProvider;

public interface LoadBalancerStrategy {
    SmsProvider choose();
}