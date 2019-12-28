package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * WCS消息记录表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:11
 */
@Mapper
public interface WcsWcsmessagelogDao extends BaseMapper<WcsWcsmessagelogEntity> {
	
}
