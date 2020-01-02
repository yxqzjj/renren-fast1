package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.dao.provider.WorkPlanProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 工作计划
 *
 * @author CalmLake
 * @date 2019/1/8 10:29
 */
@Repository("WorkPlanDao")
@Mapper
public interface WorkPlanDao {
    @Delete({
            "delete from WCS_WorkPlan",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into WCS_WorkPlan (ID, McKey, ",
            "Type, Barcode, Create_Time, ",
            "Start_Time, Finish_Time, ",
            "Status, From_Station, ",
            "To_Station, From_Location, ",
            "To_Location, WMS_Flag, Priority_Config_Priority, ",
            "Reserved1, Reserved2)",
            "values (#{id,jdbcType=INTEGER}, #{mcKey,jdbcType=CHAR}, ",
            "#{type,jdbcType=TINYINT}, #{barcode,jdbcType=CHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{startTime,jdbcType=TIMESTAMP}, #{finishTime,jdbcType=TIMESTAMP}, ",
            "#{status,jdbcType=TINYINT}, #{fromStation,jdbcType=CHAR}, ",
            "#{toStation,jdbcType=CHAR}, #{fromLocation,jdbcType=CHAR}, ",
            "#{toLocation,jdbcType=CHAR}, #{wmsFlag,jdbcType=CHAR}, #{priorityConfigPriority,jdbcType=TINYINT}, ",
            "#{reserved1,jdbcType=VARCHAR}, #{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsWorkplanEntity record);

    @InsertProvider(type = WorkPlanProvider.class, method = "insertProvider")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertProvider(WcsWorkplanEntity workPlan);

    /**
     * 根据mcKey查找
     *
     * @param mcKey wcs任务唯一标识
     * @return com.wap.entity.WorkPlan
     * @author CalmLake
     * @date 2019/1/9 10:13
     */
    @Select({
            "select",
            " * ",
            "from WCS_WorkPlan",
            "where trim(McKey) = #{mcKey,jdbcType=CHAR}"
    })
    @Results(id = "workPlan", value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "McKey", property = "mcKey", jdbcType = JdbcType.CHAR),
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
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Warehouse_No", property = "warehouseNo", jdbcType = JdbcType.SMALLINT)
    })
    WcsWorkplanEntity selectByMcKey(@Param("mcKey") String mcKey);

    /**
     * 工作计划为演示移库任务正在执行或等待的条数
     *
     * @param reserved1 特殊标识
     * @return int
     * @author CalmLake
     * @date 2019/4/12 15:11
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where Status in (1,2)  ",
            " and  Type in (3)",
            "  and  trim(Reserved1) = #{reserved1,jdbcType=CHAR} "
    })
    int countWorkPlanNumByReserved1(@Param("reserved1") String reserved1);

    /**
     * 工作计划类型在6,7,8,9,10,11内，状态在1,2内，源站台或目标站台为输入值的数据条目
     *
     * @param toStation   目标站台
     * @param fromStation 源站台
     * @return int
     * @author CalmLake
     * @date 2019/4/12 15:11
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where Status in (1,2)  ",
            " and  Type in (6,7,8,9,10,11)",
            "  and  ((trim(From_Station)= #{toStation,jdbcType=CHAR} or trim(To_Station)= #{toStation,jdbcType=CHAR}) or (trim(From_Station)= #{fromStation,jdbcType=CHAR} or trim(To_Station)= #{fromStation,jdbcType=CHAR}))"
    })
    int countWorkPlanNumByStation(@Param("toStation") String toStation, @Param("fromStation") String fromStation);

    /**
     * 未完成任务数
     *
     * @param date 今日时间
     * @return int
     * @author CalmLake
     * @date 2019/2/25 15:05
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where Status = 1 or Status = 2 and CONVERT(varchar(100), Create_Time, 23) =  CONVERT(varchar(100), #{date,jdbcType=TIMESTAMP}, 23)"
    })
    int countWorkingNum(@Param("date") Date date);

    /**
     * 此源站台某种工作计划类型的数量
     *
     * @param fromStation 源站台
     * @param type        工作计划类型
     * @return int
     * @author CalmLake
     * @date 2019/4/1 17:23
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where  trim(From_Station) = #{fromStation,jdbcType=CHAR} and Type = #{type,jdbcType=TINYINT}"
    })
    int countNumByTypeAndFromStation(@Param("fromStation") String fromStation, @Param("type") Integer type);

    /**
     * 此源站台某种工作计划类型的数量
     *
     * @param fromStation 源站台
     * @param type        工作计划类型
     * @param type2       工作计划类型
     * @return int
     * @author CalmLake
     * @date 2019/4/1 17:23
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where  trim(From_Station) = #{fromStation,jdbcType=CHAR} and (Type = #{type,jdbcType=TINYINT} or  Type = #{type2,jdbcType=TINYINT})"
    })
    int countNumByTypeTwoAndFromStation(@Param("fromStation") String fromStation, @Param("type") Integer type, @Param("type2") Integer type2);

    /**
     * 根据站台查找此信息的工作计划数量
     *
     * @param toStation    目标站台
     * @param fromStation1 源站台1
     * @param fromStation2 源站台2
     * @param status       订单状态
     * @return int
     * @author CalmLake
     * @date 2019/1/28 15:52
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where  trim(To_Station) = #{toStation,jdbcType=CHAR} and  Status =#{status,jdbcType=TINYINT} and (trim(From_Station)= #{fromStation1,jdbcType=CHAR} or trim(From_Station)= #{fromStation2,jdbcType=CHAR})"
    })
    int selectByToStation(@Param("toStation") String toStation, @Param("fromStation1") String fromStation1, @Param("fromStation2") String fromStation2, @Param("status") Integer status);

    /**
     * 根据站台查找此信息的工作计划数量
     *
     * @param fromStation 源站台
     * @param toStation1  目标站台1
     * @param toStation2  目标站台2
     * @param status      订单状态
     * @return int
     * @author CalmLake
     * @date 2019/1/28 15:52
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where  trim(From_Station)= #{fromStation,jdbcType=CHAR} and  Status =#{status,jdbcType=TINYINT} and (trim(To_Station)= #{toStation1,jdbcType=CHAR} or trim(To_Station)= #{toStation2,jdbcType=CHAR})"
    })
    int selectByFromStation(@Param("fromStation") String fromStation, @Param("toStation1") String toStation1, @Param("toStation2") String toStation2, @Param("status") Integer status);

    /**
     * 统计该站台待执行任务和正在执行任务数的和
     *
     * @param station 站台名称
     * @return int
     * @author CalmLake
     * @date 2019/3/14 17:55
     */
    @Select({
            "select count(*)  from WCS_WorkPlan where  (trim(From_Station) = #{station,jdbcType=CHAR} or trim(To_Station) = #{station,jdbcType=CHAR} ) and  (Status = 1 or Status = 2 )"
    })
    int countNumChargeWorkPlan(@Param("station") String station);

    /**
     * 统计该站台待执行任务和正在执行任务数的和
     *
     * @param station 站台名称
     * @return int
     * @author CalmLake
     * @date 2019/3/14 17:55
     */
    @Select({
            "select count(*)  from WCS_WorkPlan where  (trim(From_Station) = #{station,jdbcType=CHAR} or trim(To_Station) = #{station,jdbcType=CHAR} ) and  (Status = 1 or Status = 2 )"
    })
    int countSameStationWorkPlanNum(@Param("station") String station);


    @Select({
            "select count(*)  from WCS_WorkPlan where  (trim(From_Station) = #{station,jdbcType=CHAR} or trim(To_Station) = #{station,jdbcType=CHAR} ) and  (Status = 1 or Status = 2 ) and  Type != 8  "
    })
    int countSameStationWorkPlanNumtypeNotInChangeTier(@Param("station") String station);

    @Select({
            "select count(*)  from WCS_WorkPlan where  (trim(From_Station) = #{station,jdbcType=CHAR} or trim(To_Station) = #{station,jdbcType=CHAR} ) and  (Status = 1 or Status = 2 ) and  Type = 8  "
    })
    int countSameStationWorkPlanNumtypeInChangeTier(@Param("station") String station);
    /**
     * 计数-返回原点工作计划数
     *
     * @param fromStation 源站台
     * @param toStation   目标站台
     * @param status      订单状态
     * @param status2     订单状态
     * @return int
     * @author CalmLake
     * @date 2019/3/12 10:47
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where  trim(From_Station) = #{fromStation,jdbcType=CHAR}   and  trim(To_Station) = #{toStation,jdbcType=CHAR} and  (Status =#{status,jdbcType=TINYINT} or Status =#{status2,jdbcType=TINYINT})"
    })
    int countSameStationAndStatus(@Param("fromStation") String fromStation, @Param("toStation") String toStation, @Param("status") int status, @Param("status2") int status2);

    /**
     * 根据站台查找此信息的工作计划数量
     *
     * @param fromStation 源站台
     * @param toStation   目标站台
     * @param status      订单状态
     * @return int
     * @author CalmLake
     * @date 2019/1/28 15:04
     */
    @Select({
            "select",
            " count(*) ",
            "from WCS_WorkPlan",
            "where trim(To_Station)= #{toStation,jdbcType=CHAR} and  trim(From_Station)= #{fromStation,jdbcType=CHAR} and  Status =#{status,jdbcType=TINYINT}"
    })
    int selectByStation(@Param("fromStation") String fromStation, @Param("toStation") String toStation, @Param("status") Integer status);

    /**
     * 根据wms任务id查找 相同wms id的任务数
     *
     * @param wmsFlag wms任务唯一标识
     * @return int 计数
     * @author CalmLake
     * @date 2019/1/9 10:13
     */
    @Select({
            "select",
            " count(*) from WCS_WorkPlan ",
            "where trim(WMS_Flag) = #{wmsFlag,jdbcType=CHAR}"
    })
    int selectByWmsFlag(@Param("wmsFlag") String wmsFlag);

    /**
     * 根据wms唯一标识查找任务
     *
     * @param wmsFlag wms任务唯一标识
     * @return com.wap.entity.WorkPlan
     * @author CalmLake
     * @date 2019/5/18 15:49
     */
    @Select({
            "select",
            "* from WCS_WorkPlan ",
            "where trim(WMS_Flag) = #{wmsFlag,jdbcType=CHAR}"
    })
    @ResultMap("workPlan")
    WcsWorkplanEntity selectWorkPlanByWmsFlag(@Param("wmsFlag") String wmsFlag);

    /**
     * 获取所有工作计划
     *
     * @return java.util.List<com.wap.entity.WorkPlan>
     * @author CalmLake
     * @date 2019/2/21 10:22
     */
    @Select({
            "select * from WCS_WorkPlan order by id desc"
    })
    @ResultMap("workPlan")
    List<WcsWorkplanEntity> getList();

    @Select({
            "select",
            "ID, McKey, Type, Barcode, Create_Time, Start_Time, Finish_Time, Status, From_Station, ",
            "To_Station, From_Location, To_Location, WMS_Flag, Priority_Config_Priority, ",
            "Reserved1, Reserved2",
            "from WCS_WorkPlan",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("workPlan")
    WcsWorkplanEntity selectByPrimaryKey(Integer id);

    @Update({
            "update WCS_WorkPlan",
            "set McKey = #{mcKey,jdbcType=CHAR},",
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
    int updateByPrimaryKey(WcsWorkplanEntity record);

    /**
     * 修改工作计划状态和完成时间
     *
     * @param id         id
     * @param status     状态
     * @param finishTime 完成时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 16:54
     */
    @Update({
            "update WCS_WorkPlan",
            "set ",
            "Finish_Time = #{finishTime,jdbcType=TIMESTAMP},",
            "Status = #{status,jdbcType=TINYINT}",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateWorkPlanStatusByPrimaryKey(@Param("id") int id, @Param("status") Integer status, @Param("finishTime") Date finishTime);

    /**
     * 修改工作计划状态和开始时间
     *
     * @param id        id
     * @param status    状态
     * @param startTime 开始时间
     * @return int
     * @author CalmLake
     * @date 2019/1/21 16:54
     */
    @Update({
            "update WCS_WorkPlan",
            "set ",
            "Start_Time = #{startTime,jdbcType=TIMESTAMP},",
            "Status = #{status,jdbcType=TINYINT}",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateStatusStartTimeByPrimaryKey(@Param("id") int id, @Param("status") int status, @Param("startTime") Date startTime);
}