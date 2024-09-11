//package org.moretea.sms.service.templates;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import org.moretea.sms.api.SmsTemplateService;
//import org.moretea.sms.api.TemplateConfiguration;
//import org.moretea.sms.api.domain.SmsTemplateEntity;
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