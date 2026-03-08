package org.moretea.sms.service.provider.tianyiyun;

import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.SmsRequestTranslator;
import org.moretea.sms.api.domain.SmsRequest;
import org.springframework.stereotype.Component;

/**
 * 天翼云短信请求参数转换器
 */
@Slf4j
@Component
public class CtyunSmsRequestTranslator implements SmsRequestTranslator {
    
    @Override
    public CtyunSmsRequestDTO translate(SmsRequest smsRequest) {
        CtyunSmsRequestDTO dto = new CtyunSmsRequestDTO();
        dto.setPhoneNumber(smsRequest.getPhoneNumber());
        dto.setTemplateCode(smsRequest.getTemplateCode());
        dto.setSignName(smsRequest.getSignName());
        dto.setTemplateParam(smsRequest.getParams());
        dto.setOutId(smsRequest.getUniqueId());
        
        log.debug("天翼云短信请求转换完成, phoneNumber: {}, templateCode: {}", 
                smsRequest.getPhoneNumber(), smsRequest.getTemplateCode());
        
        return dto;
    }
}
