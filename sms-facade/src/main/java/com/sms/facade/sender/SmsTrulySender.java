package com.sms.facade.sender;

import com.sms.api.SmsProvider;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsResponse;
import com.sms.api.exception.SmsSendException;
import com.sms.load.balance.LoadBalancerManager;
import com.sms.service.provider.ProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 短信发送的第二层抽象服务，包含了重试、负载均衡、历史记录等逻辑，在此处以配置的方式做插件化处理，下一层抽象是真正的服务商发送逻辑
 */
@Component
public class SmsTrulySender {
    // 使用线程安全的集合存储待重试的任务
    private final Map<Long, SmsRequest> retryMap = new ConcurrentHashMap<>();

    @Value("${sms.retry.limit:3}")
    private int limit;

    @Value("${sms.retry.enabled: false}")
    private boolean retryEnabled;

    @Value("${sms.retry.maxRetryTimes:10}")
    private int maxRetryTimes;

    @Value("${sms.history.enabled}")
    private String historyEnabled;
    // TODO 加入历史记录

    @Autowired
    private LoadBalancerManager loadBalancerManager;

    @Autowired
    private ProviderManager providerManager;

    @Async
    public void executeMulti(BatchSmsRequest smsRequest)  {
        smsRequest.getTargets().forEach(target -> {
            target.setScheduleTime(smsRequest.getScheduleTime());
            target.setTemplateCode(smsRequest.getTemplateCode());
            target.setSignName(smsRequest.getSignName());
            try {
                execute(target);
            } catch (SmsSendException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Async
    public void execute(SmsRequest smsRequest) throws SmsSendException {
        // 获取服务提供商
        SmsProvider chosenProvider = loadBalancerManager.currentProvider();
        if(retryEnabled){
            // 快速重试
            long id = Thread.currentThread().getId();
            int attempts = 0;
            while (attempts++ < limit) {
                try {
                    // TODO 处理历史记录等
                    CompletableFuture<SmsResponse> future = chosenProvider.sendSms(smsRequest);
                    // 发送成功，从retryMap中移除此任务
                    retryMap.remove(id);
                    return;
                } catch (Exception e) {
                    providerManager.handleFailure(chosenProvider);
                    retryMap.put(id, smsRequest);
                    if (attempts >= limit) {
                        // 如果超过最大重试次数，从retryMap中移除此任务
                        retryMap.remove(id);
                    }
                }
            }
        } else{
            // 不需要重试
            try{
                chosenProvider.sendSms(smsRequest);
            }catch (Exception e){
                // 处理发送失败的情况，比如记录到日志
            }
        }
    }

    @Scheduled(fixedDelayString = "${sms.retry.fixedDelay:5000}")
    public void retryFailed() {
        // 这个map用来记录每一个任务的重试次数
        Map<Long, Integer> retryCountMap = new HashMap<>();

        Iterator<Map.Entry<Long, SmsRequest>> iterator = retryMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Long, SmsRequest> entry = iterator.next();
            long threadId = entry.getKey();
            SmsRequest request = entry.getValue();

            int retryCount = retryCountMap.getOrDefault(threadId, 0);
            // 增加重试计数
            retryCount++;
            if (retryCount >= maxRetryTimes) {
                // 重试次数超过上限，把这个任务从重试列表中删除
                iterator.remove();
                continue;
            }

            try {
                // 重新选择服务商来发送
                loadBalancerManager.currentProvider().sendSms(request);
                // 如果发送成功，从重试列表中删除
                iterator.remove();
            } catch (Exception ignore){
                // 未发送成功，更新重试次数
                retryCountMap.put(threadId, retryCount);
            }
        }
    }

}