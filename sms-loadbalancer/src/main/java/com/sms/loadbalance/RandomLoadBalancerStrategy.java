package com.sms.loadbalance;

import com.sms.api.SmsProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component("random")
public class RandomLoadBalancerStrategy implements LoadBalancerStrategy {
    private final List<SmsProvider> providers;

    public RandomLoadBalancerStrategy(List<SmsProvider> providers) {
        this.providers = providers;
    }

    public SmsProvider choose() {
        int index = new Random().nextInt(providers.size());
        return providers.get(index);
    }
}