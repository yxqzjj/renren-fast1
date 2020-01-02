package io.renren.wap.dao;

import io.renren.modules.generator.entity.WcsStationmodeEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * 站台模式
 *
 * @author CalmLake
 * @date 2019/1/9 14:28
 */
@Repository("StationModeDao")
@Mapper
public interface StationModeDao {
    @Delete({
            "delete from WCS_StationMode",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    @Insert({
            "insert into WCS_StationMode (Name, Mode)",
            "values (#{name,jdbcType=CHAR}, #{mode,jdbcType=TINYINT})"
    })
    int insert(WcsStationmodeEntity record);

    @Select({
            "select",
            "Name, Mode",
            "from WCS_StationMode",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    @Results({
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "Mode", property = "mode", jdbcType = JdbcType.TINYINT)
    })
    WcsStationmodeEntity selectByPrimaryKey(String name);

    @Update({
            "update WCS_StationMode",
            "set Mode = #{mode,jdbcType=TINYINT}",
            "where trim(Name) = #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsStationmodeEntity record);
}