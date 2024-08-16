package com.sms.test.controller;

import com.sms.api.domain.SmsRequest;
import com.sms.api.exception.SmsCommonException;
import com.sms.api.exception.SmsSendException;
import com.sms.facade.sender.SmsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试短信发送
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/8/15
 * Time: 16:20
 */
@RestController
@RequestMapping("sms/test")
public class TestSmsController {

    @Autowired
    private SmsFacade smsFacade;


    @GetMapping("/send")
    public String send() {
        SmsRequest smsRequest = SmsRequest.builder().phoneNumber("15270661017").build();
        try {
            smsFacade.sendByTemplateSimple(smsRequest);
        } catch (SmsSendException e) {
            throw new RuntimeException("发送失败");
        } catch (SmsCommonException e) {
            throw new RuntimeException("发送失败");
        }
        return "success";
    }
}
