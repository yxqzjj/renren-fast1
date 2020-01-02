package io.renren.wap.block.task;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsCrossrouteEntity;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.block.BlockTask;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CrossRouteConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.factory.BlockDaoFactory;
import io.renren.wap.factory.BlockDaoImpl;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

/**
 * 交叉输送线任务分配
 *
 * @Author: CalmLake
 * @date 2019/8/7  14:12
 * @Version: V1.0.0
 **/
public class BlockTaskClImpl extends BlockTaskImpl {

    public BlockTaskClImpl(WcsTaskingEntity tasking) {
        super(tasking);
    }

    /**
     * 任务分配
     *
     * @return boolean
     * @author CalmLake
     * @date 2019/7/25 16:44
     */
    @Override
    public boolean task() {
        boolean result = true;
        try {
            Integer id = tasking.getId();
            String blockName = tasking.getBlockName();
            String nextBlockName = tasking.getNextBlockName();
            String mcKey = tasking.getMckey();
            int workPlanType = tasking.getWorkPlanType();
            WcsMachineEntity machine = MachineCache.getMachine(blockName);
            WcsMachineEntity nextMachine = MachineCache.getMachine(nextBlockName);
            BlockTask blockTask = new BlockTask(machine);
            BlockTask NextBlockTask = new BlockTask(nextMachine);
            blockTask.getBlockStatus();
            NextBlockTask.getBlockStatus();
            Block block = blockTask.getBlock();
            Block nextBlock = NextBlockTask.getBlock();
            blockTask.judgeMachineStatus();
            String keyType = blockTask.getType();
            NextBlockTask.judgeMachineStatus();
            String nextKeyType = NextBlockTask.getType();
            BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
            if (BlockConstant.KEY_EMPTY_STRING.equals(keyType) && BlockConstant.KEY_EMPTY_STRING.equals(nextKeyType)) {
                //  两个设备空闲
                blockDaoFactory.getBlockDao(machine).updateMcKeyByName(mcKey, nextBlockName, blockName);
                blockDaoFactory.getBlockDao(nextMachine).updateMcKeyByName(mcKey, blockName, nextBlockName);
            } else {
                if (MachineConstant.BYTE_TYPE_CL.equals(machine.getType()) && MachineConstant.BYTE_TYPE_CL.equals(nextMachine.getType())) {
                    if (!BlockConstant.KEY_NOT_EMPTY_STRING.equals(nextKeyType)) {
                        updateBlockTable(mcKey, nextBlockName, blockName, nextBlock, nextMachine, blockDaoFactory);
                        WcsCrossrouteEntity crossRoute = DbUtil.getCrossRouteDao().selectOne(new QueryWrapper<WcsCrossrouteEntity>().eq("Run_Block_Name",tasking.getRunBlockName()));
                        if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
                            crossRoute.setMode(CrossRouteConstant.MODE_OUT_PUT);
                        } else if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
                            crossRoute.setMode(CrossRouteConstant.MODE_PUT_IN);
                        } else {
                            result = false;
                            Log4j2Util.getAssigningTaskLogger().info(String.format("未解析的工作计划类型：%d ", workPlanType));
                        }
                        crossRoute.setLoadNum(crossRoute.getLoadNum() + 1);
                        DbUtil.getCrossRouteDao().selectOne(new QueryWrapper<WcsCrossrouteEntity>().setEntity(crossRoute));
                    } else {
                        result = false;
                        Log4j2Util.getAssigningTaskLogger().info(String.format("设备任务标识key类型：%s ", nextKeyType));
                    }
                } else {
                    result = false;
                    Log4j2Util.getAssigningTaskLogger().info(String.format("设备类型错误：block: %d,nextBlock:%d ", machine.getType(), nextMachine.getType()));
                }
                if (result) {
                    DbUtil.getTaskingDao().deleteById(id);
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                    BlockServiceImplFactory.blockServiceDoKey(nextBlockName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
