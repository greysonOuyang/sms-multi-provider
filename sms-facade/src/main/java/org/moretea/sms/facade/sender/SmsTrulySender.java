package org.moretea.sms.facade.sender;

import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.SmsProvider;
import org.moretea.sms.api.TemplateMessageBuilder;
import org.moretea.sms.api.domain.BatchSmsRequest;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.api.exception.SmsSendException;
import org.moretea.sms.load.balance.LoadBalancerManager;
import org.moretea.sms.service.provider.ProviderManager;
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
 * 短信真实发送器
 * 
 * 负责实际的短信发送逻辑，包括：
 * - 负载均衡选择服务商
 * - 重试机制
 * - 延迟重试队列
 * 
 * @author greyson
 * @since 1.0.0
 */
@Slf4j
@Component
public class SmsTrulySender {

    // 使用线程安全的集合存储待重试的任务
    private final Map<String, RetryTask> retryMap = new ConcurrentHashMap<>();

    @Value("${sms.retry.immediate-limit:3}")
    private int immediateLimit;

    @Value("${sms.retry.enabled:false}")
    private boolean retryEnabled;

    @Value("${sms.retry.max-retry-times:10}")
    private int maxRetryTimes;

    @Value("${sms.history.enabled:false}")
    private boolean historyEnabled;

    @Autowired
    private LoadBalancerManager loadBalancerManager;

    @Autowired
    private ProviderManager providerManager;

    @Autowired(required = false)
    private TemplateMessageBuilder templateMessageBuilder;

    /**
     * 执行短信发送
     *
     * @param smsRequest 短信请求
     * @return 发送结果
     * @throws SmsSendException 发送异常
     */
    public SmsResponse execute(SmsRequest smsRequest) throws SmsSendException {
        // 获取服务提供商
        SmsProvider chosenProvider = loadBalancerManager.currentProvider();
        String providerName = chosenProvider.getName();
        
        log.debug("选择服务商: {}，发送短信到: {}", providerName, smsRequest.getPhoneNumber());

        if (retryEnabled) {
            return executeWithRetry(smsRequest, chosenProvider);
        } else {
            return executeOnce(smsRequest, chosenProvider);
        }
    }

    /**
     * 执行模板短信发送
     *
     * @param smsRequest 短信请求（包含 templateCode）
     * @return 发送结果
     * @throws SmsSendException 发送异常
     */
    public SmsResponse executeTemplate(SmsRequest smsRequest) throws SmsSendException {
        // 如果设置了模板参数但 message 为空，尝试构建消息内容
        if ((smsRequest.getMessage() == null || smsRequest.getMessage().isEmpty()) 
                && smsRequest.getTemplateCode() != null 
                && templateMessageBuilder != null) {
            // 构建消息内容（供日志等使用）
            String message = templateMessageBuilder.buildMessage(
                    smsRequest.getTemplateCode(),
                    smsRequest.getTemplateParams() != null 
                            ? smsRequest.getTemplateParams().values() 
                            : null,
                    null
            );
            smsRequest.setMessage(message);
        }
        
        return execute(smsRequest);
    }

    /**
     * 单次执行（无重试）
     */
    private SmsResponse executeOnce(SmsRequest smsRequest, SmsProvider provider) {
        try {
            CompletableFuture<SmsResponse> future = provider.sendSms(smsRequest);
            SmsResponse response = future.get();
            
            // 记录历史
            if (historyEnabled) {
                saveHistory(smsRequest, response);
            }
            
            return response;
        } catch (Exception e) {
            log.error("短信发送失败, provider: {}, phone: {}, error: {}", 
                    provider.getName(), smsRequest.getPhoneNumber(), e.getMessage());
            
            // 标记服务商失败
            providerManager.handleFailure(provider);
            
            return SmsResponse.httpError(smsRequest.getUniqueId(), e.getMessage());
        }
    }

    /**
     * 带立即重试的执行
     */
    private SmsResponse executeWithRetry(SmsRequest smsRequest, SmsProvider initialProvider) {
        String uniqueId = smsRequest.getUniqueId();
        SmsProvider currentProvider = initialProvider;
        
        int attempts = 0;
        while (attempts < immediateLimit) {
            attempts++;
            
            try {
                CompletableFuture<SmsResponse> future = currentProvider.sendSms(smsRequest);
                SmsResponse response = future.get();
                
                // 发送成功
                if (Boolean.TRUE.equals(response.getSendStatus())) {
                    if (historyEnabled) {
                        saveHistory(smsRequest, response);
                    }
                    return response;
                }
                
                // 发送失败但请求成功，不重试
                log.warn("短信发送被拒绝, provider: {}, attempt: {}/{}, response: {}", 
                        currentProvider.getName(), attempts, immediateLimit, response.getMsg());
                return response;
                
            } catch (Exception e) {
                log.error("短信发送异常, provider: {}, attempt: {}/{}, error: {}", 
                        currentProvider.getName(), attempts, immediateLimit, e.getMessage());
                
                // 标记当前服务商失败
                providerManager.handleFailure(currentProvider);
                
                // 如果不是最后一次尝试，切换服务商重试
                if (attempts < immediateLimit) {
                    currentProvider = loadBalancerManager.currentProvider();
                    log.info("切换到服务商重试: {}", currentProvider.getName());
                }
            }
        }
        
        // 立即重试全部失败，加入延迟重试队列
        if (maxRetryTimes > immediateLimit) {
            RetryTask retryTask = new RetryTask(smsRequest, immediateLimit);
            retryMap.put(uniqueId, retryTask);
            log.info("短信加入延迟重试队列, uniqueId: {}, 当前重试次数: {}", uniqueId, immediateLimit);
        }
        
        return SmsResponse.httpError(uniqueId, "发送失败，已加入重试队列");
    }

    /**
     * 批量执行
     */
    @Async
    public void executeMulti(BatchSmsRequest batchRequest) {
        if (batchRequest.getTargets() == null) {
            return;
        }
        
        batchRequest.getTargets().forEach(target -> {
            target.setScheduleTime(batchRequest.getScheduleTime());
            target.setTemplateCode(batchRequest.getTemplateCode());
            target.setSignName(batchRequest.getSignName());
            try {
                executeTemplate(target);
            } catch (Exception e) {
                log.error("批量发送中单个失败, phone: {}", target.getPhoneNumber(), e);
            }
        });
    }

    /**
     * 定时重试失败的任务
     */
    @Scheduled(fixedDelayString = "${sms.retry.fixed-delay:5000}")
    public void retryFailed() {
        if (retryMap.isEmpty()) {
            return;
        }
        
        log.debug("开始执行延迟重试，当前队列大小: {}", retryMap.size());
        
        Iterator<Map.Entry<String, RetryTask>> iterator = retryMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, RetryTask> entry = iterator.next();
            String uniqueId = entry.getKey();
            RetryTask task = entry.getValue();
            
            // 检查是否超过最大重试次数
            if (task.getRetryCount() >= maxRetryTimes) {
                log.warn("短信超过最大重试次数, uniqueId: {}", uniqueId);
                iterator.remove();
                continue;
            }
            
            try {
                SmsProvider provider = loadBalancerManager.currentProvider();
                log.debug("延迟重试, uniqueId: {}, provider: {}, attempt: {}", 
                        uniqueId, provider.getName(), task.getRetryCount() + 1);
                
                CompletableFuture<SmsResponse> future = provider.sendSms(task.getRequest());
                SmsResponse response = future.get();
                
                if (Boolean.TRUE.equals(response.getSendStatus())) {
                    log.info("延迟重试成功, uniqueId: {}", uniqueId);
                    if (historyEnabled) {
                        saveHistory(task.getRequest(), response);
                    }
                    iterator.remove();
                } else {
                    task.incrementRetryCount();
                    providerManager.handleFailure(provider);
                }
            } catch (Exception e) {
                log.error("延迟重试失败, uniqueId: {}", uniqueId, e);
                task.incrementRetryCount();
            }
        }
    }

    /**
     * 保存发送历史
     */
    private void saveHistory(SmsRequest request, SmsResponse response) {
        // TODO: 实现历史记录保存
        log.debug("保存发送历史, uniqueId: {}", request.getUniqueId());
    }

    /**
     * 重试任务包装类
     */
    private static class RetryTask {
        private final SmsRequest request;
        private int retryCount;
        
        RetryTask(SmsRequest request, int initialCount) {
            this.request = request;
            this.retryCount = initialCount;
        }
        
        SmsRequest getRequest() {
            return request;
        }
        
        int getRetryCount() {
            return retryCount;
        }
        
        void incrementRetryCount() {
            this.retryCount++;
        }
    }
}
