package org.moretea.sms.load.balance.strategy.example;

import org.moretea.sms.api.LoadBalancerStrategy;
import org.moretea.sms.api.SmsProvider;

import java.util.List;

/**
 * 拓展负载均衡算法策略实现示例
 * 使用自定义配置时
 * loadbalancer:
 *   strategy: com.example.custom.CustomLoadBalancerStrategy
 */
public class UserDefinedLoadBalancerStrategy implements LoadBalancerStrategy {


    @Override
    public SmsProvider choose(List<SmsProvider> providers) {
        return providers.get(0);
        // 用户自定义的算法逻辑
    }

}