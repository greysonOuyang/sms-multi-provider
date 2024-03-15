package com.sms.api;


import com.sms.exception.SmsProviderException;

/**
 * @author greysonchance
 */
public interface SmsProvider {
    void sendSms(String phoneNumber, String message) throws SmsProviderException;
    boolean isAvailable();
    boolean isHealthy(); // 新添加的方法，用于判断供应商是否健康
}