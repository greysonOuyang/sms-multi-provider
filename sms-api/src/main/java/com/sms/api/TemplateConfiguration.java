package com.sms.api;

import com.sms.api.domain.SmsTemplateEntity;

/**
 * 模板配置形式接口
 */
public interface TemplateConfiguration {

    /**
     * 获取模板
     *
     * @param businessCode  业务编码
     * @param provider      服务商名称
     * @return              找到的模板
     */
    SmsTemplateEntity getTemplate(String businessCode, String provider);


}