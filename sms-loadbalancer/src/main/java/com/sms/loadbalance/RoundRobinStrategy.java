package com.sms.loadbalance;

import com.sms.api.SmsProvider;

import java.util.List;

public class RoundRobinStrategy implements LoadBalanceStrategy {
    private int lastSelected = -1;

    @Override
    public SmsProvider select(List<SmsProvider> providers) {
        lastSelected = (lastSelected + 1) % providers.size();
        return providers.get(lastSelected);
    }
}