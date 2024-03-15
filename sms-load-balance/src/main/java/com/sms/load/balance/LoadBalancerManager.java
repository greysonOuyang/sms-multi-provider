package com.sms.load.balance;

import com.sms.api.SmsProvider;
import com.sms.load.balance.strategy.LoadBalancerStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/14
 * Time: 17:32
 */

@Service
public class LoadBalancerManager {
    @Value("${load.balance.isEnabled}")
    private boolean loadBalanceIsEnabled;

    @Autowired
    private List<SmsProvider> smsProviders;

    @Autowired
    private LoadBalancerStrategy loadBalancerStrategy; //注入负载均衡策略

    public SmsProvider getProvider() {
        if (!loadBalanceIsEnabled) {
            return smsProviders.get(0); // 如果不启用负载均衡，则直接返回默认供应商
        } else {
            // 启用负载均衡，根据策略选择供应商
            SmsProvider selectedProvider = loadBalancerStrategy.choose();

            if (selectedProvider.isAvailable()) {
                // 如果当前选择的供应商可用，返回该供应商
                return selectedProvider;
            } else {
                // 如果当前供应商不可用，剔除并选择下一个供应商
                smsProviders.remove(selectedProvider);
                // TODO 保留一个或者其他策略
                if (smsProviders.isEmpty()) {
                    return null; // 如果所有供应商都被剔除了，返回null
                } else {
                    return getProvider(); // 递归地选择下一个供应商
                }
            }
        }
    }
}