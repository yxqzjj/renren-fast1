package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 路径起止表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@Data
@TableName("WCS_RouteStationStartEnd")
public class WcsRoutestationstartendEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 起始站台
	 */
	private String fromStation;
	/**
	 * 终点站台
	 */
	private String endStation;
	/**
	 * 类型
	 */
	private Integer type;

}
