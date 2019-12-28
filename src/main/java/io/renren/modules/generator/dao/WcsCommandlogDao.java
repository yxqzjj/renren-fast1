package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsCommandlogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 命令消息记录表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Mapper
public interface WcsCommandlogDao extends BaseMapper<WcsCommandlogEntity> {
	
}
