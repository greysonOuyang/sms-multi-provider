package org.moretea.sms.service.provider.config;

import org.moretea.sms.api.SmsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 服务商列表
 * <p>
 * Author: greyson
 * Email:  
 * Date: 2024/3/15
 * Time: 15:11
 */
@Configuration
//@ConfigurationProperties(prefix = "sms.base.provider")
@Slf4j
public class ProviderConfig {
    /**
     * 选择启用的服务商
     */
    @Value("${sms.provider.selected:}")
    private String selected;

    @Autowired
    private ApplicationContext context;

    private List<SmsProvider> providers = new ArrayList<>();

    @PostConstruct
    public void initProviders() {
        if (StringUtils.hasText(selected)) {
            initByConfiguredSelection();
            return;
        }

        initByAutoDetection();
    }

    private void initByConfiguredSelection() {
        List<SmsProvider> providerList = new ArrayList<>();
        Arrays.stream(selected.split(",")).forEach(name -> {
            String beanName = name.trim() + "SmsProvider";
            if (context.containsBean(beanName)) {
                providerList.add((SmsProvider) context.getBean(beanName));
            } else {
                throw new RuntimeException("The provider with name " + beanName + " is not defined.");
            }
        });
        providers = providerList;
        log.info("Using configured providers: {}", selected);
    }

    private void initByAutoDetection() {
        Map<String, SmsProvider> providerBeans = context.getBeansOfType(SmsProvider.class);
        if (providerBeans.isEmpty()) {
            throw new RuntimeException("No SMS providers found. Please configure `sms.provider.selected` and provider credentials.");
        }

        List<SmsProvider> providerList = new ArrayList<>();
        providerBeans.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> providerList.add(entry.getValue()));

        providers = providerList;
        log.info("No `sms.provider.selected` configured. Auto-detected providers: {}", providerBeans.keySet());
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
