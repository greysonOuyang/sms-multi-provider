package com.sms.api;


import com.sms.exception.SmsProviderException;

/**
 * @author greysonchance
 */
public interface SmsProvider {
    void sendSms(String phoneNumber, String message) throws SmsProviderException;

    /**
     * 探测服务是否可用，默认未实现，即true，如果有服务商提供了探测接口，可接入
     * @return
     */
    boolean isHealthy(); // 新添加的方法，用于判断供应商是否健康

    String getName();
}