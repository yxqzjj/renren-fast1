package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsDefaultlocationEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * 默认原点位置信息
 *
 * @author CalmLake
 * @date 2019/3/11 15:55
 */
@Repository("DefaultLocationDao")
@Mapper
public interface DefaultLocationDao {
    @Delete({
            "delete from WCS_DefaultLocation",
            "where trim(Block_Name) = #{blockName,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String blockName);

    @Insert({
            "insert into WCS_DefaultLocation (Block_Name, Default_Location)",
            "values (#{blockName,jdbcType=CHAR}, #{defaultLocation,jdbcType=CHAR})"
    })
    int insert(WcsDefaultlocationEntity record);

    @Select({
            "select",
            "Block_Name, Default_Location",
            "from WCS_DefaultLocation",
            "where trim(Block_Name)  = #{blockName,jdbcType=CHAR}"
    })
    @Results({
            @Result(column = "Block_Name", property = "blockName", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "Default_Location", property = "defaultLocation", jdbcType = JdbcType.CHAR)
    })
    WcsDefaultlocationEntity selectByPrimaryKey(String blockName);

    @Update({
            "update WCS_DefaultLocation",
            "set Default_Location = #{defaultLocation,jdbcType=CHAR}",
            "where trim(Block_Name)  = #{blockName,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsDefaultlocationEntity record);
}