package com.sms.service.provider.config;

import com.sms.api.SmsProvider;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 服务商列表
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/15
 * Time: 15:11
 */
@Configuration
@ConfigurationProperties(prefix = "sms.base.provider")
public class ProviderConfig {
    /**
     * 选择启用的服务商
     */
    private String selected;

    @Autowired
    private ApplicationContext context;

    private static List<SmsProvider> providers = new ArrayList<>();

    @PostConstruct
    public void initProviders() {
        if (StringUtils.isEmpty(selected)) {
            throw new RuntimeException("No SMS providers was set.");
        }

        List<SmsProvider> providerList = new ArrayList<>();
        Arrays.stream(selected.split(",")).forEach(name -> {
            String beanName = name.trim() + "SmsProvider";
            if (context.containsBean(beanName)) {
                providerList.add((SmsProvider)context.getBean(beanName));
            } else {
                throw new RuntimeException("The provider with name " + beanName + " is not defined.");
            }
        });
        providers = providerList;
    }

    @Bean
    public List<SmsProvider> smsProviders() {
        return providers;
    }

    public SmsProvider getFirstProvider() {
        return providers.get(0);
    }

    public List<SmsProvider> getAllProviders() {
        return providers;
    }
}
