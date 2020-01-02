package io.renren.wap.thread;

import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.block.BlockTask;
import io.renren.wap.lock.LockInterface;
import io.renren.wap.util.Log4j2Util;
import org.apache.logging.log4j.LogManager;

/**
 * 设备任务分配
 *
 * @Author: CalmLake
 * @date 2019/7/25  9:48
 * @Version: V1.0.0
 **/
public class MachineTaskingThread implements Runnable {
    private LockInterface lock;
    private WcsMachineEntity machine;

    public MachineTaskingThread(WcsMachineEntity machine, LockInterface lock) {
        this.lock = lock;
        this.machine = machine;
    }

    @Override
    public void run() {
        BlockTask blockTask = new BlockTask(machine);
        LogManager.getLogger().info(String.format("%s,任务分配启动！", machine.getBlockName()));
        while (true) {
            try {
                //  此程序顺序不可更改（内部逻辑关系：递进）
                blockTask.getBlockStatus();
                blockTask.judgeMachineStatus();
                blockTask.isCanTask();
                blockTask.getTask();
                blockTask.task();
                if (blockTask.isResultTask()) {
                    lock.await();
                } else {
                    blockTask.goBackDefaultLocation();
                    if (!blockTask.isResultCreateGoBackFlag()) {
                        lock.await();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log4j2Util.getAssigningTaskLogger().error(String.format("数据block：%s ，任务分配失败！任务：%s。异常：%s", machine.getBlockName(), blockTask.getTasking().toString(), e.getMessage()));
            }
        }
    }
}
