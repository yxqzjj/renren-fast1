package io.renren.modules.generator.dao;

import io.renren.modules.generator.dao.provider.WorkPlanProvider;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * 工作计划表
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:10
 */
@Mapper
public interface WcsWorkplanDao extends BaseMapper<WcsWorkplanEntity> {
    @InsertProvider(type = WorkPlanProvider.class, method = "insertProvider")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertProvider(WcsWorkplanEntity workPlan);	
}
