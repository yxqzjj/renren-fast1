package io.renren.wap.block;


import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.block.status.AbstractBlockStatus;
import io.renren.wap.block.status.BlockStatusInterface;
import io.renren.wap.block.task.BlockGetTaskInterface;
import io.renren.wap.block.task.BlockGoBackInterface;
import io.renren.wap.block.task.BlockTaskInterface;
import io.renren.wap.block.task.BlockTaskStatusJudgeInterface;

/**
 * block任务分配的爸爸
 *
 * @Author: CalmLake
 * @date 2019/7/25  16:19
 * @Version: V1.0.0
 **/
public abstract class AbstractBlockTask {
    protected WcsMachineEntity machine;
    AbstractBlockStatus abstractBlockStatus;
    BlockStatusInterface blockStatusInterface;
    BlockTaskStatusJudgeInterface blockTaskStatusJudgeInterface;
    BlockGetTaskInterface blockGetTaskInterface;
    BlockTaskInterface blockTaskInterface;
    BlockGoBackInterface blockGoBackInterface;

    /**
     * 获取设备状态
     *
     * @author CalmLake
     * @date 2019/7/26 15:34
     */
    public abstract void getBlockStatus();

    /**
     * 设备状态判断
     *
     * @author CalmLake
     * @date 2019/7/25 17:08
     */
    public abstract void judgeMachineStatus();

    /**
     * 判断设备是否可以分配任务
     *
     * @author CalmLake
     * @date 2019/7/25 17:08
     */
    public abstract void isCanTask();

    /**
     * 获取待分配任务
     *
     * @author CalmLake
     * @date 2019/7/25 17:08
     */
    public abstract void getTask();

    /**
     * 任务分配
     *
     * @author CalmLake
     * @date 2019/7/25 17:08
     */
    public abstract void task();

    /**
     * 回原点任务处理
     *
     * @author CalmLake
     * @date 2019/7/30 9:14
     */
    public abstract void goBackDefaultLocation();
}
