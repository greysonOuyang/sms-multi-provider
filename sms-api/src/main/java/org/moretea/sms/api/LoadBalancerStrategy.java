package com.sms.api;

import java.util.List;

/**
 * 均衡策略接口
 */
public interface LoadBalancerStrategy {
    SmsProvider choose(List<SmsProvider> providers);
}