//package com.sms.service.templates;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.sms.api.SmsTemplateService;
//import com.sms.api.TemplateConfiguration;
//import com.sms.api.domain.SmsTemplateEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class DatabaseTemplateConfiguration implements TemplateConfiguration {
//
//    @Autowired
//    private SmsTemplateService templateService;
//
//    @Override
//    public SmsTemplateEntity getTemplate(String businessCode, String provider) {
//        // 从数据库中读取模板并返回
//        return templateService.getBaseMapper().selectOne(
//                Wrappers.lambdaQuery(SmsTemplateEntity.class).eq(SmsTemplateEntity::getBusinessCode, businessCode)
//                        .eq(SmsTemplateEntity::getSmsProvider, provider));
//    }
//}