package org.moretea.sms.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 响应结果
 * <p>
 * Author: greyson
 * Email:  
 * Date: 2024/1/12
 * Time: 15:55
 */
@Data
@NoArgsConstructor
public class SmsResponse<T> {


    /**
     * 0:失败；1:成功 请求状态
     */
    private Boolean status;
    /**
     *  发送状态
     */
    private Boolean sendStatus;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 额外数据
     */
    private T data;

    /**
     * 跟踪ID
     */
    private String uniqueId;


    private SmsResponse(Builder<T> builder) {
        this.status = builder.status;
        this.sendStatus = builder.sendStatus;
        this.msg = builder.msg;
        this.data = builder.data;
        this.uniqueId = builder.uniqueId;
    }

    public static class Builder<T> {
        private Boolean status;
        private Boolean sendStatus;
        private String msg;
        private T data;
        private String uniqueId;

        public Builder<T> status(Boolean status) {
            this.status = status;
            return this;
        }

        public Builder<T> sendStatus(Boolean sendStatus) {
            this.sendStatus = sendStatus;
            return this;
        }

        public Builder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> uniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
            return this;
        }

        public SmsResponse<T> build() {
            return new SmsResponse<>(this);
        }
    }

    public static <T> SmsResponse<T> sendError(String uniqueId, String message, T data) {
        return new Builder<T>()
                .status(true)
                .sendStatus(false)
                .msg(message)
                .data(data)
                .uniqueId(uniqueId)
                .build();
    }

    /**
     * 请求平台成功 平台请求失败
     * @return
     */
    public static <T> SmsResponse sanYiHttpError(String uniqueId, T detail) {
        return new Builder<T>()
                .status(true)
                .sendStatus(false)
                .msg("平台发送短信失败")
                .data(detail)
                .uniqueId(uniqueId)
                .build();
    }

    /**
     * 请求失败
     * @return
     */
    public static <T> SmsResponse httpError(String uniqueId) {
        return new Builder<T>()
                .status(false)
                .sendStatus(false)
                .msg("发送短信——请求平台失败")
                .data(null)
                .uniqueId(uniqueId)
                .build();
    }

    /**
     * 请求失败
     * @return
     */
    public static <T> SmsResponse httpError(String uniqueId, T detail) {
        return new Builder<T>()
                .status(false)
                .sendStatus(false)
                .msg("发送短信——请求平台失败")
                .data(detail)
                .uniqueId(uniqueId)
                .build();
    }


    /**
     * 请求成功 并且发送成功
     *
     * @param uniqueId
     * @param msg
     * @param data
     * @return
     */
    public static <T> SmsResponse sendSuccess(String uniqueId, String msg, T data) {
        return new Builder<T>()
                .status(true)
                .sendStatus(true)
                .msg(msg)
                .data(data)
                .uniqueId(uniqueId)
                .build();
    }

    /**
     * 请求成功，记录发送结果
     *
     * @param uniqueId
     * @param msg
     * @param data
     * @return
     */
    public static <T> SmsResponse recordResult(String uniqueId, Boolean sendStatus, String msg, T data) {
        return new Builder<T>()
                .status(true)
                .sendStatus(sendStatus)
                .msg(msg)
                .data(data)
                .uniqueId(uniqueId)
                .build();
    }


}
