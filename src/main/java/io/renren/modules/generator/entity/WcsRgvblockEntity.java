package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ${comments}
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-12-20 09:26:00
 */
@Data
@TableName("WCS_RGVBlock")
public class WcsRgvblockEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * block名称
	 */
	private String name;
	/**
	 * Mckey
	 */
	private String mckey;
	/**
	 * 预约Mckey
	 */
	private String appointmentMckey;
	/**
	 * 当前执行命令
	 */
	private String command;
	/**
	 * 异常吗
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
	 * 当前交互设备
	 */
	private String withWorkBlockName;
	/**
	 * 停靠设备
	 */
	private String berthBlockName;
	/**
	 * 预留字段1
	 */
	private String reserved1;
	/**
	 * 预留字段2
	 */
	private String reserved2;

}
