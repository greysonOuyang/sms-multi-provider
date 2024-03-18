package com.sms.facade.sender;

import com.sms.api.SmsProvider;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsResponse;
import com.sms.api.exception.SmsSendException;
import com.sms.load.balance.LoadBalancerManager;
import com.sms.service.AbstractSmsProvider;
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


    @Async
    public void execute(SmsRequest smsRequest) throws SmsSendException {
        // 获取服务提供商
        SmsProvider chosenProvider = loadBalancerManager.currentProvider();
        if (chosenProvider == null) {
            throw new SmsSendException("All sms providers are unavailable");
        }
        if(retryEnabled){
            // 快速重试
            long id = Thread.currentThread().getId();
            int attempts = 0;
            while (attempts++ < limit) {
                try {
                    chosenProvider.sendSms(smsRequest);
                    return;
                } catch (Exception e) {
                    retryMap.put(id, smsRequest);
                }
            }
        }else{
            // 不需要重试
            try{
                smsRequest.getChosenProvider().sendSms(smsRequest);
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

            try {
                request.getChosenProvider().sendSms(request);
                //如果发送成功，从重试列表中删除
                iterator.remove();
            } catch (Exception ignore){
                // 这里增加重试计数，并检查是否超过最大重试次数
                retryCount++;
                retryCountMap.put(threadId, retryCount);
                if (retryCount >= maxRetryTimes) {
                    // 重试次数超过上限，把这个任务从重试列表中删除
                    iterator.remove();
                }
            }
        }
    }

}