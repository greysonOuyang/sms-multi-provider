package com.sms.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "monitor")
public class MonitorProperties {
    private boolean enabled;
    private String clientType;
    private String dataUrl;
    private String apiKey;

//    public boolean isEnabled() {
//        return enabled;
//    }
    // getter 和 setter...
}