package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * plc配置表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@Mapper
public interface WcsPlcconfigDao extends BaseMapper<WcsPlcconfigEntity> {
	
}
