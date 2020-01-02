package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsChargeEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * 充电信息
 *
 * @author CalmLake
 * @date 2019/3/20 11:07
 */
@Repository("ChargeDao")
@Mapper
public interface ChargeDao {
    @Delete({
            "delete from WCS_Charge",
            "where trim(Block_Name) = #{blockName,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String blockName);

    @Insert({
            "insert into WCS_Charge (Block_Name, Type, ",
            "Charge_Block_Name, Location, ",
            "Reserved1, Reserved2)",
            "values (#{blockName,jdbcType=CHAR}, #{type,jdbcType=TINYINT}, ",
            "#{chargeBlockName,jdbcType=CHAR}, #{location,jdbcType=VARCHAR}, ",
            "#{reserved1,jdbcType=VARCHAR}, #{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsChargeEntity record);

    @Select({
            "select",
            "Block_Name, Type, Charge_Block_Name, Location, Reserved1, Reserved2",
            "from WCS_Charge",
            "where trim(Block_Name) = #{blockName,jdbcType=CHAR}"
    })
    @Results({
            @Result(column = "Block_Name", property = "blockName", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "Type", property = "type", jdbcType = JdbcType.TINYINT),
            @Result(column = "Charge_Block_Name", property = "chargeBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Location", property = "location", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    WcsChargeEntity selectByPrimaryKey(String blockName);

    @Update({
            "update WCS_Charge",
            "set Type = #{type,jdbcType=TINYINT},",
            "Charge_Block_Name = #{chargeBlockName,jdbcType=CHAR},",
            "Location = #{location,jdbcType=VARCHAR},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR},",
            "Reserved2 = #{reserved2,jdbcType=VARCHAR}",
            "where Block_Name = #{blockName,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsChargeEntity record);
}