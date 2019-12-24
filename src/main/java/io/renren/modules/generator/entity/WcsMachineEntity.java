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
 * @date 2019-12-20 16:39:45
 */
@Data
@TableName("WCS_Machine")
public class WcsMachineEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * $column.comments
	 */
	private String name;
	/**
	 * $column.comments
	 */
	private String blockName;
	/**
	 * $column.comments
	 */
	private String plcName;
	/**
	 * $column.comments
	 */
	private Integer type;
	/**
	 * $column.comments
	 */
	private String stationName;
	/**
	 * $column.comments
	 */
	private String dockName;
	/**
	 * $column.comments
	 */
	private String reserved1;
	/**
	 * $column.comments
	 */
	private String reserved2;
	/**
	 * $column.comments
	 */
	private Integer warehouseNo;
	/**
	 * $column.comments
	 */
	private Boolean taskFlag;

}
