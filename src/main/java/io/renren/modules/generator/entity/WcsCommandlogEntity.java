package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 命令消息记录表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Data
@TableName("WCS_CommandLog")
public class WcsCommandlogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 命令
	 */
	private String command;
	/**
	 * 消息序号
	 */
	private String seqNo;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 数据block名称
	 */
	private String blockName;
	/**
	 * Cycle命令
	 */
	private String cycleCommand;
	/**
	 * Cycle区分
	 */
	private String cycleType;
	/**
	 * 搬运货物唯一标识
	 */
	private String mckey;
	/**
	 * 站台
	 */
	private String station;
	/**
	 * 码头
	 */
	private String dock;
	/**
	 * 层
	 */
	private String tier;
	/**
	 * 列
	 */
	private String line;
	/**
	 * 排
	 */
	private String row;
	/**
	 * 载荷状态
	 */
	private String load;
	/**
	 * 应答区分
	 */
	private String ackType;
	/**
	 * 异常区分
	 */
	private String errorType;
	/**
	 * 完成区分
	 */
	private String finishType;
	/**
	 * 重发标识
	 */
	private String resend;
	/**
	 * 预留字段1
	 */
	private String reserved1;
	/**
	 * 预留字段2
	 */
	private String reserved2;

}
