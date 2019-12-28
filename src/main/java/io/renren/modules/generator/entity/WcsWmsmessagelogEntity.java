package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * WMS消息记录表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:40:18
 */
@Data
@TableName("WCS_WMSMessageLog")
public class WcsWmsmessagelogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * WMS任务唯一标识
	 */
	private String wmsId;
	/**
	 * 工作计划序号
	 */
	private Integer workPlanId;
	/**
	 * 消息类型
	 */
	private String type;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 托盘号
	 */
	private String barcode;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 内容
	 */
	private String message;
	/**
	 * 预留字段1
	 */
	private String reserved1;
	/**
	 * 预留字段2
	 */
	private String reserved2;
	/**
	 * 消息标识
	 */
	private String uuid;

}
