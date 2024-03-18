package com.sms.load.balance;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.SmsProvider;
import com.sms.api.UnavailableHandler;
import com.sms.service.provider.config.ProviderConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/14
 * Time: 17:32
 */
@Slf4j
@Service
public class LoadBalancerManager {

    @Value("${load.balance.isEnabled}")
    private boolean loadBalanceIsEnabled;

    // TODO 需要有策略，例如多少分钟内失效多少次则不可用
    @Value("${sms.base.provider.failure.times:5}")
    private int providerFailureTimes;

    @Value("${sms.base.provider.recovery.time:5}")
    private int providerFailureRecovery;

    @Autowired
    private LoadBalancerStrategy loadBalancerStrategy;

    @Autowired
    private ProviderConfig providerConfig;

    /**
     * 可用服务商
     */
    private List<SmsProvider> availableProviders;
    /**
     * 服务商 可用标记
     */
    private Map<SmsProvider, Boolean> availabilityMap;
    /**
     * 失败计数
     */
    private Map<SmsProvider, Integer> failCounter;

    // 创建一个调度线程池
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public void failInc(SmsProvider provider) {
        // 失败计数加一
        int failCount = failCounter.get(provider) + 1;
        failCounter.put(provider, failCount);
    }

    @PostConstruct
    public void init() {
        availableProviders = providerConfig.getAllProviders();
        availabilityMap = providerConfig.getAllProviders().stream().collect(Collectors.toMap(Function.identity(), provider -> true));
        failCounter = providerConfig.getAllProviders().stream().collect(Collectors.toMap(Function.identity(), provider -> 0));
    }

    public String currentProviderName() {
        return getProvider().getName();
    }

    public SmsProvider currentProvider() {
        if (getProvider() == null) {
            throw new RuntimeException("Can't get a sms provider, please check config or make sure provider is available");
        }
        return getProvider();
    }

    public SmsProvider getProvider() {
        if (!loadBalanceIsEnabled) {
            return providerConfig.getFirstProvider();
        }

        if (!availableProviders.isEmpty()) {
            SmsProvider provider = loadBalancerStrategy.choose(availableProviders);
            if (provider.isHealthy()) {
                return provider;
            } else {
                // 服务不可用 TODO
                failInc(provider);
                return getProvider();
            }
        } else {
            resetProviders();
            return getProvider();
        }
    }

    // TODO 对服务商进行发送失败统计 超过用户设置的阈值则置为失效，配置例如：time: 1s, count:10, 表示当1s内超过了10次失败，则该服务商不可用
    //  考虑下该怎么实现
    private void handleFailure(SmsProvider provider) {
        int failCount = failCounter.get(provider);
        if (failCount >= providerFailureTimes) {
            provider.setAvailable(false);
            availableProviders.remove(provider);
            availabilityMap.put(provider, false);
            // 将此服务商放入定时器，5分钟后回复健康
            scheduleRecovery(provider);
        }
    }

    /**
     * 如果没有可用的服务，那就全部恢复成可用状态
     */
    private void resetProviders() {
        failCounter.replaceAll((k, v) -> 0);
        availableProviders = new ArrayList<>(providerConfig.getAllProviders());
        availabilityMap.replaceAll((k, v) -> true);
    }

    // 在5分钟后将服务商从不健康状态恢复为健康状态
    private void scheduleRecovery(SmsProvider provider) {
        Runnable runnable = () -> {
            if (!provider.isHealthy()) {
                availabilityMap.put(provider, true);
                failCounter.put(provider, 0);
                availableProviders.add(provider);
            }
        };
        scheduler.schedule(runnable, providerFailureRecovery, TimeUnit.MINUTES);
    }

}