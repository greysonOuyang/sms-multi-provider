package com.sms.api;


import com.sms.api.domain.SmsRequest;
import com.sms.api.exception.SmsCommonException;

import java.util.List;

/**
 * 请求参数构造服务
 * <p>
 * Author: greyson
 * Email:  
 * Date: 2024/1/18
 * Time: 17:44
 */

public interface RequestHelperService {

    /**
     * 根据业务枚举和参数值列表获取发送参数
     * @param businessCode 业务编码
     * @param paramValues 参数对应的值
     * @return
     */
    SmsRequest generateParameter(String businessCode, List<String> paramValues, String phoneNumber, String name) throws SmsCommonException;
}
