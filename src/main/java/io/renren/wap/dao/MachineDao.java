package io.renren.wap.dao;



import io.renren.modules.generator.entity.WcsMachineEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备信息
 *
 * @author CalmLake
 * @date 2019/1/8 10:29
 */
@Repository("MachineDao")
@Mapper
public interface MachineDao {
    @Delete({
            "delete from WCS_Machine",
            "where trim(Name)  = #{name,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String name);

    /**
     * 获取所有设备信息
     *
     * @return java.util.List<com.wap.entity.Machine>
     * @author CalmLake
     * @date 2019/2/26 16:46
     */
    @Select({
            "select * from WCS_Machine"
    })
    @Results(id = "machine", value = {
            @Result(column = "Name", property = "name", jdbcType = JdbcType.CHAR, id = true),
            @Result(column = "Block_Name", property = "blockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Plc_Name", property = "plcName", jdbcType = JdbcType.CHAR),
            @Result(column = "Type", property = "type", jdbcType = JdbcType.TINYINT),
            @Result(column = "Station_Name", property = "stationName", jdbcType = JdbcType.CHAR),
            @Result(column = "Dock_Name", property = "dockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Warehouse_No", property = "warehouseNo", jdbcType = JdbcType.SMALLINT),
            @Result(column = "Task_Flag", property = "taskFlag", jdbcType = JdbcType.BIT)
    })
    List<WcsMachineEntity> getList();

    /**
     * 查询所有站台名称
     *
     * @return java.util.List<com.wap.entity.Machine>
     * @author CalmLake
     * @date 2019/2/26 16:46
     */
    @Select({
            "select",
            " Station_Name ",
            "from WCS_Machine",
            "where trim(Station_Name)  !=' '   group by Station_Name "
    })
    @ResultMap("machine")
    List<WcsMachineEntity> selectStationList();

    /**
     * 查询所有plc名称
     *
     * @return java.util.List<com.wap.entity.Machine>
     * @author CalmLake
     * @date 2019/2/26 16:46
     */
    @Select({
            "select",
            " Plc_Name ",
            "from WCS_Machine",
            "where trim(Plc_Name)   !=' '  group by Plc_Name "
    })
    @ResultMap("machine")
    List<WcsMachineEntity> selectPlcList();

    /**
     * 查询所有block
     *
     * @return java.util.List<com.wap.entity.Machine>
     * @author CalmLake
     * @date 2019/2/26 16:46
     */
    @Select({
            "select",
            " Block_Name,Plc_Name ",
            "from WCS_Machine",
            "where trim(Block_Name)  !=' '   group by Block_Name,Plc_Name "
    })
    @ResultMap("machine")
    List<WcsMachineEntity> selectBlockList();

    /**
     * 查询所有运行block
     *
     * @return java.util.List<com.wap.entity.Machine>
     * @author CalmLake
     * @date 2019/2/26 16:46
     */
    @Select({
            "select",
            " Reserved1 ",
            "from WCS_Machine",
            "where trim(Reserved1)  !=' '   group by Reserved1 "
    })
    @ResultMap("machine")
    List<WcsMachineEntity> selectRunBlockList();

    /**
     * 根据运行block查找对应的plcName
     *
     * @param reserved1 运行block名称
     * @author CalmLake
     * @date 2019/2/26 17:34
     */
    @Select({
            "select      *  from WCS_Machine" +
                    " where   trim(Reserved1)  =( " +
                    "select     Reserved1  from WCS_Machine " +
                    " where  trim(Reserved1)   = #{reserved1,jdbcType=VARCHAR} and  rownum <= 1 ) and  rownum <= 1"
    })
    @ResultMap("machine")
    WcsMachineEntity selectPlcName(@Param("reserved1") String reserved1);

    /**
     * 根据机器名称查询该名称对应的机器信息的所有字段
     *
     * @param name 机器名称
     * @return 当前机器信息
     */
    @Select({
            "select",
            "Name, Block_Name, Plc_Name, Type, Station_Name, Dock_Name, Reserved1, Reserved2",
            "from WCS_Machine",
            "where trim(Name)   = #{name,jdbcType=CHAR}"
    })
    @ResultMap("machine")
    WcsMachineEntity selectByPrimaryKey(String name);

    /**
     * 根据数据block名称查询该名称对应的机器信息的所有字段
     *
     * @param blockName 数据block名称
     * @return 当前机器信息
     */
    @Select({
            "select    *",
            "from WCS_Machine",
            "where trim(Block_Name)   = #{blockName,jdbcType=CHAR} and  rownum <= 1"
    })
    @ResultMap("machine")
    WcsMachineEntity selectByBlockName(String blockName);

    /**
     * 根据站台名称查询该名称对应的机器信息的所有字段
     *
     * @param stationName 站台名称
     * @return 当前机器信息
     */
    @Select({
            "select",
            " *",
            "from WCS_Machine",
            "where  trim(Station_Name)  = #{stationName,jdbcType=CHAR} and rownum<=1"
    })
    @ResultMap("machine")
    WcsMachineEntity selectByStationName(String stationName);

    @Update({
            "update WCS_Machine",
            "set Block_Name = #{blockName,jdbcType=CHAR},",
            "Plc_Name = #{plcName,jdbcType=CHAR},",
            "Type = #{type,jdbcType=TINYINT},",
            "Station_Name = #{stationName,jdbcType=CHAR},",
            "Dock_Name = #{dockName,jdbcType=CHAR},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR},",
            "Reserved2 = #{reserved2,jdbcType=VARCHAR}",
            "where trim(Name)  = #{name,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(WcsMachineEntity record);
}