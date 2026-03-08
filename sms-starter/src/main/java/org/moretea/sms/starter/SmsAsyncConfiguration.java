package org.moretea.sms.starter;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启用异步支持
 * 
 * SmsProvider 的发送方法是异步的，需要启用 @EnableAsync
 */
@Configuration
@EnableAsync
public class SmsAsyncConfiguration {
}
