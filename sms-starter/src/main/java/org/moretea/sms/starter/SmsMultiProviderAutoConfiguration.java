package org.moretea.sms.starter;

import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.config.SmsProperties;
import org.moretea.sms.facade.sender.SmsFacade;
import org.moretea.sms.service.config.SmsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * SMS Multi Provider 自动配置入口
 *
 * 提供 SmsFacade 的自动装配，让开发者开箱即用
 *
 * @author greyson
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(SmsFacade.class)
@EnableConfigurationProperties(SmsProperties.class)
@ComponentScan(basePackages = {
    "org.moretea.sms.facade",
    "org.moretea.sms.load.balance",
    "org.moretea.sms.service"
})
@Import({SmsServiceAutoConfiguration.class, SmsAsyncConfiguration.class})
public class SmsMultiProviderAutoConfiguration {

    public SmsMultiProviderAutoConfiguration() {
        log.info("SMS Multi Provider Auto Configuration initialized");
    }
}
