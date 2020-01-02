package io.renren.wap.service;


import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

import java.util.Date;

/**
 * 任务分配
 *
 * @Author: CalmLake
 * @Date: 2019/1/22  10:07
 * @Version: V1.0.0
 **/
public class TaskingService {
    /**
     * 新增任务记录
     *
     * @param mcKey                  任务标识
     * @param withWorkBlockName      交互工作数据block名称
     * @param blockName              数据block名称
     * @param priorityConfigPriority 优先级
     * @param workPlanType           工作计划类型
     * @param mlMcNum                该任务使用堆垛机母车数量
     * @param wareHouseNo            该任务属于哪个库区
     * @param toStation              任务目标站台
     * @param machineType            工作设备类型
     * @author CalmLake
     * @date 2019/8/8 10:13
     */
    public void insertNewTasking(String mcKey, String withWorkBlockName, String blockName, int priorityConfigPriority, int workPlanType, int mlMcNum, int wareHouseNo, String runBlockName, String toStation, int machineType) {
        WcsTaskingEntity tasking = new WcsTaskingEntity();
        tasking.setPriorityConfigPriority(priorityConfigPriority);
        tasking.setMckey(mcKey);
        tasking.setCreateTime(new Date());
        tasking.setNextBlockName(withWorkBlockName);
        tasking.setBlockName(blockName);
        tasking.setWorkPlanType(workPlanType);
        tasking.setMlMcNum(mlMcNum);
        tasking.setWarehouseNo(wareHouseNo);
        tasking.setRunBlockName(runBlockName);
        tasking.setMachineType(machineType);
        tasking.setToStation(toStation);
        DbUtil.getTaskingDao().insertProvider(tasking);
        Log4j2Util.getAssigningTaskLogger().info(String.format("插入新任务：%s", tasking.toString()));
    }
}
