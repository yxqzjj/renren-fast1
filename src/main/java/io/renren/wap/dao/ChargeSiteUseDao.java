package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsChargesiteuseEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 充电位信息
 *
 * @author CalmLake
 * @date 2019/4/17 17:50
 */
@Mapper
@Repository("ChargeSiteUseDao")
public interface ChargeSiteUseDao {

    /**
     * 根据id查找充电位信息
     *
     * @param id 序号
     * @return com.wap.entity.ChargeSiteUse
     * @author CalmLake
     * @date 2019/4/18 10:45
     */
    @Select({
            "select",
            " * ",
            "from WCS_ChargeSiteUse",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    @Results(id = "chargeSiteUse", value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "Row", property = "row", jdbcType = JdbcType.SMALLINT),
            @Result(column = "Line", property = "line", jdbcType = JdbcType.SMALLINT),
            @Result(column = "Tier", property = "tier", jdbcType = JdbcType.SMALLINT),
            @Result(column = "Block_Name", property = "blockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Location", property = "location", jdbcType = JdbcType.VARCHAR)
    })
    WcsChargesiteuseEntity selectByPrimaryKey(Integer id);

    /**
     * 获取当前充电货位
     *
     * @param blockName 数据block名称
     * @return com.wap.entity.ChargeSiteUse
     * @author CalmLake
     * @date 2019/4/17 18:13
     */
    @Select({
            "select",
            " * ",
            "from WCS_ChargeSiteUse",
            "where trim(Block_Name) = #{blockName,jdbcType=CHAR}"
    })
    @ResultMap("chargeSiteUse")
    WcsChargesiteuseEntity selectByBlockName(@Param("blockName") String blockName);

    /**
     * 获取充电位信息
     *
     * @return java.util.List<com.wap.entity.ChargeSiteUse>
     * @author CalmLake
     * @date 2019/4/17 17:58
     */
    @Select({
            "select",
            " * ",
            "from WCS_ChargeSiteUse"
    })
    @ResultMap("chargeSiteUse")
    List<WcsChargesiteuseEntity> getList();

    /**
     * 根据货位修改
     *
     * @param blockName 设备名称
     * @param id        序号
     * @return int
     * @author CalmLake
     * @date 2019/4/17 17:55
     */
    @Update({
            "update WCS_ChargeSiteUse",
            "set \"BLOCK_NAME\" = #{blockName,jdbcType=CHAR}",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateBlockNameByLocation(@Param("blockName") String blockName, @Param("id") int id);
}