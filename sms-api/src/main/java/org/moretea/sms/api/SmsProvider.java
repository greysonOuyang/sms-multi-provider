package org.moretea.sms.api;


import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.api.exception.SmsSendException;

import java.util.concurrent.CompletableFuture;

/**
 * 短信服务商接口
 * 
 * 所有短信服务商（阿里云、天翼云等）必须实现此接口
 * 
 * @author greyson
 * @since 1.0.0
 */
public interface SmsProvider {

    /**
     * 发送短信
     *
     * @param smsRequest 短信请求
     * @return 异步响应
     * @throws SmsSendException 发送异常
     */
    CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest) throws SmsSendException;

    /**
     * 探测服务是否可用
     * 
     * @return true 可用，false 不可用
     */
    boolean isHealthy();

    /**
     * 获取服务商名称
     * 
     * @return 服务商名称（如 aliyun, ctyun）
     */
    String getName();

    /**
     * 是否可用（用于熔断）
     * 
     * @return true 可用
     */
    boolean isAvailable();
    
    /**
     * 设置可用状态（用于熔断）
     * 
     * @param available 是否可用
     */
    void setAvailable(boolean available);
    
    /**
     * 获取负载均衡权重
     * 
     * @return 权重值，默认 10
     */
    default int getWeight() {
        return 10;
    }
}
