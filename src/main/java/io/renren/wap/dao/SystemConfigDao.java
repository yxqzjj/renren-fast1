package io.renren.wap.dao;

import io.renren.modules.generator.entity.WcsSystemconfigEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统配置
 * @author CalmLake
 * @date 2019/1/9 14:29
 */
@Repository("SystemConfigDao")
@Mapper
public interface SystemConfigDao {
    @Insert({
        "insert into WCS_SystemConfig (Name, Value)",
        "values (#{name,jdbcType=VARCHAR}, #{value,jdbcType=VARCHAR})"
    })
    int insert(WcsSystemconfigEntity record);

}