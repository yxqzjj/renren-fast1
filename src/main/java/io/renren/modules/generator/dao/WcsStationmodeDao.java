package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsStationmodeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 站台模式表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@Mapper
public interface WcsStationmodeDao extends BaseMapper<WcsStationmodeEntity> {
	
}
