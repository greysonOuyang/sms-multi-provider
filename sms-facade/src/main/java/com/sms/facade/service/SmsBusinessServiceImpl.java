package com.sms.facade.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sms.api.SmsBusinessService;
import com.sms.api.SmsTemplateService;
import com.sms.api.TemplateMessageBuilder;
import com.sms.api.domain.BatchSmsRequest;
import com.sms.api.domain.SmsTemplateEntity;
import com.sms.api.exception.SmsCommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 短信业务服务实现
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/1/18
 * Time: 17:45
 */
@Slf4j
@Service
public class SmsBusinessServiceImpl implements SmsBusinessService {

    @Autowired
    private SmsTemplateService templateService;

    @Value("${sms.provider:1}")
    private String smsProvider;

    @Autowired
    private TemplateMessageBuilder msgBuilder;



    @Override
    public BatchSmsRequest.SmsTarget generateParameter(String businessCode, List<String> values, String phoneNumber, String name) throws SmsCommonException {
        // TODO
        msgBuilder.buildMessage(businessCode, generateMap(templateEntity.getParameters(), values))
        BatchSmsRequest.SmsTarget smsTarget = new BatchSmsRequest.SmsTarget();
        smsTarget.setUniqueId(IdUtil.fastSimpleUUID());
        smsTarget.setPhoneNumber(phoneNumber);
        smsTarget.setReceiverName(name);
        SmsTemplateEntity templateEntity = getTemplateCode(businessCode);
            smsTarget.setTemplateParams();
            smsTarget.setParams(String.join(",", values));
        if (CollectionUtil.isEmpty(smsTarget.getTemplateParams()) && StringUtils.isEmpty(smsTarget.getParams())) {
            throw new SmsCommonException("没有成功设置模板参数，请检查");
        }
        return smsTarget;
    }

    @Override
    public SmsTemplateEntity getTemplateCode(String businessCode) {
        return templateService.getBaseMapper().selectOne(
                Wrappers.lambdaQuery(SmsTemplateEntity.class).eq(SmsTemplateEntity::getBusinessCode, businessCode)
                        .eq(SmsTemplateEntity::getSmsProvider, smsProvider)
        );
    }

    private Map<String, String> generateMap(String keys, List<String> values){
        Map<String, String> paramMap = new LinkedHashMap<>();
        String[] keysArray = keys.split(",");
        if(keysArray.length != values.size()) {
            throw new IllegalArgumentException("键的数量和值的数量不匹配！");
            // 这里可以根据需要抛出一个异常
        } else {
            for(int i = 0; i < keysArray.length; i++) {
                paramMap.put(keysArray[i].trim(), values.get(i));
            }
        }
        return paramMap;
    }

}
