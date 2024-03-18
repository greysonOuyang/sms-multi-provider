# INTRODUCE

# 模板配置
1. 数据库方式
```dtd
CREATE TABLE `cp_sms_history` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`template_id` BIGINT(20) NOT NULL COMMENT '模板表主键ID',
	`business_code` VARCHAR(50) NOT NULL COMMENT '业务编码' COLLATE 'utf8_general_ci',
	`sender_id` BIGINT(20) NOT NULL COMMENT '发送人',
	`sender_name` VARCHAR(50) NOT NULL COMMENT '发送人' COLLATE 'utf8_general_ci',
	`receiver_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '接收人' COLLATE 'utf8_general_ci',
	`receiver_phone` VARCHAR(50) NULL DEFAULT NULL COMMENT '接收人电话' COLLATE 'utf8_general_ci',
	`result` TINYINT(4) NULL DEFAULT '0' COMMENT '发送结果，成功/失败',
	`message` VARCHAR(50) NULL DEFAULT NULL COMMENT '回执消息' COLLATE 'utf8_general_ci',
	`data` VARCHAR(500) NULL DEFAULT NULL COMMENT '其余回执信息' COLLATE 'utf8_general_ci',
	`unique_id` VARCHAR(255) NOT NULL COMMENT '跟踪ID' COLLATE 'utf8_general_ci',
	`params` VARCHAR(500) NULL DEFAULT NULL COMMENT '发送的参数' COLLATE 'utf8_general_ci',
	`create_time` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `idx_unique+id` (`unique_id`) USING BTREE
)
COMMENT='短信发送历史记录'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=21
;

        CREATE TABLE `cp_sms_template` (
        `id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '模板唯一标识',
        `name` VARCHAR(255) NOT NULL COMMENT '模板名字' COLLATE 'utf8_general_ci',
        `template_text` TEXT NOT NULL COMMENT '短信内容模板' COLLATE 'utf8_general_ci',
        `parameters` VARCHAR(500) NULL DEFAULT NULL COMMENT '模板动态参数' COLLATE 'utf8_general_ci',
        `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
        `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
        `req_code` VARCHAR(50) NOT NULL COMMENT '请求标识' COLLATE 'utf8_general_ci',
        `description` TEXT NULL DEFAULT NULL COMMENT '描述' COLLATE 'utf8_general_ci',
        `status` VARCHAR(20) NULL DEFAULT 'active' COMMENT '模板状态' COLLATE 'utf8_general_ci',
        `sms_provider` VARCHAR(50) NULL DEFAULT NULL COMMENT '短信服务商' COLLATE 'utf8_general_ci',
        `business_code` VARCHAR(50) NULL DEFAULT NULL COMMENT '业务标识' COLLATE 'utf8_general_ci',
        PRIMARY KEY (`id`) USING BTREE,
        UNIQUE INDEX `union_idx_provider_business_code` (`sms_provider`, `business_code`) USING BTREE
        )
        COMMENT='短信模板'
        COLLATE='utf8_general_ci'
        ENGINE=InnoDB
        ;


```