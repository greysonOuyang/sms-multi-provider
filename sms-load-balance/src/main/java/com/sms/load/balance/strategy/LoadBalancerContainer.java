//package com.sms.load.balance.strategy;
//
//import com.sms.api.LoadBalancerStrategy;
//import com.sms.api.util.ReflectionUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//public class LoadBalancerContainer {
//
//    private static Map<String, LoadBalancerStrategy> strategies = new ConcurrentHashMap<>();
//
//
//
//    public static void register(String name) {
//        try {
//            log.info("Registering load balancer strategy: " + name);
//            LoadBalancerStrategy instance = (LoadBalancerStrategy) ReflectionUtil.createInstance(name + "LoadBalancerStrategy");
//            strategies.putIfAbsent(name, instance);
//            log.info("Registered load balancer strategy success: " + name);
//        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
//                 InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static LoadBalancerStrategy getStrategy(String name) {
//        return strategies.get(name);
//    }
//
//}