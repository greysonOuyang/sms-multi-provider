package com.sms.loadbalance;

import com.sms.api.SmsProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component("random")
public class RandomLoadBalancer implements LoadBalancer {
    private final List<SmsProvider> providers;

    public RandomLoadBalancer(List<SmsProvider> providers) {
        this.providers = providers;
    }

    public SmsProvider getProvider() {
        int index = new Random().nextInt(providers.size());
        return providers.get(index);
    }
}