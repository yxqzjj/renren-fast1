package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 升降机数据block表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Data
@TableName("WCS_ALBlock")
public class WcsAlblockEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 数据block名称
	 */
	private String name;
	/**
	 * 搬运货物唯一标识
	 */
	private String mckey;
	/**
	 * 预约搬运货物唯一标识
	 */
	private String appointmentMckey;
	/**
	 * 当前执行命令
	 */
	private String command;
	/**
	 * 异常码
	 */
	private String errorCode;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 载荷状态
	 */
	private Boolean isLoad;
	/**
	 * 一起工作数据block名称
	 */
	private String withWorkBlockName;
	/**
	 * 停泊位置设备数据Block名称
	 */
	private String berthBlockName;
	/**
	 * 层
	 */
	private String tier;
	/**
	 * 预留字段1（预约任务交互设备名称）
	 */
	private String reserved1;
	/**
	 * 预留字段2
	 */
	private String reserved2;

}
