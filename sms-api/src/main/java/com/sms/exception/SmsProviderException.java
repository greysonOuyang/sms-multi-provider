package com.sms.exception;

/**
 * 发送短信异常
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/14
 * Time: 17:15
 */

public class SmsProviderException extends Exception {

    public SmsProviderException(String message) {
        super(message);
    }

    public SmsProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
