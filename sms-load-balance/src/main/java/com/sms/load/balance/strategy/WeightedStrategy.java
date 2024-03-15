package com.sms.load.balance.strategy;

import com.sms.api.SmsProvider;

public class WeightedStrategy implements LoadBalancerStrategy {
    // 假设 SmsProvider 类有一个 getWeight() 方法来获取权重

    @Override
    public SmsProvider choose() {
        return null;
    }
}