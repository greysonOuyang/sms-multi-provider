package com.sms.api.domain;
import lombok.Data;

import java.util.Map;

@Data
public class MessageTemplate {
    // 业务代码
    private String businessCode;
    // 服务供应商名称
    private String provider;
    // 短信内容模板
    private String template;
    // 模板ID
    private String templateId;
    // 签名内容
    private String signContent;
    // 其他可能需要的参数
    private Map<String, String> extra;

}