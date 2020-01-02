package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.wap.dao.provider.WorkPlanLogSqlProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 工作计划记录
 *
 * @author CalmLake
 * @date 2019/3/15 15:40
 */
@Repository("WorkPlanLogDao")
@Mapper
public interface WorkPlanLogDao {
    @Delete({
            "delete from WCS_WorkPlanLog",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into WCS_WorkPlanLog (ID, Work_Plan_Id, ",
            "Mckey, Type, Barcode, ",
            "Create_Time, Start_Time, ",
            "Finish_Time, Status, ",
            "From_Station, To_Station, ",
            "From_Location, To_Location, ",
            "WMS_Flag, Priority_Config_Priority, ",
            "Reserved1, Reserved2)",
            "values (#{id,jdbcType=INTEGER}, #{workPlanId,jdbcType=INTEGER}, ",
            "#{mckey,jdbcType=CHAR}, #{type,jdbcType=TINYINT}, #{barcode,jdbcType=CHAR}, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{startTime,jdbcType=TIMESTAMP}, ",
            "#{finishTime,jdbcType=TIMESTAMP}, #{status,jdbcType=TINYINT}, ",
            "#{fromStation,jdbcType=CHAR}, #{toStation,jdbcType=CHAR}, ",
            "#{fromLocation,jdbcType=CHAR}, #{toLocation,jdbcType=CHAR}, ",
            "#{wmsFlag,jdbcType=CHAR}, #{priorityConfigPriority,jdbcType=TINYINT}, ",
            "#{reserved1,jdbcType=VARCHAR}, #{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsWorkplanlogEntity record);

    @InsertProvider(type = WorkPlanLogSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int InsertProvider(WcsWorkplanlogEntity record);

    @Select({
            "select",
            "ID, Work_Plan_Id, Mckey, Type, Barcode, Create_Time, Start_Time, Finish_Time, ",
            "Status, From_Station, To_Station, From_Location, To_Location, WMS_Flag, Priority_Config_Priority, ",
            "Reserved1, Reserved2",
            "from WCS_WorkPlanLog",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    @Results(id = "workPlanLog", value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "Work_Plan_Id", property = "workPlanId", jdbcType = JdbcType.INTEGER),
            @Result(column = "Mckey", property = "mckey", jdbcType = JdbcType.CHAR),
            @Result(column = "Type", property = "type", jdbcType = JdbcType.TINYINT),
            @Result(column = "Barcode", property = "barcode", jdbcType = JdbcType.CHAR),
            @Result(column = "Create_Time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Start_Time", property = "startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Finish_Time", property = "finishTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "From_Station", property = "fromStation", jdbcType = JdbcType.CHAR),
            @Result(column = "To_Station", property = "toStation", jdbcType = JdbcType.CHAR),
            @Result(column = "From_Location", property = "fromLocation", jdbcType = JdbcType.CHAR),
            @Result(column = "To_Location", property = "toLocation", jdbcType = JdbcType.CHAR),
            @Result(column = "WMS_Flag", property = "wmsFlag", jdbcType = JdbcType.CHAR),
            @Result(column = "Priority_Config_Priority", property = "priorityConfigPriority", jdbcType = JdbcType.TINYINT),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR)
    })
    WcsWorkplanlogEntity selectByPrimaryKey(Integer id);

    /**
     * 查找最新完成的任务记录
     *
     * @param mckey 任务标识
     * @return com.wap.entity.WorkPlanLog
     * @author CalmLake
     * @date 2019/4/23 11:37
     */
    @Select({
            "select",
            "   * from WCS_WorkPlanLog",
            "where trim(Mckey) = #{Mckey,jdbcType=CHAR} and  rownum <= 1 order by ID desc"
    })
    @ResultMap("workPlanLog")
    WcsWorkplanlogEntity getWorkPlanLogByMcKey(@Param("mckey") String mckey);

    /**
     * 根据wms唯一标识查找工作计划信息
     * @author CalmLake
     * @date 2019/5/18 15:52
     * @param wmsFlag wms唯一标识
     * @return com.wap.entity.WorkPlanLog
     */
    @Select({
            "select",
            "* from WCS_WorkPlanLog ",
            "where trim(WMS_Flag) = #{wmsFlag,jdbcType=CHAR}"
    })
    @ResultMap("workPlanLog")
    WcsWorkplanlogEntity selectWorkPlanLogByWmsFlag(@Param("wmsFlag") String wmsFlag);

    /**
     * 根据时间查找当天的任务完成数
     *
     * @param date 时间
     * @return int
     * @author CalmLake
     * @date 2019/2/25 14:58
     */
    @Select({
            "select",
            " count(*) from WCS_WorkPlanLog",
            "where Status = 3 and CONVERT(varchar(100), Create_Time, 23) =  CONVERT(varchar(100), #{date,jdbcType=TIMESTAMP}, 23)"
    })
    int countFinishWorkPlanNum(@Param("date") Date date);

    @Update({
            "update WCS_WorkPlanLog",
            "set Work_Plan_Id = #{workPlanId,jdbcType=INTEGER},",
            "Mckey = #{mckey,jdbcType=CHAR},",
            "Type = #{type,jdbcType=TINYINT},",
            "Barcode = #{barcode,jdbcType=CHAR},",
            "Create_Time = #{createTime,jdbcType=TIMESTAMP},",
            "Start_Time = #{startTime,jdbcType=TIMESTAMP},",
            "Finish_Time = #{finishTime,jdbcType=TIMESTAMP},",
            "Status = #{status,jdbcType=TINYINT},",
            "From_Station = #{fromStation,jdbcType=CHAR},",
            "To_Station = #{toStation,jdbcType=CHAR},",
            "From_Location = #{fromLocation,jdbcType=CHAR},",
            "To_Location = #{toLocation,jdbcType=CHAR},",
            "WMS_Flag = #{wmsFlag,jdbcType=CHAR},",
            "Priority_Config_Priority = #{priorityConfigPriority,jdbcType=TINYINT},",
            "Reserved1 = #{reserved1,jdbcType=VARCHAR},",
            "Reserved2 = #{reserved2,jdbcType=VARCHAR}",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WcsWorkplanlogEntity record);
}