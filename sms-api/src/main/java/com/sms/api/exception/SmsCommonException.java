package com.sms.api.exception;

/**
 * 发送短信异常
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/14
 * Time: 17:15
 */

public class SmsCommonException extends Exception {

    public SmsCommonException(String message) {
        super(message);
    }

    public SmsCommonException(String message, Throwable cause) {
        super(message, cause);
    }
}
