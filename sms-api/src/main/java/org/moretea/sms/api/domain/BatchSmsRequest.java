package com.sms.api.domain;

import lombok.Data;

import java.util.List;

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
    private List<SmsRequest> targets;
}