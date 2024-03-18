package com.sms.api.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 短信发送包装服务
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/18
 * Time: 14:58
 */

public interface SmsWrapperService {
    /**
     * 发送短信 所有参数手动设置
     */
    CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest);


    /**
     * 批量发送短信
     * @param request
     * @return
     */
    CompletableFuture<List<SmsResponse>> sendBatchSms(BatchSmsRequest request);
}
