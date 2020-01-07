package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.renren.wap.cache.BlockCache;
import io.renren.wap.client.util.MessageDetailUtil;
import lombok.Data;

/**
 * 输送线数据block表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Data
@TableName("WCS_CLBlock")
public class WcsClblockEntity extends Block implements Serializable {
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
	 * 当前执行命令
	 */
	@TableField(exist = false)
	private String cleCommand = BlockCache.getString(this.getName()) == null ? "" : MessageDetailUtil.getCycleCommandDetail(BlockCache.getString(this.getName()));

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
	 * 状态码
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
	 * Reserved1
	 */
	private String reserved1;
	/**
	 * Reserved2
	 */
	private String reserved2;

}
