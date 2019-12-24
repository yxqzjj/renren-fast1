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
 * @date 2019-12-20 10:56:28
 */
@Data
@TableName("WCS_CommandLog")
public class WcsCommandlogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer id;
	/**
	 * $column.comments
	 */
	private String command;
	/**
	 * $column.comments
	 */
	private String seqNo;
	/**
	 * $column.comments
	 */
	private Date createTime;
	/**
	 * $column.comments
	 */
	private String blockName;
	/**
	 * $column.comments
	 */
	private String cycleCommand;
	/**
	 * $column.comments
	 */
	private String cycleType;
	/**
	 * $column.comments
	 */
	private String mckey;
	/**
	 * $column.comments
	 */
	private String station;
	/**
	 * $column.comments
	 */
	private String dock;
	/**
	 * $column.comments
	 */
	private String tier;
	/**
	 * $column.comments
	 */
	private String line;
	/**
	 * $column.comments
	 */
	private String row;
	/**
	 * $column.comments
	 */
	private String load;
	/**
	 * $column.comments
	 */
	private String ackType;
	/**
	 * $column.comments
	 */
	private String errorType;
	/**
	 * $column.comments
	 */
	private String finishType;
	/**
	 * $column.comments
	 */
	private String resend;
	/**
	 * $column.comments
	 */
	private String reserved1;
	/**
	 * $column.comments
	 */
	private String reserved2;

}
