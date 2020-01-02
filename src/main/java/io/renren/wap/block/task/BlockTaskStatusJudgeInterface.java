package io.renren.wap.block.task;

/**
 * block任务分配前置状态判断
 *
 * @Author: CalmLake
 * @Date: 2019/7/25  16:03
 * @Version: V1.0.0
 **/
public interface BlockTaskStatusJudgeInterface {
    /**
     * 判断设备是否可以分配任务
     *
     * @return boolean
     * @author CalmLake
     * @date 2019/7/25 16:04
     */
    boolean isCanTask();
}
