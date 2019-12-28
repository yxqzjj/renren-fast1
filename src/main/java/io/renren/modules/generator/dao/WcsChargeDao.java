package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsChargeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充电信息表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Mapper
public interface WcsChargeDao extends BaseMapper<WcsChargeEntity> {
	
}
