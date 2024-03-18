package com.sms.api;


import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsTemplateEntity;
import com.sms.api.exception.SmsCommonException;

import java.util.List;

/**
 * 短信业务服务
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/1/18
 * Time: 17:44
 */

public interface SmsBusinessService {
    /**
     * 查询模板CODE
     * @param businessCode 业务编码
     * @return
     */
    SmsTemplateEntity getTemplateCode(String businessCode);

    /**
     * 根据业务枚举和参数值列表获取发送参数
     * @param businessCode 业务编码
     * @param values
     * @return
     */
    BatchSmsRequest.SmsTarget generateParameter(String businessCode, List<String> values, String phoneNumber, String name) throws SmsCommonException;
}
