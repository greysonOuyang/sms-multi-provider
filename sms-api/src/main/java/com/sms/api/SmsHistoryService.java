package com.sms.api;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.facade.domain.BatchSmsRequest;
import com.sms.facade.domain.SmsHistoryEntity;
import com.sms.facade.domain.SmsResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * 短信发送历史记录
 *
 * @author greyson
 * @email ouyangguanling@ssc-hn.com
 * @date 2024-01-23 15:37:17
 */
public interface SmsHistoryService extends IService<SmsHistoryEntity> {

    /**
     * 发送基本参数入库
     * @param smsRequest
     * @param businessEnum
     */
    void buildBaseParam(BatchSmsRequest smsRequest, BusinessEnum businessEnum);

    /**
     * 设置响应结果
     */
    CompletableFuture<Void> recordSendResultAsync(CompletableFuture<List<SmsResponse>> resultsFuture);

}

