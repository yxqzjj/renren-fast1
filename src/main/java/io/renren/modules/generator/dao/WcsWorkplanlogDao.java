package io.renren.modules.generator.dao;

import io.renren.modules.generator.dao.provider.WorkPlanLogSqlProvider;
import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * 工作计划信息记录
 * 
 * @author yxq
 * @email yxq@163.com
 * @date 2019-12-27 12:11:10
 */
@Mapper
public interface WcsWorkplanlogDao extends BaseMapper<WcsWorkplanlogEntity> {
    @InsertProvider(type = WorkPlanLogSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int InsertProvider(WcsWorkplanlogEntity record);

}
