package org.moretea.sms.load.balance.strategy;

import org.moretea.sms.api.LoadBalancerStrategy;
import org.moretea.sms.api.SmsProvider;
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