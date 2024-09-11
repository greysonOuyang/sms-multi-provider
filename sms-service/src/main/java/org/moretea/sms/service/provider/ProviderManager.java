package org.moretea.sms.service.provider;

import com.sms.api.SmsProvider;
import org.moretea.sms.service.provider.config.ProviderConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
public class ProviderManager {

    @Autowired
    private ProviderConfig providerConfig;

    @Value("${sms.failure.threshold:10}")
    private int failureThreshold;

    @Value("${sms.failure.recovery.time:5}")
    private int recoveryTime;

    /**
     * 单位 s
     */
    @Value("${sms.failure.window.time:60}")
    private int failureWindowTime;

    private ConcurrentHashMap<SmsProvider, ConcurrentLinkedQueue<Long>> failureMap; // 原先的failureCount改为failureMap来记录每次失败的时间
    private final Set<SmsProvider> availableProviders = new HashSet<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        availableProviders.addAll(providerConfig.getAllProviders());
        failureMap = new ConcurrentHashMap<>();
        for (SmsProvider provider : providerConfig.getAllProviders()) {
            failureMap.put(provider, new ConcurrentLinkedQueue<>());
        }
    }

    public void handleFailure(SmsProvider provider) {
        ConcurrentLinkedQueue<Long> failureTimes = failureMap.get(provider);
        failureTimes.offer(System.currentTimeMillis());

        // 移除时间窗口外的失败记录
        while (!failureTimes.isEmpty() && failureTimes.peek() < System.currentTimeMillis() - failureWindowTime * 1000) {
            failureTimes.poll();
        }

        if (failureTimes.size() >= failureThreshold) {
            availableProviders.remove(provider);
            log.error("The {} sms provider send msg failure too many times.Now the status is set unavailable",  provider.getName());
            scheduleRecovery(provider);
        }
    }

    private void scheduleRecovery(SmsProvider provider) {
        scheduler.schedule(() -> {
            if (!provider.isHealthy()) {
                return;
            }
            availableProviders.add(provider);
            failureMap.put(provider, new ConcurrentLinkedQueue<>());
            log.info("Trying to set The {} sms provider is unavailable",  provider.getName());
        }, recoveryTime, TimeUnit.MINUTES);
    }

    public List<SmsProvider> getAvailableProviders() {
        if (availableProviders.isEmpty()) {
            // 打印日志
            log.error("No available providers! you must check the config, or make sure the sms provider is available. System is trying reset now.");

            // 重置所有服务提供者为可用
            availableProviders.addAll(providerConfig.getAllProviders());

            // 清空所有服务提供者的失败记录
            for (ConcurrentLinkedQueue<Long> queue : failureMap.values()) {
                queue.clear();
            }
        }

        return new ArrayList<>(availableProviders);
    }
}