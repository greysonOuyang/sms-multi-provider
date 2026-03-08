package org.moretea.sms.service.provider.tianyiyun;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.AbstractSmsProvider;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.api.exception.SmsSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 天翼云短信服务商实现
 * 
 * 天翼云短信API文档: https://www.ctyun.cn/document/10027696/10034783
 * 
 * @author greyson
 * @since 1.0.0
 */
@Slf4j
@Service("ctyunSmsProvider")
public class CtyunSmsProvider extends AbstractSmsProvider {
    
    @Autowired
    private CtyunSmsProviderProperties properties;
    
    @Autowired
    private CtyunSmsRequestTranslator requestTranslator;

    @Autowired
    private CtyunSmsResponseTranslator responseTranslator;
    
    private RestTemplate restTemplate;
    
    /**
     * 默认API端点
     */
    private static final String DEFAULT_API_URL = "https://sms-global.ctapi.ctyun.cn/sms/api/v1/send";
    
    /**
     * 签名算法
     */
    private static final String HMAC_SHA256 = "HmacSHA256";
    
    @PostConstruct
    public void init() {
        log.info("天翼云短信平台初始化完成, apiUrl: {}, weight: {}", 
                properties.getApiUrl() != null ? properties.getApiUrl() : DEFAULT_API_URL,
                properties.getWeight());
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    @Async
    public CompletableFuture<SmsResponse> sendSms(SmsRequest smsRequest) throws SmsSendException {
        log.info("天翼云发送短信, phoneNumber: {}, templateCode: {}", 
                smsRequest.getPhoneNumber(), smsRequest.getTemplateCode());
        
        try {
            // 如果请求没有设置签名，使用配置的默认签名
            if (smsRequest.getSignName() == null || smsRequest.getSignName().isEmpty()) {
                smsRequest.setSignName(properties.getSignName());
            }
            
            // 1. 转换请求参数
            CtyunSmsRequestDTO requestDTO = requestTranslator.translate(smsRequest);
            
            // 2. 构建请求头
            HttpHeaders headers = buildHeaders();
            
            // 3. 构建请求体
            Map<String, Object> requestBody = buildRequestBody(requestDTO);
            
            // 4. 发送HTTP请求
            String apiUrl = properties.getApiUrl() != null ? properties.getApiUrl() : DEFAULT_API_URL;
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            log.debug("天翼云请求URL: {}", apiUrl);
            log.debug("天翼云请求体: {}", JSON.toJSONString(requestBody));
            
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            
            // 5. 解析响应
            String responseBody = responseEntity.getBody();
            log.debug("天翼云响应: {}", responseBody);
            
            CtyunSmsResponseDTO responseDTO = JSON.parseObject(responseBody, CtyunSmsResponseDTO.class);
            SmsResponse<CtyunSmsResponseDTO> result = responseTranslator.translate(responseDTO, smsRequest);
            
            log.info("天翼云短信发送完成, phoneNumber: {}, result: {}", 
                    smsRequest.getPhoneNumber(), result.getSendStatus());
            
            return CompletableFuture.completedFuture(result);
            
        } catch (Exception e) {
            log.error("天翼云短信发送失败, phoneNumber: {}, error: {}", 
                    smsRequest.getPhoneNumber(), e.getMessage(), e);
            
            SmsResponse<Object> errorResponse = SmsResponse.httpError(
                    smsRequest.getUniqueId(),
                    e.getMessage()
            );
            return CompletableFuture.completedFuture(errorResponse);
        }
    }
    
    /**
     * 构建请求头
     */
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        return headers;
    }
    
    /**
     * 构建请求体
     * 天翼云短信API请求体格式
     */
    private Map<String, Object> buildRequestBody(CtyunSmsRequestDTO requestDTO) throws SmsSendException {
        Map<String, Object> body = new HashMap<>();
        
        // 动作：发送短信
        body.put("action", "SendSms");
        
        // 构建请求参数
        Map<String, Object> request = new HashMap<>();
        request.put("PhoneNumber", requestDTO.getPhoneNumber());
        request.put("SignName", requestDTO.getSignName());
        request.put("TemplateCode", requestDTO.getTemplateCode());
        request.put("TemplateParam", requestDTO.getTemplateParam());
        
        if (requestDTO.getOutId() != null) {
            request.put("OutId", requestDTO.getOutId());
        }
        
        body.put("request", request);
        
        // 添加认证信息
        addAuthentication(body);
        
        return body;
    }
    
    /**
     * 添加认证信息
     * 使用HMAC-SHA256签名
     */
    private void addAuthentication(Map<String, Object> body) throws SmsSendException {
        try {
            String accessKey = properties.getAccessKey();
            String secretKey = properties.getSecretKey();
            
            if (accessKey == null || secretKey == null) {
                throw new SmsSendException("天翼云配置不完整: accessKey 或 secretKey 为空");
            }
            
            // 时间戳
            String timestamp = LocalDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            
            // 生成签名
            String signature = generateSignature(accessKey, secretKey, timestamp);
            
            // 认证信息
            Map<String, String> auth = new HashMap<>();
            auth.put("AccessKey", accessKey);
            auth.put("Timestamp", timestamp);
            auth.put("Signature", signature);
            auth.put("SignatureMethod", "HMAC-SHA256");
            auth.put("SignatureVersion", "1.0");
            
            body.put("auth", auth);
            
        } catch (Exception e) {
            throw new SmsSendException("构建认证信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 生成HMAC-SHA256签名
     */
    private String generateSignature(String accessKey, String secretKey, String timestamp) throws Exception {
        String stringToSign = accessKey + timestamp;
        
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        mac.init(secretKeySpec);
        
        byte[] hash = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
    
    @Override
    public boolean isHealthy() {
        // 简单健康检查：检查配置是否完整且启用
        try {
            return properties.isEnabled()
                    && properties.getAccessKey() != null 
                    && properties.getSecretKey() != null
                    && !properties.getAccessKey().isEmpty()
                    && !properties.getSecretKey().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getName() {
        return "ctyun";
    }
    
    @Override
    public int getWeight() {
        return properties.getWeight();
    }
}
