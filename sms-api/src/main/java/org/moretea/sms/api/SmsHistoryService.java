//package org.moretea.sms.api;
//
//
//import com.baomidou.mybatisplus.extension.service.IService;
//import org.moretea.sms.api.domain.BatchSmsRequest;
//import org.moretea.sms.api.domain.SmsHistoryEntity;
//import org.moretea.sms.api.domain.SmsResponse;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//
///**
// * 短信发送历史记录
// *
// * @author greyson
// * @email
// * @date 2024-01-23 15:37:17
// */
//public interface SmsHistoryService extends IService<SmsHistoryEntity> {
//
//    /**
//     * 发送基本参数入库
//     * @param smsRequest
//     * @param businessCode
//     */
//    void buildBaseParam(BatchSmsRequest smsRequest, String businessCode);
//
//    /**
//     * 设置响应结果
//     */
//    CompletableFuture<Void> recordSendResultAsync(CompletableFuture<List<SmsResponse>> resultsFuture);
//
//}
//
