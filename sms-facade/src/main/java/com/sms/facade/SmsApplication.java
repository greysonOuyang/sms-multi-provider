package com.sms.facade;

import com.sms.api.SmsProvider;
import com.sms.load.balance.strategy.LoadBalancerStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@Configuration
public class SmsApplication {
   
    @Value("${sms.provider.selected}")
    private String providers;

    @Value("${load.balancer}")
    private String balance;

    @Autowired
    private ApplicationContext context;

    @Bean
    public List<SmsProvider> smsProviders() {
        List<SmsProvider> providerList = new ArrayList<>();

        Arrays.stream(providers.split(",")).forEach(name -> {
            providerList.add((SmsProvider)context.getBean(name.trim() + "SmsProvider"));
        });

        return providerList;
    }

    @Bean
    public LoadBalancerStrategy loadBalancer() {
        return (LoadBalancerStrategy) context.getBean(balance);
    }
}