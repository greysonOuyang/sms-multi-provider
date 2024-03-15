package com.sms.api;

import java.util.List;

public interface LoadBalancerStrategy {
    SmsProvider choose(List<SmsProvider> providers);
}