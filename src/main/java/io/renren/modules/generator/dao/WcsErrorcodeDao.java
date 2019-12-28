package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsErrorcodeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 故障字典表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@Mapper
public interface WcsErrorcodeDao extends BaseMapper<WcsErrorcodeEntity> {
	
}
