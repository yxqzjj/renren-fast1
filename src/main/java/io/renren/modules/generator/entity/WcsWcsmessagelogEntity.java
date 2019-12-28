package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * WCS消息记录表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@Data
@TableName("WCS_WCSMessageLog")
public class WcsWcsmessagelogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * Plc名称
	 */
	private String plcName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 消息类型
	 */
	private Integer type;
	/**
	 * 消息
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

}
