package com.sms.load.balance;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.SmsProvider;
import com.sms.service.provider.ProviderManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: greyson
 * Email:  
 * Date: 2024/3/14
 * Time: 17:32
 */
@Slf4j
@Service
public class LoadBalancerManager {

    @Value("${load.balance.isEnabled}")
    private boolean loadBalanceIsEnabled;


    @Autowired
    private LoadBalancerStrategy loadBalancerStrategy;


    @Autowired
    private ProviderManager providerManager;

    public String currentProviderName() {
        return getProvider().getName();
    }

    public SmsProvider currentProvider() {
        if (getProvider() == null) {
            throw new RuntimeException("Can't get a sms provider, please check config or make sure provider is available");
        }
        return getProvider();
    }

    public SmsProvider getProvider() throws RuntimeException {
        List<SmsProvider> availableProviders = providerManager.getAvailableProviders();
        if(availableProviders.isEmpty()) {
            throw new RuntimeException("No available providers.");
        } else {
            if (!loadBalanceIsEnabled) {
                return availableProviders.get(0);
            } else {
                SmsProvider provider = loadBalancerStrategy.choose(availableProviders);
                if (provider.isHealthy()) {
                    return provider;
                } else {
                    providerManager.handleFailure(provider);
                    return getProvider();
                }
            }
        }
    }

}