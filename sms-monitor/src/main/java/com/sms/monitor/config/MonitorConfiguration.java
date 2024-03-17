package com.sms.monitor.config;

import com.sms.monitor.NullMonitor;
import com.sms.monitor.PrometheusLogstashMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.monitor.Monitor;

@Configuration
public class MonitorConfiguration {
    @Autowired
    private MonitorProperties properties;

    @Bean
    public Monitor monitor() {
        if (properties.isEnabled()) {
            switch (properties.getClientType()) {
                case "PrometheusLogstash": return new PrometheusLogstashMonitor(/* 参数... */);
                // 其他类型...
                default: throw new IllegalArgumentException("Unsupported client type: " + properties.getClientType());
            }
        } else {
            return new NullMonitor();
        }
    }
}