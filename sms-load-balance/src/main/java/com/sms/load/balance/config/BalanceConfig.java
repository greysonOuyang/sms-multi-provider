//package com.sms.load.balance.config;
//
//import com.sms.api.LoadBalancerStrategy;
//import com.sms.load.balance.strategy.LoadBalancerContainer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//@Slf4j
//@EnableAsync
//@EnableScheduling
//@SpringBootApplication
//@Configuration
//public class BalanceConfig {
//
//    @Value("${load.balancer}")
//    private String balancer;
//
//    @Autowired
//    private ApplicationContext context;
//
//
//    @Bean
//    public LoadBalancerStrategy loadBalancer() {
//        log.info("config loadBalancer: {}", balancer);
////        LoadBalancerContainer.register(balancer);
//        LoadBalancerStrategy strategy = LoadBalancerContainer.getStrategy(balancer);
//        log.info("load loadBalancer {}", strategy != null ? strategy.getClass().getName() + "success": "failed");
//        return strategy != null ? strategy : (LoadBalancerStrategy) context.getBean(balancer);
//    }
//}