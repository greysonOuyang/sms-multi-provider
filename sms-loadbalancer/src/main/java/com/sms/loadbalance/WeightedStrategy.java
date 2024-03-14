package com.sms.loadbalance;

import com.sms.api.SmsProvider;

import java.util.List;

public class WeightedStrategy implements LoadBalanceStrategy {
    // 假设 SmsProvider 类有一个 getWeight() 方法来获取权重
    @Override
    public SmsProvider select(List<SmsProvider> providers) {
        // 这里可以根据权重实现一个选择算法，例如使用加权轮询算法
        return null;
    }
}