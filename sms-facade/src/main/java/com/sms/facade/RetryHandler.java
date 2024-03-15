package com.sms.facade;

import com.sms.facade.domain.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RetryHandler {
    // 使用线程安全的集合存储待重试的任务
    private final Map<Long, SmsRequest> retryMap = new ConcurrentHashMap<>();

    @Value("${sms.retry.limit}")
    private int limit;

    @Value("${sms.retry.enabled}")
    private boolean retryEnabled;

    @Value("${sms.retry.maxRetryTimes}")
    private int maxRetryTimes;

    @Value("${sms.retry.fixedDelay}")
    private long fixedDelay;

    @Async
    public void execute(SmsRequest smsRequest) {
        if(retryEnabled){
            // 快速重试
            long id = Thread.currentThread().getId();
            int attempts = 0;
            while (attempts++ < limit) {
                try {
                    smsRequest.getChosenProvider().sendSms(smsRequest.getPhoneNumber(), smsRequest.getMessage());
                    return;
                } catch (Exception e) {
                    retryMap.put(id, smsRequest);
                }
            }
        }else{
            // 不需要重试
            try{
                smsRequest.getChosenProvider().sendSms(smsRequest.getPhoneNumber(), smsRequest.getMessage());
            }catch (Exception e){
                // 处理发送失败的情况，比如记录到日志
            }
        }
    }

    @Scheduled(fixedDelayString = "${sms.retry.fixedDelay}")
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
                request.getChosenProvider().sendSms(request.getPhoneNumber(), request.getMessage());
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