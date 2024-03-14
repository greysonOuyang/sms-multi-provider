package com.sms.loadbalance;

import com.sms.api.SmsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取均衡策略
 */
@Service
public class SmsProviderManager {
  
    @Autowired
    private Environment env;
  
    private LoadBalanceStrategy loadBalanceStrategy;

    private List<SmsProvider> providers = new ArrayList<>();


    public SmsProviderManager() {
        String[] providerNames = env.getProperty("sms.providers", "").split(",");
        for (String name : providerNames) {
            // Assume SmsProvider has a constructor that accepts a name
            providers.add(new SmsProvider(name));
        }

        String balanceStrategyName = env.getProperty("load.balance.strategy");
        switch (balanceStrategyName) {
            // TODO 默认不进行负载均衡，只取一个，当一个无法使用时，取下一个
            case "WeightedStrategy":
                this.loadBalanceStrategy = new WeightedStrategy();
                break;
            case "RoundRobinStrategy":
                this.loadBalanceStrategy = new RoundRobinStrategy();
                break;
            case "RandomStrategy":
                this.loadBalanceStrategy = new RandomStrategy();
                break;
            default:
                throw new IllegalArgumentException("Invalid load balance strategy: " + balanceStrategyName);
        }
    }

    public synchronized void setLoadBalanceStrategy(LoadBalanceStrategy strategy) {
        this.loadBalanceStrategy = strategy;
    }
  
    public synchronized SmsProvider getBestProvider() {
        if (providers.isEmpty()) {
           throw new RuntimeException("No available provider"); 
        } else {
           return loadBalanceStrategy.select(providers);
        }
    }
  
    public LoadBalanceStrategy getLoadBalanceStrategy() {
        return this.loadBalanceStrategy;
    }

    //... 其他的方法
}