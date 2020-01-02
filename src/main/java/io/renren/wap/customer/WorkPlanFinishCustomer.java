package io.renren.wap.customer;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsAutocreateworkplanEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.wap.cache.BlockQueueCache;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.constant.AutoCreateWorkPlanConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;
import org.apache.logging.log4j.LogManager;

/**
 * 工作计划完成消费者
 *
 * @Author: CalmLake
 * @Date: 2019/4/3  12:01
 * @Version: V1.0.0
 **/
public class WorkPlanFinishCustomer implements Runnable {
    @Override
    public void run() {
        LogManager.getLogger().info("已完成任务删除消费者启动！");
        while (true) {
            try {
                String string = BlockQueueCache.getWorkPlanFishString();
                SleepUtil.sleep(1);
                JSONObject jsonObject = JSONObject.parseObject(string);
                String mcKey = jsonObject.getString("mcKey");
                int workPlanId = jsonObject.getIntValue("workPlanId");
                WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectById(workPlanId);
                DbUtil.getProcedureOrcaleDao().spDeleteFinishWorkPlanOperationIn(workPlanId, mcKey);
                if (CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU.equals(SystemCache.SYS_NAME_COMPANY)){
                    String showTimeString = "showTime";
                    if (WorkPlanConstant.TYPE_MOVEMENT==workPlan.getType() && showTimeString.equals(workPlan.getReserved1())) {
                        WcsAutocreateworkplanEntity autoCreateWorkPlan = DbUtil.getAutoCreateWorkPlanDao().selectById(1);
                        int countNum = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>().
                                eq("Reserved1",showTimeString).eq("Type",3).in("Status",1,2));
                        if (countNum < 1) {
                            if (autoCreateWorkPlan.getType() == 1) {
                                autoCreateWorkPlan.setType(AutoCreateWorkPlanConstant.TYPE_2);
                            } else {
                                autoCreateWorkPlan.setType(AutoCreateWorkPlanConstant.TYPE_1);
                            }
                            if (autoCreateWorkPlan.getSwitchMode()) {
                                WorkPlanService.createAutoWorkPlan(autoCreateWorkPlan);
                            } else {
                                Log4j2Util.getWorkPlanLogger().info("演示关闭！");
                                autoCreateWorkPlan.setStatus(AutoCreateWorkPlanConstant.STATUS_1);
                                if (autoCreateWorkPlan.getStationA().equals(autoCreateWorkPlan.getLocationB())) {
                                    LockCache.getValue(autoCreateWorkPlan.getStationA()).signal();
                                } else {
                                    LockCache.getValue(autoCreateWorkPlan.getStationA()).signal();
                                    LockCache.getValue(autoCreateWorkPlan.getStationB()).signal();
                                }
                            }
                            DbUtil.getAutoCreateWorkPlanDao().updateById(autoCreateWorkPlan);
                        }
                    }
                }
                Log4j2Util.getWorkPlanLogger().info(String.format("任务删除成功！id: %d,mcKey: %s", workPlanId, mcKey));
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log4j2Util.getWorkPlanLogger().info("任务删除队列中断异常");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
