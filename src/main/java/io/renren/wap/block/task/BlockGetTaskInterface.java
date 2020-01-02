package io.renren.wap.block.task;


import io.renren.modules.generator.entity.WcsTaskingEntity;

/**
 * 获取待分配任务
 *
 * @Author: CalmLake
 * @date 2019/7/25  16:34
 * @Version: V1.0.0
 **/
public interface BlockGetTaskInterface {
    /**
     * 获取待分配任务
     *
     * @return com.wap.entity.Tasking
     * @author CalmLake
     * @date 2019/7/25 16:35
     */
    WcsTaskingEntity getTask();
}
