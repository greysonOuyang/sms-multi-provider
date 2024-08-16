package com.sms.load.balance.strategy;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.SmsProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinLoadBalancerStrategy implements LoadBalancerStrategy {

    private final AtomicInteger position = new AtomicInteger();

    @Override
    public SmsProvider choose(List<SmsProvider> providers) {
        int pos = Math.abs(position.getAndIncrement());
        return providers.get(pos % providers.size());
    }
}