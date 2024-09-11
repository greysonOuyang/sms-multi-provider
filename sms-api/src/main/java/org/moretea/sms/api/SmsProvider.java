package org.moretea.sms.api;


import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.api.exception.SmsSendException;

import java.util.concurrent.CompletableFuture;

/**
 * @author greysonchance
 */
public interface SmsProvider {

    CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest) throws SmsSendException;

    /**
     * 探测服务是否可用，默认未实现，即true，如果有服务商提供了探测接口，可接入
     * @return
     */
    boolean isHealthy();

    String getName();

    boolean isAvailable();
    void setAvailable(boolean available);
}