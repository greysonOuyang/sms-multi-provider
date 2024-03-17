package com.sms.load.balance.strategy;

import com.sms.api.LoadBalancerStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoadBalancerRegistry {
    private Map<String, LoadBalancerStrategy> strategies = new ConcurrentHashMap<>();

    public void register(String name, LoadBalancerStrategy strategy) {
        strategies.putIfAbsent(name, strategy);
    }

    public LoadBalancerStrategy getStrategy(String name) {
        return strategies.get(name);
    }
}