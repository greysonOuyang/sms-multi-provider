package com.sms.facade.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.api.domain.SmsHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信发送历史记录
 * 
 * @author greyson
 * @email ouyangguanling@ssc-hn.com
 * @date 2024-01-23 15:37:17
 */
@Mapper
public interface SmsHistoryMapper extends BaseMapper<SmsHistoryEntity> {
	
}
