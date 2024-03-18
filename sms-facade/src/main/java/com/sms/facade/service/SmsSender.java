package com.sms.facade.service;

import com.sms.api.SmsProvider;
import com.sms.api.TemplateMessageBuilder;
import com.sms.api.exception.SmsSendException;
import com.sms.facade.RetryHandler;
import com.sms.api.domain.SmsRequest;
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

    public void sendSms(String phoneNumber, String message) throws SmsSendException {
        // 获取服务提供商
        SmsProvider chosenProvider = loadBalancerManager.currentProvider();
        if (chosenProvider == null) {
            throw new SmsSendException("All sms providers are unavailable");
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

    public void sendByTemplate(String businessCode, String phoneNumber, Collection<?> params) throws SmsSendException {
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