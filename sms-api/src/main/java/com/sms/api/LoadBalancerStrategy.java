package com.sms.api;

public interface LoadBalancerStrategy {
    SmsProvider choose();
}