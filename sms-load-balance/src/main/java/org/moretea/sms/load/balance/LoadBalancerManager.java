package org.moretea.sms.load.balance;

import org.moretea.sms.api.LoadBalancerStrategy;
import org.moretea.sms.api.SmsProvider;
import org.moretea.sms.service.provider.ProviderManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡管理器
 *
 * @author greyson
 * @since 1.0.0
 */
@Slf4j
@Service
public class LoadBalancerManager {

    @Autowired
    private org.moretea.sms.api.config.SmsProperties smsProperties;

    private final ApplicationContext context;
    private final Map<String, LoadBalancerStrategy> strategies;

    private LoadBalancerStrategy loadBalancerStrategy;

    @Autowired
    private ProviderManager providerManager;

    private String userConfigStrategyName;

    @Autowired
    public LoadBalancerManager(ApplicationContext context, Map<String, LoadBalancerStrategy> strategies) {
        this.context = context;
        this.strategies = strategies;
    }

    @PostConstruct
    public void init() {
        this.userConfigStrategyName = smsProperties.getLoadBalance().getStrategy();
        if (smsProperties.getLoadBalance().isEnabled()) {
            log.info("loading load-balance strategy: {}", userConfigStrategyName);
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
        SmsProvider provider = getProvider();
        if (provider == null) {
            throw new RuntimeException("Can't get a sms provider, please check config or make sure provider is available");
        }
        return provider;
    }

    public SmsProvider getProvider() throws RuntimeException {
        boolean loadBalanceEnabled = smsProperties.getLoadBalance().isEnabled();
        log.debug("load balance is enabled: {}", loadBalanceEnabled);

        List<SmsProvider> availableProviders = providerManager.getAvailableProviders();
        if (availableProviders.isEmpty()) {
            throw new RuntimeException("No available providers.");
        }

        List<SmsProvider> healthyProviders = new ArrayList<>();
        for (SmsProvider provider : availableProviders) {
            if (provider.isHealthy()) {
                healthyProviders.add(provider);
            } else {
                providerManager.handleFailure(provider);
            }
        }

        if (healthyProviders.isEmpty()) {
            throw new RuntimeException("No healthy providers available.");
        }

        if (!loadBalanceEnabled) {
            return healthyProviders.get(0);
        }

        return loadBalancerStrategy.choose(healthyProviders);
    }

}
