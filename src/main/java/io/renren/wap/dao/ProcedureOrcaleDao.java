package io.renren.wap.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * orcale 存储过程操作
 *
 * @Author: CalmLake
 * @Date: 2019/5/26  11:09
 * @Version: V1.0.0
 **/
@Repository("ProcedureOrcaleDao")
@Mapper
public interface ProcedureOrcaleDao {

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
    @Update({"CALL SP_UPDATE_BLOCK_SC_T_PICKUP(#{blocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT})"})
    int spUpdateBlockScBlockTransplantingPickUpFinishedTaskingIn(@Param("blocKName") String blocKName, @Param("scBlocKName") String scBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Integer machineType);

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
    @Update({"CALL SP_UPDATE_T_PICKUP_A_KEY(#{blocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT})"})
    int spUpdateBlockScBlockTransplantingPickUpFinishedTaskingAppointmentmckeyIn(@Param("blocKName") String blocKName, @Param("scBlocKName") String scBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Integer machineType);

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
    @Update({"CALL SP_UPDATE_BLOCK_SC_CLEAR_KEY(#{blocKName,jdbcType=CHAR},#{scBlocKName,jdbcType=CHAR},#{machineType,jdbcType=TINYINT})"})
    int spUpdateBlockScblockClearMckeyIn(@Param("blocKName") String blocKName, @Param("scBlocKName") String scBlocKName, @Param("machineType") Integer machineType);


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
    @Update({"CALL SP_UPDATE_BL_CLOCK_A_KEY(#{blocKName,jdbcType=CHAR},#{clBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT})"})
    int spUpdateBlockClblockAppointmckeyIn(@Param("blocKName") String blocKName, @Param("clBlocKName") String clBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Integer machineType);

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
    @Update({"CALL SP_UPDATE_BLOCK_KEY(#{blocKName,jdbcType=CHAR},#{clBlocKName,jdbcType=CHAR},#{mcKey,jdbcType=CHAR},#{machineType,jdbcType=TINYINT})"})
    int spUpdateBlockClblockMckeyIn(@Param("blocKName") String blocKName, @Param("clBlocKName") String clBlocKName, @Param("mcKey") String mcKey, @Param("machineType") Integer machineType);

    /**
     * 工作计划完成DB操作，删除该条计划且删除该工作计划指令
     *
     * @param workPlanId 工作计划序号
     * @param mcKey      任务唯一标识
     * @return int
     * @author CalmLake
     * @date 2019/5/27 10:11
     */
    @Delete({"CALL SP_DELETE_WORKPLAN_IN(#{workPlanId,jdbcType=INTEGER},#{mcKey,jdbcType=CHAR})"})
    int spDeleteFinishWorkPlanOperationIn(@Param("workPlanId") int workPlanId, @Param("mcKey") String mcKey);

    /**
     * 删除历史数据
     *
     * @return int
     * @author CalmLake
     * @date 2019/11/3 10:16
     */
    @Delete({"CALL SP_DELETE_COMMAND_WCS_WMS_LOG "})
    int spDeleteCommandWcsWmsLog();

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
    @Select({"CALL SP_GO_BACK_DEFAULT_IN_OUT(#{blockName,jdbcType=CHAR},#{chargeMachineBlockName,jdbcType=CHAR},#{bingScBlockName,jdbcType=CHAR},#{defaultLocation,jdbcType=CHAR})"})
    int spSelectCanCreateGoBackDefaultLocationInOut(@Param("blockName") String blockName, @Param("chargeMachineBlockName") String chargeMachineBlockName, @Param("bingScBlockName") String bingScBlockName, @Param("defaultLocation") String defaultLocation);

}
