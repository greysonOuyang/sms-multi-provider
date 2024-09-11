package com.sms.api;

import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsResponse;

import java.util.concurrent.CompletableFuture;

/**
 * 响应翻译器
 * <p>
 * Author: greyson
 * Email:  
 * Date: 2024/3/18
 * Time: 16:38
 */

public interface SmsResponseTranslator {
    SmsResponse translate(Object  resp, SmsRequest sendSmsRequest);
}
