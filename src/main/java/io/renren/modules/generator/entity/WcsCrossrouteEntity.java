package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 交叉路径信息
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Data
@TableName("WCS_CrossRoute")
public class WcsCrossrouteEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	private Integer id;
	/**
	 * 运行Block名称
	 */
	private String runBlockName;
	/**
	 * 模式
	 */
	private Integer mode;
	/**
	 * 最大载荷数量
	 */
	private Integer maxLoadNum;
	/**
	 * 当前载荷数量
	 */
	private Integer loadNum;

}
