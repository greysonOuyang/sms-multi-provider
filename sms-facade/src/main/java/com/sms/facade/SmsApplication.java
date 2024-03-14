package com.sms.facade;

import com.sms.api.SmsProvider;
import com.sms.loadbalance.LoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:sms.properties")
public class SmsApplication {
    @Autowired
    private ApplicationContext context;

    @Value("${sms.providers}")
    private String providers;

    @Value("${load.balance}")
    private String balance;

    @Bean
    public SmsService smsService() {
        List<SmsProvider> providerList = new ArrayList<>();

        Arrays.stream(providers.split(",")).forEach(name -> {
            providerList.add((SmsProvider)context.getBean(name.trim() + "SmsProvider"));
        });

        LoadBalancer loadBalancer = (LoadBalancer)context.getBean(balance);

        return new SmsService(providerList, loadBalancer);
   }
}