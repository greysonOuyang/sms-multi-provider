package com.sms.service.provider.aliyun;

import com.sms.api.LoadBalancerStrategy;
import com.sms.api.domain.MessageTemplate;
import com.sms.api.TemplateMessageBuilder;
import com.sms.api.TemplateConfigurationInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * description
 *
 * @author greysonchance
 * @since 3/17/24
 */
public class AliyunMsgBuilder implements TemplateMessageBuilder {

    @Autowired
    private TemplateConfigurationInterface templateConfigurationInterface;

    @Autowired
    private LoadBalancerStrategy ss;

    @Override
    public String buildMessage(String businessCode, Collection<?> params, String providerName) {
        MessageTemplate template = templateConfigurationInterface.getTemplate(businessCode, providerName);
        return null;
    }
}
