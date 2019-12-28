package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 设备信息表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@Data
@TableName("WCS_Machine")
public class WcsMachineEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 设备名称
	 */
	private String name;
	/**
	 * block名称
	 */
	private String blockName;
	/**
	 * plc名称
	 */
	private String plcName;
	/**
	 * 设备类型
	 */
	private Integer type;
	/**
	 * 站台名称
	 */
	private String stationName;
	/**
	 * 码头名称
	 */
	private String dockName;
	/**
	 * 预留字段1
	 */
	private String reserved1;
	/**
	 * 预留字段2
	 */
	private String reserved2;
	/**
	 * 仓库编号
	 */
	private Integer warehouseNo;
	/**
	 * 任务分配标识
	 */
	private Boolean taskFlag;

}
