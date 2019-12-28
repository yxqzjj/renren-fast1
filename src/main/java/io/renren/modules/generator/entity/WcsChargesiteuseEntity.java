package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 充电位使用状态表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Data
@TableName("WCS_ChargeSiteUse")
public class WcsChargesiteuseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 排
	 */
	private Integer row;
	/**
	 * 列
	 */
	private Integer line;
	/**
	 * 层
	 */
	private Integer tier;
	/**
	 * Block名称
	 */
	private String blockName;
	/**
	 * 货位
	 */
	private String location;

}
