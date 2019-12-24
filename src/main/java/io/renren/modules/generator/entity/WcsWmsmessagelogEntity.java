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
 * @date 2019-12-20 10:48:11
 */
@Data
@TableName("WCS_WMSMessageLog")
public class WcsWmsmessagelogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * $column.comments
	 */
	private String wmsId;
	/**
	 * $column.comments
	 */
	private Integer workPlanId;
	/**
	 * $column.comments
	 */
	private String type;
	/**
	 * $column.comments
	 */
	private Date createTime;
	/**
	 * $column.comments
	 */
	private String barcode;
	/**
	 * $column.comments
	 */
	private Integer status;
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
	/**
	 * $column.comments
	 */
	private String uuid;

}
