package io.renren.wap.block.task;



import io.renren.modules.generator.dao.impl.WcsTaskingDaoImpl;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.block.BlockTask;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.service.RouteService;
import io.renren.wap.util.DbUtil;

import java.util.List;

/**
 * 获取待分配任务
 *
 * @Author: CalmLake
 * @date 2019/7/25  16:36
 * @Version: V1.0.0
 **/
public class BlockGetTaskImpl implements BlockGetTaskInterface {
    public WcsMachineEntity machine;

    public BlockGetTaskImpl(WcsMachineEntity machine) {
        this.machine = machine;
    }

    /**
     * 获取待分配任务
     *
     * @return com.wap.entity.Tasking
     * @author CalmLake
     * @date 2019/7/25 16:35
     */
    @Override
    public WcsTaskingEntity getTask() {
        String blockName = machine.getBlockName();
        WcsTaskingEntity tasking = null;
        List<WcsTaskingEntity> taskingCharges = WcsTaskingDaoImpl.getTaskingDao().getChargeWcsTaskingEntityListByBlockName(blockName);
        List<WcsTaskingEntity> taskingChanges = WcsTaskingDaoImpl.getTaskingDao().getChangeGetOffCarWcsTaskingEntityListByBlockName(blockName);
        List<WcsTaskingEntity> taskingMovements = WcsTaskingDaoImpl.getTaskingDao().getMovementTallyTakeStockWcsTaskingEntityListByBlockName(blockName);
        List<WcsTaskingEntity> taskingOutPuts = WcsTaskingDaoImpl.getTaskingDao().getOutPutStorageWcsTaskingEntityListByBlockName(blockName);
        List<WcsTaskingEntity> taskingPutIns = WcsTaskingDaoImpl.getTaskingDao().getPutInStorageWcsTaskingEntityListByBlockName(blockName);
        int taskingChargesNum = taskingCharges.size();
        int taskingChangesNum = taskingChanges.size();
        int taskingMovementsNum = taskingMovements.size();
        int taskingOutPutsNum = taskingOutPuts.size();
        int taskingPutInsNum = taskingPutIns.size();
        if (taskingChargesNum > 0) {
            tasking = taskingCharges.get(0);
        } else {
            if (taskingChangesNum > 0) {
                tasking = taskingChanges.get(0);
            } else {
                //  三种任务类型都有
                if (taskingMovementsNum > 0 && taskingOutPutsNum > 0 && taskingPutInsNum > 0) {
                    //  最早创建
                    if (taskingMovements.get(0).getId() < taskingOutPuts.get(0).getId() && taskingMovements.get(0).getId() < taskingPutIns.get(0).getId()) {
                        tasking = taskingMovements.get(0);
                        return tasking;
                    }
                    if (taskingOutPuts.get(0).getId() < taskingMovements.get(0).getId() && taskingOutPuts.get(0).getId() < taskingPutIns.get(0).getId()) {
                        if (isBlockKeyEmpty(blockName, taskingOutPuts.get(0))) {
                            tasking = taskingOutPuts.get(0);
                        } else {
                            if (taskingPutIns.get(0).getId() < taskingMovements.get(0).getId()) {
                                tasking = taskingPutIns.get(0);
                            }
                        }
                    }
                    if (taskingPutIns.get(0).getId() < taskingOutPuts.get(0).getId() && taskingPutIns.get(0).getId() < taskingMovements.get(0).getId()) {
                        tasking = taskingPutIns.get(0);
                        return tasking;
                    }
                } else {
                    //  只有两种任务类型
                    if (taskingMovementsNum > 0 && taskingOutPutsNum > 0) {
                        if (taskingMovements.get(0).getId() < taskingOutPuts.get(0).getId()) {
                            tasking = taskingMovements.get(0);
                        } else {
                            tasking = taskingOutPuts.get(0);
                        }
                    } else if (taskingMovementsNum > 0 && taskingPutInsNum > 0) {
                        if (taskingMovements.get(0).getId() < taskingPutIns.get(0).getId()) {
                            tasking = taskingMovements.get(0);
                        } else {
                            tasking = taskingPutIns.get(0);
                        }
                    } else if (taskingOutPutsNum > 0 && taskingPutInsNum > 0) {
                        //  优先出库
                        if (isBlockKeyEmpty(blockName, taskingOutPuts.get(0))) {
                            tasking = taskingOutPuts.get(0);
                        } else {
                            tasking = taskingPutIns.get(0);
                        }
                    } else {
                        //  只有一种任务类型
                        if (taskingMovementsNum > 0) {
                            tasking = taskingMovements.get(0);
                        } else if (taskingOutPutsNum > 0) {
                            tasking = taskingOutPuts.get(0);
                        } else if (taskingPutInsNum > 0) {
                            tasking = taskingPutIns.get(0);
                        }
                    }
                }
            }
        }
        return tasking;
    }

    /**
     * 判断下一设备是否有任务标识
     *
     * @param blockName 数据block名称
     * @param tasking   任务数据
     * @return boolean
     * @author CalmLake
     * @date 2019/8/8 10:26
     */
    boolean isBlockKeyEmpty(String blockName, WcsTaskingEntity tasking) {
        boolean result = false;
        String nextBlockString = RouteService.getRouteNextBlockName(blockName, tasking.getToStation());
        WcsMachineEntity nextMachine = MachineCache.getMachine(nextBlockString);
        BlockTask nextBlockTask = new BlockTask(nextMachine);
        if (BlockConstant.KEY_EMPTY_STRING.equals(nextBlockTask.getType())) {
            result = true;
        }
        return result;
    }
}
