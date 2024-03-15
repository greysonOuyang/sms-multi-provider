package com.sms.facade.domain;

import com.sms.api.SmsProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/3/15
 * Time: 10:48
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    private String phoneNumber;
    private String message;
    private SmsProvider chosenProvider;
}
