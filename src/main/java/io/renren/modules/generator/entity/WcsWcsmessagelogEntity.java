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
 * @date 2019-12-20 11:06:48
 */
@Data
@TableName("WCS_WCSMessageLog")
public class WcsWcsmessagelogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * $column.comments
	 */
	private String plcName;
	/**
	 * $column.comments
	 */
	private Date createTime;
	/**
	 * $column.comments
	 */
	private Integer type;
	/**
	 * $column.comments
	 */
	private String message;
	/**
	 * $column.comments
	 */
	private String reserved1;
	/**
	 * $column.comments
	 */
	private String reserved2;

}
