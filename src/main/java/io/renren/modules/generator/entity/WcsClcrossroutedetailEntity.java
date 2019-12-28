package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 输送线交叉路径详情
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Data
@TableName("WCS_ClCrossRouteDetail")
public class WcsClcrossroutedetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 开始Block名称
	 */
	private String startBlockName;
	/**
	 * 经过运行Block名称
	 */
	private String runBlockName;
	/**
	 * 结束Block名称
	 */
	private String endBlockName;
	/**
	 * 目标站台
	 */
	private String endStation;
	/**
	 * 路线类型
	 */
	private Integer type;
	/**
	 * 重复设备路线
	 */
	private Boolean isSameBlockCrossRoute;

}
