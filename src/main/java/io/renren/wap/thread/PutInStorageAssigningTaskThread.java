package io.renren.wap.thread;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.TaskingConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.factory.BlockDaoFactory;
import io.renren.wap.factory.BlockDaoImpl;
import io.renren.wap.service.RouteService;
import io.renren.wap.service.TaskingService;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 入库任务分配
 *
 * @Author: CalmLake
 * @Date: 2019/1/9  11:19
 * @Version: V1.0.0
 **/
public class PutInStorageAssigningTaskThread implements Runnable {
    private Integer workPlanId;

    public PutInStorageAssigningTaskThread(Integer workPlanId) {
        this.workPlanId = workPlanId;
    }

    @Override
    public void run() {
        boolean result = false;
        do {
            try {
                WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectById(workPlanId);
                String station = workPlan.getFromStation();
                String toStation = workPlan.getToStation();
                String fromStation = workPlan.getFromStation();
                String mcKey = workPlan.getMckey();
                WcsMachineEntity machine = DbUtil.getMachineDao().selectOne(new QueryWrapper<WcsMachineEntity>().eq("Station_Name",station));
                String blockName = machine.getBlockName();
                String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                int workPlanSize = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                        .eq("To_Station",toStation)
                        .eq("Status",WorkPlanConstant.STATUS_WORKING)
                        .eq("From_Station",fromStation));
                if (workPlanSize < 1) {
                    WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                    BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
                    BlockDao blockDao = blockDaoFactory.getBlockDao(nextBlockName);
                    Block nextBlock = blockDao.selectByPrimaryKey(nextBlockName);
                    if (BlockService.isNotError(clBlock.getErrorCode()) && BlockService.isFinishWork(clBlock.getCommand())) {
                        if (StringUtils.isEmpty(clBlock.getMckey()) && StringUtils.isEmpty(clBlock.getAppointmentMckey())) {
                            if (CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU.equals(SystemCache.SYS_NAME_COMPANY)) {
                                Integer workPlanType = workPlan.getType();
                                if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
                                    String blockNameCl = DbUtil.getMachineDao().selectOne(new QueryWrapper<WcsMachineEntity>().eq("stationName",station)).getBlockName();
                                    int resultCl = WcsClblockDaoImpl.getInstance().updateCLBlockLoad(true,blockName);
                                    Log4j2Util.getBlockBrickLogger().info(String.format("入库任务，站台数据block：%s 载荷修改结果：%b", blockNameCl, resultCl));
                                }
                            }
                            if (StringUtils.isEmpty(nextBlock.getMckey()) && StringUtils.isEmpty(nextBlock.getAppointmentMckey())) {
                                if (BlockService.isFinishWork(nextBlock.getCommand())) {
                                    if (nextBlock instanceof WcsClblockEntity) {

                                        WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, nextBlockName, blockName);
                                        WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, blockName, nextBlockName);
                                        WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                                        wcsWorkplanEntity.setStartTime(new Date());
                                        wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                                        DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);                                        BlockServiceImplFactory.blockServiceDoKey(blockName);
                                        BlockServiceImplFactory.blockServiceDoKey(nextBlockName);
                                        result = true;
                                    } else if (nextBlock instanceof WcsAlblockEntity) {
                                        WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, nextBlockName, blockName);                                        WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                                        wcsWorkplanEntity.setStartTime(new Date());
                                        wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                                        DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);                                        BlockServiceImplFactory.blockServiceDoKey(blockName);
                                        //  放入任务分配表
                                        TaskingService taskingService = new TaskingService();
                                        taskingService.insertNewTasking(mcKey, nextBlockName, blockName, workPlan.getPriorityConfigPriority(), workPlan.getType(),
                                                TaskingConstant.ML_MC_ONE, machine.getWarehouseNo(), "", toStation, TaskingConstant.MACHINE_TYPE_CL_AL);
                                        LockCache.getValue(nextBlockName).signal();
                                        result = true;
                                    } else if (nextBlock instanceof WcsMlblockEntity) {
                                        WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, nextBlockName, blockName);
                                        WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                                        wcsWorkplanEntity.setStartTime(new Date());
                                        wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                                        DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);                                        BlockServiceImplFactory.blockServiceDoKey(blockName);
                                        //  放入任务分配表
                                        TaskingService taskingService = new TaskingService();
                                        taskingService.insertNewTasking(mcKey, nextBlockName, blockName, workPlan.getPriorityConfigPriority(), workPlan.getType(),
                                                TaskingConstant.ML_MC_ONE, machine.getWarehouseNo(), "", toStation, TaskingConstant.MACHINE_TYPE_CL_M);
                                        LockCache.getValue(nextBlockName).signal();
                                        result = true;
                                    } else {
                                        Log4j2Util.getWorkPlanLogger().info(String.format("%s 设备类型未解析", nextBlockName));
                                    }
                                } else {
                                    Log4j2Util.getWorkPlanLogger().info(String.format("%s 设备工作未完成，指令：%s", nextBlockName, nextBlock.getCommand()));
                                }
                            } else if (StringUtils.isNotEmpty(nextBlock.getMckey()) && StringUtils.isEmpty(nextBlock.getAppointmentMckey())) {
                                if (nextBlock instanceof WcsClblockEntity) {
                                    WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, nextBlockName, blockName);
                                    WcsClblockDaoImpl.getInstance().updateAppointmentMcKeyCLBlock(mcKey, blockName, nextBlockName);
                                    WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                                    wcsWorkplanEntity.setStartTime(new Date());
                                    wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                                    DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);                                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                                    BlockServiceImplFactory.blockServiceDoKey(nextBlockName);
                                    result = true;
                                } else if (nextBlock instanceof WcsAlblockEntity) {
                                    WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, nextBlockName, blockName);
                                    WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                                    wcsWorkplanEntity.setStartTime(new Date());
                                    wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                                    DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);                                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                                    //  放入任务分配表
                                    TaskingService taskingService = new TaskingService();
                                    taskingService.insertNewTasking(mcKey, nextBlockName, blockName, workPlan.getPriorityConfigPriority(), workPlan.getType(),
                                            TaskingConstant.ML_MC_ONE, machine.getWarehouseNo(), "", toStation, TaskingConstant.MACHINE_TYPE_CL_AL);
                                    LockCache.getValue(nextBlockName).signal();
                                    result = true;
                                }else if (nextBlock instanceof WcsMlblockEntity) {
                                    WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, nextBlockName, blockName);
                                    WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                                    wcsWorkplanEntity.setStartTime(new Date());
                                    wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                                    DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);                                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                                    //  放入任务分配表
                                    TaskingService taskingService = new TaskingService();
                                    taskingService.insertNewTasking(mcKey, nextBlockName, blockName, workPlan.getPriorityConfigPriority(), workPlan.getType(),
                                            TaskingConstant.ML_MC_ONE, machine.getWarehouseNo(), "", toStation, TaskingConstant.MACHINE_TYPE_CL_M);
                                    LockCache.getValue(nextBlockName).signal();
                                    result = true;
                                } else {
                                    Log4j2Util.getWorkPlanLogger().info(String.format("%s 设备类型未解析", nextBlockName));
                                }
                            }
                        }
                    }
                }
                SleepUtil.sleep(0.3);
            } catch (Exception e) {
                e.printStackTrace();
                Log4j2Util.getWorkPlanLogger().error("入库工作计划分配出现异常：" + e.getMessage());
                result = true;
            }
        } while (!result);
    }
}
