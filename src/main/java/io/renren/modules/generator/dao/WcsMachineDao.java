package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsMachineEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备信息表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:12
 */
@Mapper
public interface WcsMachineDao extends BaseMapper<WcsMachineEntity> {
	
}
