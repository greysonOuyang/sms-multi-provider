package org.moretea.sms.service.provider.tianyiyun;

import org.moretea.sms.api.AbstractSmsProvider;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.api.exception.SmsSendException;
import org.moretea.sms.service.provider.config.SmsProviderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service("ctyunSmsProvider")
public class CtyunSmsProvider extends AbstractSmsProvider {
    private final SmsProviderProperties properties;

    @Autowired
    public CtyunSmsProvider(CtyunSmsProviderProperties properties) {
        this.properties = properties;
    }

    @Override
    public CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest) throws SmsSendException {
        return null;
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public String getName() {
        return "ctyun";
    }
}