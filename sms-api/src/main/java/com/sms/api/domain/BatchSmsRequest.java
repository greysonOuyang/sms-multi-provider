package com.sms.api.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BatchSmsRequest {
    /**
     * 签名 默认不填，从配置取统一签名
     */
    private String signName;
    /**
     * 模板code 请求标识 必填
     */
    private String templateCode;
    /**
     * 计划发送时间
     */
    private String scheduleTime;
    /**
     * 发送目标以及模板参数 必填
     */
    private List<SmsTarget> targets;


    @Data
    public static class SmsTarget {
        /**
         * 发送短信用不上，用于记录在历史表中
         */
        private String receiverName;
        /**
         * 唯一性ID 用来追踪发送结果
         */
        private String uniqueId;

        /**
         * 电话
         */
        private String phoneNumber;
        /**
         * 模板参数
         */
        private Map<String, String> templateParams;

        /**
         * 模板参数
         */
        private String params;


    }
}