package com.sms.loadbalance;

import com.sms.api.SmsProvider;

import java.util.List;

public interface LoadBalanceStrategy {
    SmsProvider select(List<SmsProvider> providers);
}





