package org.moretea.sms.service.provider.tianyiyun;

import org.moretea.sms.api.SmsResponseTranslator;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.springframework.stereotype.Component;

/**
 * 天翼云短信响应转换器
 */
@Component
public class CtyunSmsResponseTranslator implements SmsResponseTranslator {
    
    @Override
    public SmsResponse<CtyunSmsResponseDTO> translate(Object response, SmsRequest originalRequest) {
        CtyunSmsResponseDTO ctyunResponse = (CtyunSmsResponseDTO) response;
        String uniqueId = originalRequest.getUniqueId();
        
        // 天翼云返回 code = "ok" 表示成功
        if ("ok".equalsIgnoreCase(ctyunResponse.getCode())) {
            CtyunSmsResponseDTO.SendResult sendResult = ctyunResponse.getSendResult();
            
            // 检查发送状态
            if (sendResult != null && "1".equals(sendResult.getSendStatus())) {
                return SmsResponse.sendSuccess(
                        uniqueId,
                        "短信发送成功",
                        ctyunResponse
                );
            } else {
                // 提交成功但发送失败
                String failMsg = sendResult != null 
                        ? sendResult.getCarrierMessage() 
                        : "发送失败";
                return SmsResponse.recordResult(
                        uniqueId,
                        false,
                        failMsg,
                        ctyunResponse
                );
            }
        } else {
            // 请求失败
            return SmsResponse.sendError(
                    uniqueId,
                    ctyunResponse.getMessage() != null ? ctyunResponse.getMessage() : "发送失败",
                    ctyunResponse
            );
        }
    }
}
