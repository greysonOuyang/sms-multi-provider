package org.moretea.sms.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SMS Multi Provider 示例应用启动类
 */
@Slf4j
@SpringBootApplication
public class SmsExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsExampleApplication.class, args);
        log.info("========================================");
        log.info("SMS Multi Provider Example 启动成功！");
        log.info("访问 http://localhost:8080/actuator/health 检查健康状态");
        log.info("========================================");
    }
}
