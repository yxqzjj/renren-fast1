package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsAutocreateworkplanEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * 自动演示信息
 *
 * @author CalmLake
 * @date 2019/9/2 14:09
 */
@Repository("AutoCreateWorkPlanDao")
@Mapper
public interface AutoCreateWorkPlanDao {
    @Delete({
        "delete from WCS_AutoCreateWorkPlan",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into WCS_AutoCreateWorkPlan (ID, Location_A, ",
        "Location_B, Cargo_Num, ",
        "Switch_Mode, Status, ",
        "Type, Station_A, Station_B)",
        "values (#{id,jdbcType=INTEGER}, #{locationA,jdbcType=CHAR}, ",
        "#{locationB,jdbcType=CHAR}, #{cargoNum,jdbcType=INTEGER}, ",
        "#{switchMode,jdbcType=BIT}, #{status,jdbcType=TINYINT}, ",
        "#{type,jdbcType=TINYINT}, #{stationA,jdbcType=CHAR}, #{stationB,jdbcType=CHAR})"
    })
    int insert(WcsAutocreateworkplanEntity record);

    @Select({
        "select",
        "ID, Location_A, Location_B, Cargo_Num, Switch_Mode, Status, Type, Station_A, ",
        "Station_B",
        "from WCS_AutoCreateWorkPlan",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="Location_A", property="locationA", jdbcType=JdbcType.CHAR),
        @Result(column="Location_B", property="locationB", jdbcType=JdbcType.CHAR),
        @Result(column="Cargo_Num", property="cargoNum", jdbcType=JdbcType.INTEGER),
        @Result(column="Switch_Mode", property="switchMode", jdbcType=JdbcType.BIT),
        @Result(column="Status", property="status", jdbcType=JdbcType.TINYINT),
        @Result(column="Type", property="type", jdbcType=JdbcType.TINYINT),
        @Result(column="Station_A", property="stationA", jdbcType=JdbcType.CHAR),
        @Result(column="Station_B", property="stationB", jdbcType=JdbcType.CHAR)
    })
    WcsAutocreateworkplanEntity selectByPrimaryKey(Integer id);

    @Update({
        "update WCS_AutoCreateWorkPlan",
        "set Location_A = #{locationA,jdbcType=CHAR},",
          "Location_B = #{locationB,jdbcType=CHAR},",
          "Cargo_Num = #{cargoNum,jdbcType=INTEGER},",
          "Switch_Mode = #{switchMode,jdbcType=BIT},",
          "Status = #{status,jdbcType=TINYINT},",
          "Type = #{type,jdbcType=TINYINT},",
          "Station_A = #{stationA,jdbcType=CHAR},",
          "Station_B = #{stationB,jdbcType=CHAR}",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WcsAutocreateworkplanEntity record);
}