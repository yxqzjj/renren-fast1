package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WcsBlocknameEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 不可分配任务数据Block表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:13
 */
@Mapper
public interface WcsBlocknameDao extends BaseMapper<WcsBlocknameEntity> {
	
}
