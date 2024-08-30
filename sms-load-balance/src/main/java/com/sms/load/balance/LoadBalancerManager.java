package com.sms.load.balance;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.SmsProvider;
import com.sms.service.provider.ProviderManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Author: greyson
 * Email:  
 * Date: 2024/3/14
 * Time: 17:32
 */
@Slf4j
@Service
public class LoadBalancerManager {

    @Value("${sms.load-balance.isEnabled}")
    private boolean loadBalanceIsEnabled;

    private final ApplicationContext context;
    private final Map<String, LoadBalancerStrategy> strategies;

    private LoadBalancerStrategy loadBalancerStrategy;


    @Autowired
    private ProviderManager providerManager;

    @Value("${sms.load-balancer:random}")
    private String userConfigStrategyName;

    @Autowired
    public LoadBalancerManager(ApplicationContext context, Map<String, LoadBalancerStrategy> strategies) {
        this.context = context;
        this.strategies = strategies;
    }

    @PostConstruct
    public void init() {
        if (loadBalanceIsEnabled) {
            log.info("loading load-balance strategy...");
            loadBalancerStrategy = getStrategy();
        }
    }

    public LoadBalancerStrategy getStrategy() {
        String beanName = userConfigStrategyName + "LoadBalancerStrategy";
        if (strategies.containsKey(beanName)) {
            return strategies.get(beanName);
        }

        try {
            Class<?> clazz = Class.forName(userConfigStrategyName);
            LoadBalancerStrategy strategy = (LoadBalancerStrategy) context.getAutowireCapableBeanFactory().createBean(clazz);
            strategies.put(userConfigStrategyName, strategy);
            return strategy;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown strategy: " + userConfigStrategyName, e);
        }
    }


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
        log.info("load balance is enabled: {}", loadBalanceIsEnabled);
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