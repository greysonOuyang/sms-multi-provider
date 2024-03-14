package com.sms.api;

/**
 * @author greysonchance
 */
public interface SmsProvider {
    void send(String phoneNumber, String message);
}