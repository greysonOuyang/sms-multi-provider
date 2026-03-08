package org.moretea.sms.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moretea.sms.api.domain.SmsRequest;
import org.moretea.sms.api.domain.SmsResponse;
import org.moretea.sms.facade.sender.SmsFacade;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送演示 Controller
 */
@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class SmsDemoController {

    private final SmsFacade smsFacade;

    /**
     * 最简单的发送方式
     * 
     * GET /demo/send/simple?phone=13800138000&message=hello
     */
    @GetMapping("/send/simple")
    public Map<String, Object> sendSimple(
            @RequestParam String phone,
            @RequestParam String message) {
        
        log.info("简单发送请求: phone={}, message={}", phone, message);
        
        SmsResponse response = smsFacade.send(phone, message);
        
        return convertToMap(response);
    }

    /**
     * 使用模板发送
     * 
     * POST /demo/send/template
     * {
     *   "phone": "13800138000",
     *   "templateCode": "SMS_123456",
     *   "params": {"code": "1234"}
     * }
     */
    @PostMapping("/send/template")
    public Map<String, Object> sendTemplate(@RequestBody TemplateRequest request) {
        
        log.info("模板发送请求: phone={}, templateCode={}", 
                request.getPhone(), request.getTemplateCode());
        
        SmsRequest smsRequest = SmsRequest.builder()
                .phoneNumber(request.getPhone())
                .templateCode(request.getTemplateCode())
                .templateParams(request.getParams())
                .signName(request.getSignName())  // 可选，覆盖默认签名
                .build();
        
        SmsResponse response = smsFacade.sendTemplate(smsRequest);
        
        return convertToMap(response);
    }

    /**
     * 批量发送演示
     * 
     * POST /demo/send/batch
     * {
     *   "templateCode": "SMS_123456",
     *   "phones": ["13800138000", "13800138001"],
     *   "params": {"code": "1234"}
     * }
     */
    @PostMapping("/send/batch")
    public Map<String, Object> sendBatch(@RequestBody BatchRequest request) {
        
        log.info("批量发送请求: templateCode={}, phones={}", 
                request.getTemplateCode(), request.getPhones().size());
        
        Map<String, Object> result = new HashMap<>();
        
        for (String phone : request.getPhones()) {
            SmsRequest smsRequest = SmsRequest.builder()
                    .phoneNumber(phone)
                    .templateCode(request.getTemplateCode())
                    .templateParams(request.getParams())
                    .build();
            
            SmsResponse response = smsFacade.sendTemplate(smsRequest);
            result.put(phone, convertToMap(response));
        }
        
        return result;
    }

    /**
     * 将 SmsResponse 转换为 Map
     */
    private Map<String, Object> convertToMap(SmsResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", response.getSendStatus());
        map.put("status", response.getStatus());
        map.put("message", response.getMsg());
        map.put("uniqueId", response.getUniqueId());
        if (response.getData() != null) {
            map.put("data", response.getData().toString());
        }
        return map;
    }

    /**
     * 模板请求 DTO
     */
    @lombok.Data
    public static class TemplateRequest {
        private String phone;
        private String templateCode;
        private String signName;
        private Map<String, String> params;
    }

    /**
     * 批量请求 DTO
     */
    @lombok.Data
    public static class BatchRequest {
        private String templateCode;
        private java.util.List<String> phones;
        private Map<String, String> params;
    }
}
