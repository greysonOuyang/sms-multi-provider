package com.sms.load.balance.config;

import com.sms.api.LoadBalancerStrategy;
import com.sms.load.balance.strategy.LoadBalancerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@Configuration
public class BalanceConfig {
   
    @Value("${load.balancer}")
    private String balance;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private LoadBalancerRegistry registry;

    @Bean
    public LoadBalancerStrategy loadBalancer() {
        LoadBalancerStrategy strategy = registry.getStrategy(balance);
        return strategy != null ? strategy : (LoadBalancerStrategy) context.getBean(balance);
    }
}