package io.renren.wap.dao;


import io.renren.modules.generator.entity.WcsTaskingEntity;

import io.renren.wap.dao.provider.TaskingProvider;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 任务分配
 *
 * @author CalmLake
 * @date 2019/1/9 14:21
 */
@Repository("WcsTaskingEntityDao")
@Mapper
public interface TaskingDao {
    /**
     * 删除
     *
     * @param id id
     * @return int
     * @author CalmLake
     * @date 2019/1/28 18:03
     */
    @Delete({
            "delete from WCS_WcsTaskingEntity",
            "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(@Param("id") int id);

    /**
     * 删除该条记录
     *
     * @param mcKey 任务标识
     * @return int
     * @author CalmLake
     * @date 2019/3/19 9:59
     */
    @Delete({
            "delete from WCS_WcsTaskingEntity",
            "where trim(McKey) = #{mcKey,jdbcType=CHAR}"
    })
    int deleteByMcKey(@Param("mcKey") String mcKey);

    @Insert({
            "insert into WCS_WcsTaskingEntity (ID,McKey, Block_Name, ",
            "Next_Block_Name, Create_Time, ",
            "Priority_Config_Priority,Work_Plan_Type, Reserved1, ",
            "Reserved2)",
            "values (#{id,jdbcType=INTEGER},#{mcKey,jdbcType=CHAR}, #{blockName,jdbcType=CHAR}, ",
            "#{nextBlockName,jdbcType=CHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{priorityConfigPriority,jdbcType=TINYINT},#{workPlanType,jdbcType=TINYINT}, #{reserved1,jdbcType=VARCHAR}, ",
            "#{reserved2,jdbcType=VARCHAR})"
    })
    int insert(WcsTaskingEntity record);

    /**
     * 插入
     *
     * @param record 任务信息
     * @return int
     * @author CalmLake
     * @date 2019/1/9 14:45
     */
    @InsertProvider(type = TaskingProvider.class, method = "insertProvider")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    int insertProvider(WcsTaskingEntity record);

    /**
     * 获取该设备一条最早的待分配任务
     *
     * @param blockName 数据block名称
     * @return com.wap.entity.WcsTaskingEntity
     * @author CalmLake
     * @date 2019/3/27 13:51
     */
    @Select({
            "select    * from WCS_WcsTaskingEntity where trim(Block_Name) = #{blockName,jdbcType=CHAR} and  rownum <= 1 order by id asc"
    })
    @Results(id = "tasking", value = {
            @Result(column = "ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "McKey", property = "mcKey", jdbcType = JdbcType.CHAR),
            @Result(column = "Block_Name", property = "blockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Next_Block_Name", property = "nextBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "Create_Time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "Priority_Config_Priority", property = "priorityConfigPriority", jdbcType = JdbcType.TINYINT),
            @Result(column = "Work_Plan_Type", property = "workPlanType", jdbcType = JdbcType.TINYINT),
            @Result(column = "Reserved1", property = "reserved1", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Reserved2", property = "reserved2", jdbcType = JdbcType.VARCHAR),
            @Result(column = "Warehouse_No", property = "warehouseNo", jdbcType = JdbcType.SMALLINT),
            @Result(column = "Run_Block_Name", property = "runBlockName", jdbcType = JdbcType.CHAR),
            @Result(column = "To_Station", property = "toStation", jdbcType = JdbcType.CHAR),
            @Result(column = "Ml_Mc_Num", property = "mlMcNum", jdbcType = JdbcType.SMALLINT),
            @Result(column = "Machine_Type", property = "machineType", jdbcType = JdbcType.TINYINT)
    })
    WcsTaskingEntity selectByBlockName(@Param("blockName") String blockName);

    @Select({
            "select count(*) from WCS_WcsTaskingEntity where trim(Next_Block_Name) = #{nextBlockName,jdbcType=CHAR}"
    })
    int countByNextBlockName(@Param("nextBlockName") String nextBlockName);

    @Select({
            "select count(*) from WCS_WcsTaskingEntity where trim(Block_Name) = #{blockName,jdbcType=CHAR} or trim(Next_Block_Name) = #{blockName,jdbcType=CHAR}"
    })
    int countByBlockName(@Param("blockName") String blockName);

    /**
     * 根据优先级降序和创建时间升序查找待分配任务
     *
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/1/28 13:11
     */
    @Select({
            "select * from WCS_WcsTaskingEntity  order by Priority_Config_Priority desc,Create_Time asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getListByPriorityDescCreateTimeAsc();

    /**
     * 查询当前待分配任务中与搬运货物无直接关系的系统任务（充电，换层，接卸穿梭车）
     *
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/4/15 14:46
     */
    @Select({
            "select * from WCS_WcsTaskingEntity  where Work_Plan_Type in (6,7,8,9,10)"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getListChargeWorkPlan();

    /**
     * 查找待分配的超时任务
     *
     * @param nowDate 当前时间
     * @param minute  超时时间
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/1/28 13:05
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where DATEDIFF(MINUTE,Create_Time,#{nowDate,jdbcType=TIMESTAMP}) >  #{minute,jdbcType=INTEGER} order by Priority_Config_Priority desc,Create_Time asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getListByOvertime(@Param("nowDate") Date nowDate, @Param("minute") int minute);

    /**
     * 查找待分配任务 使用存储过程  该sql适用于单仓库双堆垛机 充电时一台子车会使用两台堆垛机
     *
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/4/22 13:29
     */
    @Select({
            "exec sp_select_tasking_out"
    })
    List<WcsTaskingEntity> getOneWarehouseListByProc();

    /**
     * 查找待分配任务 使用存储过程  该sql适用于多仓库双堆垛机且独立运行
     *
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/4/22 13:29
     */
    @Select({
            "exec sp_select_tasking_warehouses_out"
    })
    List<WcsTaskingEntity> getWarehousesListByProc();

    /**
     * 获取block名称相关的任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 9:43
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where Block_Name = #{blockName,jdbcType=CHAR} or Next_Block_Name = #{blockName,jdbcType=CHAR}  order by Priority_Config_Priority desc,ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getWcsTaskingEntityListByBlockName(@Param("blockName") String blockName);

    /**
     * 获取充电未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:22
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where (trim(Block_Name) = #{blockName,jdbcType=CHAR} or trim(Next_Block_Name) = #{blockName,jdbcType=CHAR}) and  ( Work_Plan_Type = 6 OR Work_Plan_Type = 7     )  order by ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getChargeWcsTaskingEntityListByBlockName(@Param("blockName") String blockName);

    /**
     * 获取接车卸车换层未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:22
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where  (trim(Block_Name) = #{blockName,jdbcType=CHAR} or trim(Next_Block_Name) = #{blockName,jdbcType=CHAR})  and   ( Work_Plan_Type = 8 OR Work_Plan_Type = 9 OR Work_Plan_Type = 10 OR Work_Plan_Type = 11 )    order by ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getChangeGetOffCarWcsTaskingEntityListByBlockName(@Param("blockName") String blockName);

    /**
     * 获取移库理货盘点未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:22
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where  (trim(Block_Name) = #{blockName,jdbcType=CHAR} or trim(Next_Block_Name) = #{blockName,jdbcType=CHAR})   and  ( Work_Plan_Type = 3 OR Work_Plan_Type = 4 OR Work_Plan_Type = 5 )  order by ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getMovementTallyTakeStockWcsTaskingEntityListByBlockName(@Param("blockName") String blockName);

    /**
     * 获取出库未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:20
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where  (trim(Block_Name) = #{blockName,jdbcType=CHAR} or trim(Next_Block_Name) = #{blockName,jdbcType=CHAR})   and  Work_Plan_Type = 2  order by ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getOutPutStorageWcsTaskingEntityListByBlockName(@Param("blockName") String blockName);

    /**
     * 获取入库未分配任务
     *
     * @param blockName 数据block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/7/26 11:19
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where  (trim(Block_Name) = #{blockName,jdbcType=CHAR} or trim(Next_Block_Name) = #{blockName,jdbcType=CHAR})   and  Work_Plan_Type = 1  order by ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getPutInStorageWcsTaskingEntityListByBlockName(@Param("blockName") String blockName);

    /**
     * 根据运行block查找关联任务——入库
     *
     * @param runBlockName 运行block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/8/4 18:32
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where trim(Run_Block_Name) = #{runBlockName,jdbcType=CHAR}   and  Work_Plan_Type = 1  order by ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getClWcsTaskingEntityPutInListByRunBlockName(@Param("runBlockName") String runBlockName);

    /**
     * 根据运行block查找关联任务——出库
     *
     * @param runBlockName 运行block名称
     * @return java.util.List<com.wap.entity.WcsTaskingEntity>
     * @author CalmLake
     * @date 2019/8/4 18:32
     */
    @Select({
            "select * from WCS_WcsTaskingEntity where trim(Run_Block_Name) = #{runBlockName,jdbcType=CHAR}   and  Work_Plan_Type = 2  order by ID asc"
    })
    @ResultMap("tasking")
    List<WcsTaskingEntity> getClWcsTaskingEntityOutPutListByRunBlockName(@Param("runBlockName") String runBlockName);
}