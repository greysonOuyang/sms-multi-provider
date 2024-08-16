package com.sms.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短信发送历史记录
 * 
 * @author greyson
 * @email  
 * @date 2024-01-23 15:37:17
 */
@Data
//@TableName("sms_history")
@Accessors(chain = true)
public class SmsHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 模板表主键ID
	 */
	private Long templateId;
	/**
	 * 业务编码
	 */
	private String businessCode;
	/**
	 * 发送人
	 */
	private Long senderId;
	/**
	 * 发送人
	 */
	private String senderName;
	/**
	 * 接收人
	 */
	private String receiverName;
	/**
	 * 接收人电话
	 */
	private String receiverPhone;
	/**
	 * 发送结果，成功/失败
	 */
	private Boolean result;
	/**
	 * 回执消息
	 */
	private String message;
	/**
	 * 其余回执信息
	 */
	private String data;
	/**
	 * 跟踪ID
	 */
	private String uniqueId;
	/**
	 * 发送的参数
	 */
	private String params;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;
}
