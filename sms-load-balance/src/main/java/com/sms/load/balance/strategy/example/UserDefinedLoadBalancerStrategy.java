package com.sms.load.balance.strategy.example;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.SmsProvider;
import com.sms.load.balance.strategy.LoadBalancerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 拓展负载均衡算法策略实现示例
 */
@Service
public class UserDefinedLoadBalancerStrategy implements LoadBalancerStrategy {

    @Autowired
    LoadBalancerRegistry registry;

    @PostConstruct
    public void init() {
        registry.register("userDefined", this);
    }

    @Override
    public SmsProvider choose(List<SmsProvider> providers) {
        return providers.get(0);
        // 用户自定义的算法逻辑
    }
}