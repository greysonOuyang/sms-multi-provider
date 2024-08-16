package com.sms.facade.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sms.api.RequestHelperService;
import com.sms.api.TemplateConfiguration;
import com.sms.api.domain.SmsRequest;
import com.sms.api.domain.SmsTemplateEntity;
import com.sms.api.exception.SmsCommonException;
import com.sms.facade.enums.ParamTypeEnum;
import com.sms.load.balance.LoadBalancerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信业务服务实现
 * <p>
 * Author: greyson
 * Email:  
 * Date: 2024/1/18
 * Time: 17:45
 */
@Slf4j
@Service
public class RequestHelperServiceImpl implements RequestHelperService {

    @Value("${sms.template.type:config}")
    private String templateConfig;


    private final Map<String, TemplateConfiguration> templateConfigurations;

    @Autowired
    public RequestHelperServiceImpl(Map<String, TemplateConfiguration> templateConfigurations) {
        this.templateConfigurations = templateConfigurations;
    }

    private TemplateConfiguration templateConfiguration;

    @Autowired
    private LoadBalancerManager loadBalancerManager;

    @PostConstruct
    public void setTemplateConfiguration() {
        templateConfiguration = templateConfigurations.get(templateConfig + "TemplateConfiguration");
    }


    @Override
    public SmsRequest generateParameter(String businessCode, List<String> paramValues, String phoneNumber, String name) throws SmsCommonException {

        SmsRequest smsRequest = SmsRequest.builder().uniqueId(IdUtil.fastSimpleUUID())
                .phoneNumber(phoneNumber)
                .receiverName(name).build();

        SmsTemplateEntity template = templateConfiguration.getTemplate(businessCode, loadBalancerManager.currentProviderName());
        // TODO 参数形式以及组合待优化 数据库加字段 配置文件加字段
        if (template.getParamType().equals(ParamTypeEnum.MAP.getType())) {
            smsRequest.setTemplateParams(generateMap(template.getParameters(), paramValues));
        } else {
            smsRequest.setParams(String.join(",", paramValues));
        }
        if (CollUtil.isEmpty(smsRequest.getTemplateParams()) && StringUtils.isEmpty(smsRequest.getParams())) {
            throw new SmsCommonException("没有成功设置模板参数，请检查");
        }
        return smsRequest;
    }


    private Map<String, String> generateMap(String keys, List<String> values) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        String[] keysArray = keys.split(",");
        if (keysArray.length != values.size()) {
            throw new IllegalArgumentException("键的数量和值的数量不匹配！");
        } else {
            for (int i = 0; i < keysArray.length; i++) {
                paramMap.put(keysArray[i].trim(), values.get(i));
            }
        }
        return paramMap;
    }

}
