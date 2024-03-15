package com.sms.load.balance.strategy;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.SmsProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("roundRobin")
public class RoundRobinLoadBalancerStrategy implements LoadBalancerStrategy {
    private final List<SmsProvider> providers;
    private int index;

    public RoundRobinLoadBalancerStrategy(List<SmsProvider> providers) {
        this.providers = providers;
    }

    public SmsProvider choose() {
        if (index >= providers.size()) {
            index = 0;
        }
        return providers.get(index++);
    }
}