package io.renren.wap.service.warehouse;


import io.renren.modules.generator.entity.WcsWorkplanEntity;

/**
 * 工作计划完成操作
 *
 * @Author: CalmLake
 * @Date: 2019/4/3  16:32
 * @Version: V1.0.0
 **/
public interface FinishWorkPlanInterface {
    /**
     * 出库完成操作
     *
     * @param workPlan 工作计划
     * @author CalmLake
     * @date 2019/4/3 16:37
     * @throws InterruptedException 异常
     */
    void finishOutStorage(WcsWorkplanEntity workPlan) throws InterruptedException;
}
