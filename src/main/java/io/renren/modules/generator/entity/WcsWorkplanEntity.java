package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 工作计划表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:10
 */
@Data
@TableName("WCS_WorkPlan")
public class WcsWorkplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * Mckey
	 */
	private String mckey;
	/**
	 * 工作类型
	 */
	private Integer type;
	/**
	 * 托盘号
	 */
	private String barcode;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 完成时间
	 */
	private Date finishTime;
	/**
	 * 工作状态
	 */
	private Integer status;
	/**
	 * 起始站台
	 */
	private String fromStation;
	/**
	 * 终止站台
	 */
	private String toStation;
	/**
	 * 起始货位
	 */
	private String fromLocation;
	/**
	 * 目标货位
	 */
	private String toLocation;
	/**
	 * WMS工作标识
	 */
	private String wmsFlag;
	/**
	 * 优先级
	 */
	private Integer priorityConfigPriority;
	/**
	 * 预留字段1
	 */
	private String reserved1;
	/**
	 * 预留字段2
	 */
	private String reserved2;
	/**
	 * 库区编号
	 */
	private Integer warehouseNo;

}
