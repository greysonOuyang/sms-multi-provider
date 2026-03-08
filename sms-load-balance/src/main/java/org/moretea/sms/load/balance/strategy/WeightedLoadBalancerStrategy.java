package org.moretea.sms.load.balance.strategy;

import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.LoadBalancerStrategy;
import org.moretea.sms.api.SmsProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加权负载均衡策略
 * 
 * 根据服务商的权重进行随机选择，权重越高被选中的概率越大
 * 
 * @author greyson
 * @since 1.0.0
 */
@Slf4j
@Component
public class WeightedLoadBalancerStrategy implements LoadBalancerStrategy {

    private final Map<SmsProvider, Integer> cachedWeights = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public SmsProvider choose(List<SmsProvider> providers) {
        if (providers == null || providers.isEmpty()) {
            throw new IllegalArgumentException("No available providers");
        }
        
        if (providers.size() == 1) {
            return providers.get(0);
        }
        
        // 计算总权重
        int totalWeight = 0;
        for (SmsProvider provider : providers) {
            int weight = provider.getWeight();
            if (weight <= 0) {
                weight = 1; // 最小权重为1
            }
            cachedWeights.put(provider, weight);
            totalWeight += weight;
        }
        
        // 生成随机数并选择服务商
        int randomPoint = random.nextInt(totalWeight);
        int currentWeight = 0;
        
        for (SmsProvider provider : providers) {
            currentWeight += cachedWeights.getOrDefault(provider, 1);
            if (randomPoint < currentWeight) {
                log.debug("加权选择服务商: {}, weight: {}/{}", 
                        provider.getName(), cachedWeights.get(provider), totalWeight);
                return provider;
            }
        }
        
        // 兜底：返回第一个
        return providers.get(0);
    }

    /**
     * 添加服务商（带权重）
     */
    public void addProvider(SmsProvider provider, int weight) {
        cachedWeights.put(provider, weight);
    }

    /**
     * 移除服务商
     */
    public void removeProvider(SmsProvider provider) {
        cachedWeights.remove(provider);
    }

    /**
     * 更新服务商权重
     */
    public void updateWeight(SmsProvider provider, int newWeight) {
        cachedWeights.put(provider, newWeight);
        log.info("更新服务商权重: {} -> {}", provider.getName(), newWeight);
    }
}
