package io.renren.wap.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

/**
 * 存储过程操作
 *
 * @Author: CalmLake
 * @Date: 2019/5/26  11:09
 * @Version: V1.0.0
 **/
@Repository("ProcedureDao")
@Mapper
public interface ProcedureDao {

    /**
     * 统计堆垛机/母车或穿梭车未分配任务数
     *
     * @param mBlocKName  堆垛机或母车名称
     * @param scBlocKName 穿梭车名称
     * @return int
     * @author CalmLake
     * @date 2019/6/11 16:37
     */
    @Update({"exec sp_select_tasking_ml_mc_sc_out #{mBlocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR}"})
    int spSelectTaskingMlMcScOut(@Param("mBlocKName") String mBlocKName, @Param("scBlocKName") String scBlocKName);

    /**
     * 堆垛机/母车移栽取货完成后修改自身状态和分配穿梭车任务
     *
     * @param blocKName   堆垛机/母车数据block名称
     * @param scBlocKName 穿梭车数据block名称
     * @param mcKey       任务唯一标识
     * @param machineType 设备类型
     * @return int
     * @author CalmLake
     * @date 2019/5/26 11:17
     */
    @Update({"exec sp_update_block_scblock_transplantingPickUp_finished_tasking_in #{blocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT}"})
    int spUpdateBlockScBlockTransplantingPickUpFinishedTaskingIn(@Param("blocKName") String blocKName, @Param("scBlocKName") String scBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Byte machineType);

    /**
     * 堆垛机/母车移栽取货完成后修改自身状态和分配穿梭车预约任务
     *
     * @param blocKName   堆垛机/母车数据block名称
     * @param scBlocKName 穿梭车数据block名称
     * @param mcKey       任务唯一标识
     * @param machineType 设备类型
     * @return int
     * @author CalmLake
     * @date 2019/5/26 11:17
     */
    @Update({"exec sp_update_block_scblock_transplantingPickUp_finished_tasking_appointmentmckey_in #{blocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT}"})
    int spUpdateBlockScBlockTransplantingPickUpFinishedTaskingAppointmentmckeyIn(@Param("blocKName") String blocKName, @Param("scBlocKName") String scBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Byte machineType);

    /**
     * 清除堆垛机/母车/提升机和穿梭车当前执行的任务标识和交互设备名称
     *
     * @param blocKName   堆垛机/母车/提升机数据block名称
     * @param scBlocKName 穿梭车数据block名称
     * @param machineType 设备类型
     * @return int
     * @author CalmLake
     * @date 2019/5/26 14:16
     */
    @Update({"exec sp_update_block_scblock_clear_mckey_in #{blocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{machineType,jdbcType=TINYINT}"})
    int spUpdateBlockScblockClearMckeyIn(@Param("blocKName") String blocKName, @Param("scBlocKName") String scBlocKName, @Param("machineType") Byte machineType);


    /**
     * 分配堆垛机/母车/提升机/RGV和输送线预约任务及预约交互设备名称
     *
     * @param blocKName   堆垛机/母车数据block名称
     * @param clBlocKName 输送线数据block名称
     * @param mcKey       任务标识
     * @param machineType 设备类型
     * @return int
     * @author CalmLake
     * @date 2019/5/26 14:33
     */
    @Update({"exec sp_update_block_clblock_appointmckey_in #{blocKName,jdbcType=CHAR},#{clBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT}"})
    int spUpdateBlockClblockAppointmckeyIn(@Param("blocKName") String blocKName, @Param("clBlocKName") String clBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Byte machineType);

    /**
     * 分配堆垛机/母车/提升机/RGV和输送线任务及交互设备名称
     *
     * @param blocKName   堆垛机/母车/提升机/RGV数据block名称
     * @param clBlocKName 输送线数据block名称
     * @param mcKey       任务标识
     * @param machineType 设备类型
     * @return int
     * @author CalmLake
     * @date 2019/5/26 14:33
     */
    @Update({"exec sp_update_block_clblock_mckey_in #{blocKName,jdbcType=CHAR},#{clBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT}"})
    int spUpdateBlockClblockMckeyIn(@Param("blocKName") String blocKName, @Param("clBlocKName") String clBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Byte machineType);

    /**
     * 堆垛机移栽取货完成后修改自身状态和分配穿梭车任务
     *
     * @param mlBlocKName 堆垛机数据block名称
     * @param scBlocKName 穿梭车数据block名称
     * @param mcKey       任务唯一标识
     * @return int
     * @author CalmLake
     * @date 2019/5/26 11:17
     */
    @Update({"exec sp_update_mlblock_scblock_transplantingPickUp_finished_tasking_in #{mlBlocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR}"})
    int spUpdateMlBlockScBlockTransplantingPickUpFinishedTaskingIn(@Param("mlBlocKName") String mlBlocKName, @Param("scBlocKName") String scBlocKName, @Param("mcKey") String mcKey);

    /**
     * 堆垛机移栽取货完成后修改自身状态和分配穿梭车预约任务
     *
     * @param mlBlocKName 堆垛机数据block名称
     * @param scBlocKName 穿梭车数据block名称
     * @param mcKey       任务唯一标识
     * @return int
     * @author CalmLake
     * @date 2019/5/26 11:17
     */
    @Update({"exec sp_update_mlblock_scblock_transplantingPickUp_finished_tasking_appointmentmckey_in #{mlBlocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR}"})
    int spUpdateMlBlockScBlockTransplantingPickUpFinishedTaskingAppointmentmckeyIn(@Param("mlBlocKName") String mlBlocKName, @Param("scBlocKName") String scBlocKName, @Param("mcKey") String mcKey);

    /**
     * 清除堆垛机和穿梭车当前执行的任务标识和交互设备名称
     *
     * @param mlBlocKName 堆垛机数据block名称
     * @param scBlocKName 穿梭车数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/5/26 14:16
     */
    @Update({"exec sp_update_mlblock_scblock_clear_mckey_in #{mlBlocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR}"})
    int spUpdateMlblockScblockClearMckeyIn(@Param("mlBlocKName") String mlBlocKName, @Param("scBlocKName") String scBlocKName);


    /**
     * 分配堆垛机和穿梭车预约任务及预约交互设备名称
     *
     * @param mlBlocKName 堆垛机数据block名称
     * @param clBlocKName 输送线数据block名称
     * @param mcKey       任务标识
     * @return int
     * @author CalmLake
     * @date 2019/5/26 14:33
     */
    @Update({"exec sp_update_mlblock_clblock_appointmckey_in #{mlBlocKName,jdbcType=CHAR},#{clBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR}"})
    int spUpdateMlblockClblockAppointmckeyIn(@Param("mlBlocKName") String mlBlocKName, @Param("clBlocKName") String clBlocKName, @Param("mcKey") String mcKey);

    /**
     * 分配堆垛机和穿梭车任务及交互设备名称
     *
     * @param mlBlocKName 堆垛机数据block名称
     * @param clBlocKName 输送线数据block名称
     * @param mcKey       任务标识
     * @return int
     * @author CalmLake
     * @date 2019/5/26 14:33
     */
    @Update({"exec sp_update_mlblock_clblock_mckey_in #{mlBlocKName,jdbcType=CHAR},#{clBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR}"})
    int spUpdateMlblockClblockMckeyIn(@Param("mlBlocKName") String mlBlocKName, @Param("clBlocKName") String clBlocKName, @Param("mcKey") String mcKey);


    /**
     * 工作计划完成DB操作，删除该条计划且删除该工作计划指令
     *
     * @param workPlanId 工作计划序号
     * @param mcKey      任务唯一标识
     * @return int
     * @author CalmLake
     * @date 2019/5/27 10:11
     */
    @Delete({"exec sp_delete_finish_workPlan_operation_in #{workPlanId,jdbcType=INTEGER},#{mcKey,jdbcType=CHAR}"})
    int spDeleteFinishWorkPlanOperationIn(@Param("workPlanId") int workPlanId, @Param("mcKey") String mcKey);

    /**
     * 查询是否可以创建回原点任务 0是/1否
     *
     * @param blockName              设备数据block名称
     * @param chargeMachineBlockName 关联充电设备名称
     * @param bingScBlockName        堆垛机/母车绑定子车名称
     * @param defaultLocation        默认原点名称
     * @return int
     * @author CalmLake
     * @date 2019/5/27 14:07
     */
    @Select({"exec sp_select_can_create_go_back_default_location_in_out #{blockName,jdbcType=CHAR},#{chargeMachineBlockName,jdbcType=CHAR},#{bingScBlockName,jdbcType=CHAR},#{defaultLocation,jdbcType=CHAR}"})
    int spSelectCanCreateGoBackDefaultLocationInOut(@Param("blockName") String blockName, @Param("chargeMachineBlockName") String chargeMachineBlockName, @Param("bingScBlockName") String bingScBlockName, @Param("defaultLocation") String defaultLocation);

}
