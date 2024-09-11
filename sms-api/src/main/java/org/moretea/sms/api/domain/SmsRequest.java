package org.moretea.sms.api.domain;

import org.moretea.sms.api.SmsProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    /**
     * 模板code
     */
    private String templateCode;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 发送短信内容
     */
    private String message;
    /**
     * 发送ip地址
     */
    private String ip;
    /**
     * 计划发送时间
     */
    private String scheduleTime;
    /**
     * 签名 从配置取 一般不设置
     */
    private String signName;

    /**
     * 内部使用 追踪结果的Id
     * @param builder
     */
    private String uniqueId;

    /**
     * 发送短信用不上，用于记录在历史表中
     */
    private String receiverName;
    /**
     * 模板参数 Map 形式
     */
    private Map<String, String> templateParams;

    /**
     * 模板参数 占位形式
     */
    private String params;
    /**
     * 服务商
     */
    private SmsProvider chosenProvider;
}