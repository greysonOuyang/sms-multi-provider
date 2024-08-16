package com.sms.load.balance.strategy;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.SmsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class WeightedLoadBalancerStrategy implements LoadBalancerStrategy {

    @Autowired
    private Environment env;

    private final Map<SmsProvider, Integer> weights = new ConcurrentHashMap<>();

    @Override
    public SmsProvider choose(List<SmsProvider> providers) {
        for (SmsProvider provider : providers) {
            if (!weights.containsKey(provider)) {
                String propertyName = "sms.providers." + provider.getName() +".weight";
                String weightStr = env.getProperty(propertyName);
                if (weightStr != null) {
                    weights.put(provider, Integer.parseInt(weightStr));
                } else {
                    weights.put(provider, 1);
                }
            }
        }
        // 随机生成一个位于[0, totalWeight)区间的随机数randomPoint，接着遍历权重表，从randomPoint中减去每个服务商的权重直到randomPoint小于0，
        // 返回当前权重导致randomPoint小于0的服务商

        int totalWeight = 0;
        for (Integer weight : weights.values()) {
            totalWeight += weight;
        }

        int randomPoint = new Random().nextInt(totalWeight);
        for (Map.Entry<SmsProvider, Integer> entry : weights.entrySet()) {
            randomPoint -= entry.getValue();
            if (randomPoint < 0) {
                return entry.getKey();
            }
        }
        // TODO 测试权重为0会如何

        // 仅会在所有服务provider权重为0或负数时才会走到这里，并返回第一个服务商
        return providers.get(0);
    }

    // 添加服务商
    public void addProvider(SmsProvider provider, int weight) {
        weights.put(provider, weight);
    }

    public void removeProvider(SmsProvider provider) {
        weights.remove(provider);
    }

    // 修改供应商权重的方法
    public void updateWeight(SmsProvider provider, int newWeight) {
        weights.put(provider, newWeight);
    }
}