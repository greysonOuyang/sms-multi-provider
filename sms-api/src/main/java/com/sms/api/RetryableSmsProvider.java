package com.sms.api;

/**
 * 重试机制
 */
public class RetryableSmsProvider implements SmsProvider {
    private SmsProvider delegate;
    private int maxAttempts;

    public RetryableSmsProvider(SmsProvider delegate, int maxAttempts) {
        this.delegate = delegate;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void sendSms(String phoneNumber, String message) throws SmsProviderException {
        SmsProviderException exception = null;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                delegate.sendSms(phoneNumber, message);
                return;
            } catch (SmsProviderException ex) {
                exception = ex;
                // 延迟逻辑，比如 Thread.sleep(...) 或者使用更灵活的策略
            }
        }
        throw exception;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}