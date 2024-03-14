package com.sms.loadbalance;

import com.sms.api.SmsProvider;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements LoadBalanceStrategy {
    private Random rand = new Random();

    @Override
    public SmsProvider select(List<SmsProvider> providers) {
        int idx = rand.nextInt(providers.size());
        return providers.get(idx);
    }
}