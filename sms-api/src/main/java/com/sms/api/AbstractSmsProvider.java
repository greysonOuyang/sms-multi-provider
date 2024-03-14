package com.sms.api;

public abstract class AbstractSmsProvider implements SmsProvider {

    private boolean available = true;

    @Override
    public void sendSms(String phoneNumber, String message) throws SmsProviderException {
        if (!isAvailable()) {
            throw new SmsProviderException("服务不可用。");
        }

        try {
            sendSmsInternal(phoneNumber, message);
        } catch (Exception ex) {
            setUnavailable();
            throw new SmsProviderException("短信发送失败，服务被标记为不可用。", ex);
        }
    }

    protected abstract void sendSmsInternal(String phoneNumber, String message) throws Exception;

    @Override
    public boolean isAvailable() {
        return this.available;
    }

    protected void setUnavailable() {
        this.available = false;
    }
}