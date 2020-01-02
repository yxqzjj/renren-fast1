package io.renren.wap.service;


import io.renren.modules.generator.dao.impl.WcsChargeDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMcblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.entity.*;
import io.renren.wap.entity.constant.ChargeConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.TaskingConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.thread.PutInStorageAssigningTaskThread;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

/**
 * 工作分配
 *
 * @Author: CalmLake
 * @Date: 2019/1/9  10:36
 * @Version: V1.0.0
 **/
public class AssigningTaskService {

    /**
     * 新的工作计划
     */
    private WcsWorkplanEntity workPlan;

    public AssigningTaskService(WcsWorkplanEntity workPlan) {
        this.workPlan = workPlan;
    }

    /**
     * 根据工作计划类型进行任务分配
     *
     * @author CalmLake
     * @date 2019/1/9 10:41
     */
    public void assigningTasks() {
        try {
            if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlan.getType()) {
                putInStorageAssigningTasks();
            } else {
                outPutStorageAssigningTasks();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 入库任务分配
     *
     * @author CalmLake
     * @date 2019/1/9 10:42
     */
    private void putInStorageAssigningTasks() {
        String station = workPlan.getFromStation();
        PutInStorageAssigningTaskThread putInStorageAssigningTaskThread = new PutInStorageAssigningTaskThread(workPlan.getId());
        ThreadPoolServiceSingleton.getInstance().getPutInStorageAssigningTaskExecutorService(station).submit(putInStorageAssigningTaskThread);
    }

    /**
     * 出库类任务分配 （由堆垛机或母车等作为起始设备的任务）
     *
     * @author CalmLake
     * @date 2019/1/9 13:59
     */
    private void outPutStorageAssigningTasks() {
        String station = workPlan.getFromStation();
        String toStation = workPlan.getToStation();
        int workPlanType = workPlan.getType();
        //  station 和 blockName 是相同的
        WcsMachineEntity machine = MachineCache.getMachine(station);
        int machineType = machine.getType();
        short mlMcNu = TaskingConstant.ML_MC_ONE;
        String blockName = machine.getBlockName();
        String nextBlockName;
        if (MachineConstant.BYTE_TYPE_MC.equals(machineType)) {
            WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
            if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
                nextBlockName = toStation;
                mlMcNu = TaskingConstant.ML_MC_TWO;
            } else {
                nextBlockName = mcBlock.getBingScBlockName();
            }
        } else if (MachineConstant.BYTE_TYPE_ML.equals(machineType)) {
            WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
            nextBlockName = StandbyCarService.getScBlockName(mlBlock);
        } else if (MachineConstant.BYTE_TYPE_SC.equals(machineType)) {
            if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType || WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
                WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
                int chargeType = charge.getType();
                if (ChargeConstant.TYPE_MACHINE.equals(chargeType) || ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                    blockName = workPlan.getToStation();
                    nextBlockName = station;
                    mlMcNu = TaskingConstant.ML_MC_ONE;
                } else {
                    mlMcNu = TaskingConstant.ML_MC_TWO;
                    if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
                        blockName = charge.getReserved1();
                        nextBlockName = station;
                    } else {
                        blockName = charge.getChargeBlockName();
                        nextBlockName = station;
                    }
                }
            } else if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType || WorkPlanConstant.TYPE_GET_CAR==workPlanType) {
                blockName = workPlan.getToStation();
                nextBlockName = station;
            } else {
                Log4j2Util.getAssigningTaskLogger().info(String.format("mcKey：%s,工作计划类型不正确！workPlanType：%d", workPlan.getMckey(), workPlanType));
                return;
            }
        } else if (MachineConstant.BYTE_TYPE_AL.equals(machineType)) {
            nextBlockName = toStation;
        } else {
            Log4j2Util.getAssigningTaskLogger().info(String.format("%s,未解析的设备类型！", blockName));
            return;
        }
        //  充电任务，当且仅当多台堆垛机联动时获取穿梭车名称
        if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
            if (station.contains(MachineConstant.TYPE_ML) && toStation.contains(MachineConstant.TYPE_ML)) {
                nextBlockName = workPlan.getReserved2();
            } else if (station.contains(MachineConstant.TYPE_MC) && toStation.contains(MachineConstant.TYPE_MC)) {
                nextBlockName = workPlan.getReserved2();
            }
        }
        TaskingService taskingService = new TaskingService();
        taskingService.insertNewTasking(workPlan.getMckey(), nextBlockName, blockName, workPlan.getPriorityConfigPriority(), workPlanType, mlMcNu, machine.getWarehouseNo(), "", toStation, TaskingConstant.MACHINE_TYPE_M_SC);
        Log4j2Util.getAssigningTaskLogger().info("工作计划下发成功！blockName：" + blockName + "，托盘号：" + workPlan.getBarcode() + "，mcKey：" + workPlan.getMckey());
        LockCache.getValue(blockName).signal();
    }
}
