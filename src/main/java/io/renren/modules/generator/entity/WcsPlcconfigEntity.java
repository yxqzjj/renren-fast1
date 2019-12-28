package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * plc配置表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@Data
@TableName("WCS_PlcConfig")
public class WcsPlcconfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * plc名称
	 */
	private String name;
	/**
	 * ip地址
	 */
	private String ip;
	/**
	 * 端口号
	 */
	private Integer port;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 心跳
	 */
	private Date heartbeatTime;
	/**
	 * 预留字段
	 */
	private String reserved1;

}
