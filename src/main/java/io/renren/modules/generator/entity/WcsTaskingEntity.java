package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务分配表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@Data
@TableName("WCS_Tasking")
public class WcsTaskingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * mckey
	 */
	private String mckey;
	/**
	 * block名称
	 */
	private String blockName;
	/**
	 * 下一个block名称
	 */
	private String nextBlockName;
	/**
	 * 创建时间
	 */
	private Date createTime;
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
	 * 工作计划类型
	 */
	private Integer workPlanType;
	/**
	 * 母车数量
	 */
	private Integer mlMcNum;
	/**
	 * 仓库编号
	 */
	private Integer warehouseNo;
	/**
	 * 运行block名称
	 */
	private String runBlockName;
	/**
	 * 工作设备类型
	 */
	private Integer machineType;
	/**
	 * 目标状态
	 */
	private String toStation;

}
