package org.moretea.sms.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 短信平台启动类
 *
 * @author greyson
 * @version 1.0.0
 * @since 2024/3/1
 */
@SpringBootApplication(scanBasePackages = "org.moretea.sms")
@EnableAsync
@EnableScheduling
public class SmsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsPlatformApplication.class, args);
    }

}
