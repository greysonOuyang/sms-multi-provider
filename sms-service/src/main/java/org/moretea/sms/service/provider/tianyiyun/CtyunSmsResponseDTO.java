package org.moretea.sms.service.provider.tianyiyun;

import lombok.Data;

/**
 * 天翼云短信响应DTO
 */
@Data
public class CtyunSmsResponseDTO {
    
    /**
     * 响应码
     * ok 表示成功
     */
    private String code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * 发送结果详情
     */
    private SendResult sendResult;
    
    /**
     * 发送结果
     */
    @Data
    public static class SendResult {
        /**
         * 手机号
         */
        private String phoneNumber;
        
        /**
         * 发送状态
         * 1: 提交成功
         * 2: 提交失败
         */
        private String sendStatus;
        
        /**
         * 短信ID
         */
        private String smsId;
        
        /**
         * 运营商返回码
         */
        private String carrierCode;
        
        /**
         * 运营商返回消息
         */
        private String carrierMessage;
    }
}
