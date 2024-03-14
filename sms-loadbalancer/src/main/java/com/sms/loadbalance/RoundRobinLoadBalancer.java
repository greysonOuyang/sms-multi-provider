package com.sms.loadbalance;

import com.sms.api.SmsProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("roundRobin")
public class RoundRobinLoadBalancer implements LoadBalancer {
    private final List<SmsProvider> providers;
    private int index;

    public RoundRobinLoadBalancer(List<SmsProvider> providers) {
        this.providers = providers;
    }

    public SmsProvider getProvider() {
        if (index >= providers.size()) {
            index = 0;
        }
        return providers.get(index++);
    }
}