package com.sms.api;

/**
 * @author greysonchance
 */
public interface SmsProvider {
    void sendSms(String phoneNumber, String message) throws SmsProviderException;
    boolean isAvailable();
}