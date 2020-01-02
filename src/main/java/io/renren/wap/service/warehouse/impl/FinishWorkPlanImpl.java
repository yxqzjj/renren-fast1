package io.renren.wap.service.warehouse.impl;


import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.server.util.SendMsgToWmsUtil;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.service.warehouse.FinishWorkPlanInterface;

/**
 * 出库完成操作
 *
 * @Author: CalmLake
 * @Date: 2019/4/3  16:39
 * @Version: V1.0.0
 **/
public class FinishWorkPlanImpl implements FinishWorkPlanInterface {
    @Override
    public void finishOutStorage(WcsWorkplanEntity workPlan) throws InterruptedException {
        WorkPlanService.finishWorkPlan(workPlan.getId(), workPlan.getMckey());
        SendMsgToWmsUtil.sendMovementReport(workPlan);
    }
}
