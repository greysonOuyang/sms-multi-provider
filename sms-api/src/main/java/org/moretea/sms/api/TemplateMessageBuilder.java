package com.sms.api;

import java.util.Collection;

/**
 * 服务商根据模板组织消息接口
 *
 * @author greysonchance
 * @since 3/17/24
 */
public interface TemplateMessageBuilder {
    /**
     * 根据模板构造短信内容
     * @param businessCode 业务代码
     * @param params 模板参数，形式可能是Map 也可能是一组参数列表
     * @return 返回模板配置与参数的组合结果
     */
    String buildMessage(String businessCode, Collection<?> params, String providerName);
}
