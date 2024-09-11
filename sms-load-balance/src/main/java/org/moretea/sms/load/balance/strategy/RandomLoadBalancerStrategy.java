package org.moretea.sms.load.balance.strategy;

import org.moretea.sms.api.LoadBalancerStrategy;
import org.moretea.sms.api.SmsProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class RandomLoadBalancerStrategy implements LoadBalancerStrategy {

    private final Random random = new Random();

    @Override
    public SmsProvider choose(List<SmsProvider> providers) {
        int pos = random.nextInt(providers.size());
        return providers.get(pos);
    }
}