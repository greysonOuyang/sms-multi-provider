package com.sms.service;

import com.sms.api.SmsProvider;
import com.sms.api.UnavailableHandler;
import com.sms.api.exception.SmsSendException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSmsProvider implements SmsProvider {
    
    @Autowired
    private UnavailableHandler unavailableHandler;

    /**
     * 服务是否可用
     */
    private boolean available = true;

    @Override
    public void sendSms(String phoneNumber, String message) throws SmsSendException {
        if (!available) {
            throw new SmsSendException("服务不可用。");
        }
        try {
            sendSmsInternal(phoneNumber, message);
        } catch (Exception ex) {
            // 出现异常，标记此服务主为不可用，并通知UnavailableHandler
            unavailable();
            throw new SmsSendException("短信发送失败，服务被标记为不可用。", ex);
        }
    }

    protected abstract void sendSmsInternal(String phoneNumber, String message) throws Exception;

    protected void unavailable() {
        available = false;
        // 通知UnavailableHandler此服务商状态为不可用
        unavailableHandler.handleUnavailable(this);
    }
}