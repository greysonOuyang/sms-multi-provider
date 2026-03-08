package org.moretea.sms.service.provider.tianyiyun;

import lombok.Data;
import java.util.Map;

/**
 * 天翼云短信请求DTO
 */
@Data
public class CtyunSmsRequestDTO {
    
    /**
     * 手机号，多个用逗号分隔
     */
    private String phoneNumber;
    
    /**
     * 短信模板ID
     */
    private String templateCode;
    
    /**
     * 短信签名
     */
    private String signName;
    
    /**
     * 模板参数，JSON格式
     */
    private String templateParam;
    
    /**
     * 扩展码
     */
    private String extendCode;
    
    /**
     * 业务标识
     */
    private String outId;
}
