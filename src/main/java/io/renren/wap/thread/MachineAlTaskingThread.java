package io.renren.wap.thread;


import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.block.task.BlockAlGetTaskImpl;
import io.renren.wap.block.task.BlockGetTaskInterface;
import io.renren.wap.block.task.BlockTaskImpl;
import io.renren.wap.block.task.BlockTaskInterface;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.lock.LockInterface;
import io.renren.wap.util.Log4j2Util;
import org.apache.logging.log4j.LogManager;

/**
 * 提升机任务分配
 *
 * @Author: CalmLake
 * @date 2019/8/28  15:31
 * @Version: V1.0.0
 **/
public class MachineAlTaskingThread implements Runnable {
    private LockInterface lock;
    private WcsMachineEntity machine;

    public MachineAlTaskingThread(WcsMachineEntity machine, LockInterface lock) {
        this.lock = lock;
        this.machine = machine;
    }

    @Override
    public void run() {
        String blockName = machine.getBlockName();
        LogManager.getLogger().info(String.format("%s,任务分配启动！", blockName));
        while (true) {
            WcsTaskingEntity tasking = null;
            try {
                WcsMachineEntity machine = MachineCache.getMachine(blockName);
                BlockGetTaskInterface blockGetTaskInterface = new BlockAlGetTaskImpl(machine);
                tasking = blockGetTaskInterface.getTask();
                if (tasking != null) {
                    BlockTaskInterface blockTaskInterface = new BlockTaskImpl(tasking);
                    boolean resultTask = blockTaskInterface.task();
                    if (resultTask) {
                        Log4j2Util.getAssigningTaskLogger().info(String.format("数据block：%s ，任务分配成功！任务：%s", machine.getBlockName(), tasking.toString()));
                    } else {
                        Log4j2Util.getAssigningTaskLogger().info(String.format("数据block：%s ，任务分配失败！任务：%s", machine.getBlockName(), tasking.toString()));
                    }
                }
                lock.await();
            } catch (Exception e) {
                e.printStackTrace();
                assert tasking != null;
                Log4j2Util.getAssigningTaskLogger().error(String.format("数据block：%s ，任务分配失败！任务：%s。异常：%s", machine.getBlockName(), tasking.toString(), e.getMessage()));
            }
        }
    }


}
