package com.sms.api.domain;

import com.sms.api.SmsProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 这是抽象层请求参数封装，目前参数名称是对应三医平台的，所以没有设置三医平台的translator，后续有更多的平台接入再将这个抽象成与平台无关
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    /**
     * 模板code
     */
    private String reqCode;
    /**
     * 手机号
     */
    private String mobiles;
    private String phoneNumber;
    /**
     * 模板参数
     */
    private String msg;
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
    private String signText;

    /**
     * 内部使用 追踪结果的Id
     * @param builder
     */
    private String uniqueId;

    private SmsProvider chosenProvider;

    private SmsRequest(Builder builder) {
        this.reqCode = builder.reqCode;
        this.mobiles = builder.mobiles;
        this.msg = builder.msg;
        this.ip = builder.ip;
        this.scheduleTime = builder.scheduleTime;
        this.signText = builder.signText;
        this.uniqueId = builder.uniqueId;
    }

    public static class Builder {
        private String reqCode;
        private String mobiles;
        private String msg;
        private String ip;
        private String scheduleTime;
        private String signText;

        private String uniqueId;

        public Builder uniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public Builder reqCode(String reqCode) {
            this.reqCode = reqCode;
            return this;
        }

        public Builder mobiles(String mobile) {
            this.mobiles = mobile;
            return this;
        }

        public Builder msg(String message) {
            this.msg = message;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }


        public Builder scheduleTimeStr(String scheduleTime) {
            this.scheduleTime = scheduleTime;
            return this;
        }

        public Builder signText(String signText) {
            this.signText = signText;
            return this;
        }

        public SmsRequest build() {
            return new SmsRequest(this);
        }
    }
}