package com.sms.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信模板
 * 
 * @author greyson
 * @email ouyangguanling@ssc-hn.com
 * @date 2024-01-12 11:02:58
 */
@Data
@TableName("cp_sms_template")
public class SmsTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 模板唯一标识
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 模板名字
	 */
	private String name;
	/**
	 * 短信内容模板
	 */
	private String templateText;
	/**
	 * 模板动态参数
	 */
	private String parameters;
	/**
	 * 记录创建时间
	 */
	private Date createTime;
	/**
	 * 记录更新时间
	 */
	private Date updateTime;
	/**
	 * 请求标识
	 */
	private String reqCode;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 模板状态
	 */
	private String status;
	/**
	 * 短信服务商
	 */
	private String smsProvider;

	private String businessCode;

}
