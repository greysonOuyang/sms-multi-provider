package com.sms.facade;

import cn.hutool.core.collection.CollectionUtil;
import com.sms.api.TemplateConfigurationInterface;
import com.sms.api.SmsProvider;
import com.sms.api.TemplateMessageBuilder;
import com.sms.exception.SmsProviderException;
import com.sms.facade.domain.SmsRequest;
import com.sms.load.balance.LoadBalancerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SmsSender {

    //获取发送短信是否重试的配置
    @Value("${sms.retry}")
    private boolean enableRetry;

    //获取发送短信是否使用队列的配置
    @Value("${sms.queue}")
    private boolean enableQueue;

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private LoadBalancerManager loadBalancerManager;

    @Autowired
    private KafkaTemplate<String, SmsRequest> kafkaTemplate;

    @Autowired
    private RetryHandler retryHandler;

    @Autowired
    private TemplateMessageBuilder templateMessageBuilder;

    public void sendSms(String phoneNumber, String message) throws SmsProviderException {
        // 获取服务提供商
        SmsProvider chosenProvider = loadBalancerManager.currentProvider();
        if (chosenProvider == null) {
            throw new SmsProviderException("All sms providers are unavailable");
        }

        SmsRequest request = new SmsRequest(phoneNumber, message, chosenProvider);
        if (enableQueue) {
            // 启用队列发送
            kafkaTemplate.send(topic, request);
        } else {
            // 直接发送
            retryHandler.execute(request);
        }
    }

    public void sendByTemplate(String businessCode, String phoneNumber, Collection<?> params) throws SmsProviderException {
        // 获取服务提供商
        SmsProvider chosenProvider = loadBalancerManager.currentProvider();
        String message = templateMessageBuilder.buildMessage(businessCode, params, chosenProvider.getName());

        SmsRequest request = new SmsRequest(phoneNumber, message, chosenProvider);
        if (enableQueue) {
            // 启用队列发送
            kafkaTemplate.send(topic, request);
        } else {
            // 直接发送
            retryHandler.execute(request);
        }
    }
}