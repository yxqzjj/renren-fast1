package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 演示模式
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Data
@TableName("WCS_AutoCreateWorkPlan")
public class WcsAutocreateworkplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 货位A
	 */
	private String locationA;
	/**
	 * 货位B
	 */
	private String locationB;
	/**
	 * 货物数量
	 */
	private Integer cargoNum;
	/**
	 * 开关模式
	 */
	private Boolean switchMode;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 站台A
	 */
	private String stationA;
	/**
	 * 站台B
	 */
	private String stationB;

}
