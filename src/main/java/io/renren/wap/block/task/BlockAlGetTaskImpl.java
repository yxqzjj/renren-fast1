package io.renren.wap.block.task;



import io.renren.modules.generator.dao.impl.WcsTaskingDaoImpl;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.util.DbUtil;

import java.util.List;


/**
 * 提升机任务获取
 *
 * @Author: CalmLake
 * @date 2019/8/28  15:25
 * @Version: V1.0.0
 **/
public class BlockAlGetTaskImpl extends BlockGetTaskImpl {
    public BlockAlGetTaskImpl(WcsMachineEntity machine) {
        super(machine);
    }

    /**
     * 获取待分配任务
     *
     * @return com.wap.entity.WcsTaskingEntity
     * @author CalmLake
     * @date 2019/7/25 16:35
     */
    @Override
    public WcsTaskingEntity getTask() {
        String blockName = machine.getBlockName();
        WcsTaskingEntity tasking = null;
        List<WcsTaskingEntity> taskingCharges = WcsTaskingDaoImpl.getTaskingDao().getChargeWcsTaskingEntityListByBlockName(blockName);
        List<WcsTaskingEntity> taskingChanges = WcsTaskingDaoImpl.getTaskingDao().getChangeGetOffCarWcsTaskingEntityListByBlockName(blockName);
        List<WcsTaskingEntity> taskingMovements =WcsTaskingDaoImpl.getTaskingDao().getMovementTallyTakeStockWcsTaskingEntityListByBlockName(blockName);
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
                    tasking = taskingPutIns.get(0);
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
                        tasking = taskingPutIns.get(0);
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

}
