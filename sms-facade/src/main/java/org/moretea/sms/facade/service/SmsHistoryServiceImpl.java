//package com.sms.facade.service;
//
//import cn.hutool.core.collection.CollectionUtil;
//import com.alibaba.fastjson2.JSONObject;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.sms.api.SmsHistoryService;
//import com.sms.api.domain.BatchSmsRequest;
//import com.sms.api.domain.SmsHistoryEntity;
//import com.sms.facade.mapper.SmsHistoryMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service("smsHistoryService")
//public class SmsHistoryServiceImpl extends ServiceImpl<SmsHistoryMapper, SmsHistoryEntity> implements SmsHistoryService {
//
//    @Autowired
//    private SmsBusinessService smsBusiness;
//
//    @Override
//    @Async
//    public CompletableFuture<Void> recordSendResultAsync(CompletableFuture<List<SmsResponse>> resultsFuture) {
//        return resultsFuture.thenAcceptAsync(smsResponses -> {
//            if (CollectionUtil.isEmpty(smsResponses)) {
//                log.error("获取短信发送响应结果失败");
//                return;
//            }
//            List<SmsHistoryEntity> historyEntities = smsResponses.stream()
//                    .map(this::getUpdateEntity)
//                    .collect(Collectors.toList());
//
//            this.updateBatchById(historyEntities);
//        });
//    }
//
//    private SmsHistoryEntity getUpdateEntity(SmsResponse resp) {
//        String uniqueId = resp.getUniqueId();
//        log.info("uniqueId:{}, 响应信息：{}", uniqueId, JSONObject.toJSONString(resp));
//        SmsHistoryEntity historyEntity = getBaseMapper().selectOne(Wrappers.lambdaQuery(SmsHistoryEntity.class)
//                .eq(SmsHistoryEntity::getUniqueId, uniqueId));
//        historyEntity.setResult(resp.getSendStatus());
//        historyEntity.setMessage(resp.getMsg());
//        if (resp.getData() instanceof String) {
//            historyEntity.setData((String) resp.getData());
//        }
//        return historyEntity;
//    }
//
//    private List<SmsResponse> getSmsResponses(CompletableFuture<List<SmsResponse>> resultsFuture) {
//        try {
//            return resultsFuture.get();
//        } catch (InterruptedException | ExecutionException e) {
//            log.error("获取短信发送响应结果失败: {}", e);
//            return null;
//        }
//    }
//
//    @Override
//    public void buildBaseParam(BatchSmsRequest smsRequest, BusinessEnum businessEnum) {
//        List<BatchSmsRequest.SmsTarget> targets = smsRequest.getTargets();
//        List<SmsHistoryEntity> historyEntityList = targets.stream().map(item -> {
//            SmsHistoryEntity his = new SmsHistoryEntity();
//            his.setUniqueId(item.getUniqueId());
//            LoginUser loginUser = SecurityUtils.getLoginUser();
//            if (loginUser != null) {
//                his.setSenderId(loginUser.getUserid());
//                his.setSenderName(loginUser.getUsername());
//            }
//            his.setReceiverPhone(item.getPhoneNumber());
//            his.setReceiverName(item.getReceiverName());
//            SmsTemplateEntity templateEntity = smsBusiness.getTemplateCode(businessEnum);
//            if (templateEntity != null) {
//                his.setTemplateId(templateEntity.getId());
//                his.setBusinessCode(templateEntity.getReqCode());
//            }
//            if (CollectionUtil.isNotEmpty(item.getTemplateParams())) {
//                String params = JSONObject.toJSONString(item.getTemplateParams());
//                his.setParams(params);
//            } else {
//                his.setParams(item.getParams());
//            }
//            return his;
//        }).collect(Collectors.toList());
//        saveBatch(historyEntityList);
//    }
//}