package com.sms.facade;

import com.sms.facade.domain.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Async
    public void execute(SmsRequest smsRequest) {
        if(retryEnabled){
            // 需要重试
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

    @Scheduled(fixedRateString = "${sms.retry.interval}")
    public void retryFailed() {
        retryMap.forEach((id, smsRequest) -> {
            try {
                smsRequest.getChosenProvider().sendSms(smsRequest.getPhoneNumber(), smsRequest.getMessage());
                retryMap.remove(id);
            } catch (Exception ignored) {
            }
        });
    }
}