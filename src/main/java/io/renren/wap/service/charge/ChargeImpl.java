package io.renren.wap.service.charge;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.*;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.*;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.ChargeConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.AssigningTaskService;
import io.renren.wap.service.StandbyCarService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.util.ChargeLocationUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 充电逻辑处理
 *
 * @Author: CalmLake
 * @Date: 2019/3/19  17:03
 * @Version: V1.0.0
 **/
public class ChargeImpl implements ChargeInterface {
    @Override
    public void startCharge(String blockName) {
        WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (BlockConstant.STATUS_RUNNING.equals(scBlock.getStatus())) {
            //  没有充电开始任务
            WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_CREATE_CHARGE_START);
            //  充电开始 创建充电任务
            WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
            String chargeBlockName = charge.getChargeBlockName();
            Integer chargeType = charge.getType();
            if (ChargeConstant.TYPE_MACHINE.equals(chargeType)) {
                //  （穿梭车在堆垛机上充电，穿梭车与堆垛机配对。例：SC01-ML01）
                createWorkPlan(chargeBlockName, blockName, "", WorkPlanConstant.TYPE_CHARGE_UP);
            } else if (ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                String location = charge.getLocation();
                if (StringUtils.isNotEmpty(location)) {
                    createWorkPlan(chargeBlockName, blockName, location, WorkPlanConstant.TYPE_CHARGE_UP);
                } else {
                    Log4j2Util.getChargeLogger().info(String.format("%s location:%s 暂无充电位可用", blockName, location));
                    WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_RUNNING);
                }
            } else if (ChargeConstant.TYPE_OTHER_ONR_MACHINE.equals(chargeType)) {
                String location = ChargeLocationUtil.getInstance().assigningLocation(blockName);
                String transferStation = charge.getReserved1();
                if (StringUtils.isNotEmpty(location)) {
                    createOffCarWorkPlan(chargeBlockName);
                    createWorkPlan(chargeBlockName, transferStation, location, WorkPlanConstant.TYPE_CHARGE_UP);
                } else {
                    Log4j2Util.getChargeLogger().info(String.format("%s location:%s 暂无充电位可用", blockName, location));
                    WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_RUNNING);
                }
            } else {
                Log4j2Util.getChargeLogger().info(String.format("%s %d 该充电类型未解析", blockName, chargeType));
                WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_RUNNING);
            }
        }
    }

    @Override
    public void finishCharge(String blockName) {
        WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
        String chargeBlockName = charge.getChargeBlockName();
        if (BlockConstant.STATUS_CHARGE.equals(scBlock.getStatus())) {
            WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_CREATE_CHARGE_FINISH);
            Integer chargeType = charge.getType();
            if (ChargeConstant.TYPE_MACHINE.equals(chargeType)) {
                //  不用创建充电完成任务 直接更改穿梭车状态为运行
                WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_RUNNING);
            } else if (ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                String location = charge.getLocation();
                if (StringUtils.isNotEmpty(location)) {
                    WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan("", "", "000000000", chargeBlockName, "", WorkPlanConstant.TYPE_CHARGE_COMPLETE, blockName, location, blockName);
                    WcsScblockDaoImpl.getInstance().updateIsUseByName(blockName, true);
                    AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                    assigningTaskService.assigningTasks();
                } else {
                    Log4j2Util.getChargeLogger().info(String.format("%s : location:%s,该设备充电位置获取失败", blockName, location));
                    WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_RUNNING);
                }
            } else if (ChargeConstant.TYPE_OTHER_ONR_MACHINE.equals(chargeType)) {
                String transferStation = charge.getReserved1();
                String location = ChargeLocationUtil.getInstance().getLocation(blockName);
                if (StringUtils.isNotEmpty(location)) {
                    createOffCarWorkPlan(chargeBlockName);
                    WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan("", "", "000000000", transferStation, "", WorkPlanConstant.TYPE_CHARGE_COMPLETE, chargeBlockName, location, blockName);
                    WcsScblockDaoImpl.getInstance().updateIsUseByName(blockName, true);
                    AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                    assigningTaskService.assigningTasks();
                } else {
                    Log4j2Util.getChargeLogger().info(String.format("%s : location:%s,该设备充电位置获取失败", blockName, location));
                    WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_RUNNING);
                }
            } else {
                Log4j2Util.getChargeLogger().info(String.format("%s %d 该充电类型未解析", blockName, chargeType));
                WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_CHARGE);
            }
        }
    }

    @Override
    public void lowPower(String blockName) {
        WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (BlockConstant.STATUS_RUNNING.equals(scBlock.getStatus())) {
            //  充电开始 创建充电任务
            WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
            String chargeBlockName = charge.getChargeBlockName();
            int countTasks = WcsTaskingDaoImpl.getTaskingDao().countByBlockName(chargeBlockName);
            int countWork = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                    .eq("To_Station",chargeBlockName)
                    .eq("From_Station",chargeBlockName)
                    .eq("Status",WorkPlanConstant.STATUS_WAIT).or().eq("Status",WorkPlanConstant.STATUS_WORKING)
            );
            if (countTasks < 1 && countWork < 1) {
                int scKwh = Integer.parseInt(scBlock.getKwh());
                if (scKwh < SystemCache.SC_CHARGE_FINISH_KWH) {
                    startCharge(blockName);
                }
            }
        } else if (BlockConstant.STATUS_CHARGE.equals(scBlock.getStatus())) {
            WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
            String chargeBlockName = charge.getChargeBlockName();
            int countTasks = WcsTaskingDaoImpl.getTaskingDao().countByBlockName(chargeBlockName);
            int countWork = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                    .eq("To_Station",chargeBlockName)
                    .eq("From_Station",chargeBlockName)
                    .eq("Status",WorkPlanConstant.STATUS_WAIT).or().eq("Status",WorkPlanConstant.STATUS_WORKING)
            );
            if (countTasks > 0 || countWork > 0) {
                int scKwh = Integer.parseInt(scBlock.getKwh());
                if (scKwh > SystemCache.SC_CHARGE_MIN_KWH) {
                    //  充电完成
                    finishCharge(blockName);
                }
            }
        }
    }

    /**
     * 创建充电任务
     *
     * @param endStation   终点站台
     * @param fromStation  起点站台
     * @param Location     货架位置
     * @param workPlanType 工作计划类型
     * @author CalmLake
     * @date 2019/3/20 11:29
     */
    private void createWorkPlan(String endStation, String fromStation, String Location, Integer workPlanType) {
        WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan("", "", Location, endStation, "", workPlanType, fromStation, "");
        AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
        assigningTaskService.assigningTasks();
    }

    /**
     * 创建卸车任务（卸入固定临时位置）
     *
     * @param chargeBlockName 堆垛机/母车数据block名称
     * @author CalmLake
     * @date 2019/4/10 10:52
     */
    private void createOffCarWorkPlan(String chargeBlockName) {
        WcsMachineEntity machine = MachineCache.getMachine(chargeBlockName);
        String scBlockName;
        if (MachineConstant.BYTE_TYPE_ML.equals(machine.getType())) {
            WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(chargeBlockName);
            scBlockName = StandbyCarService.getScBlockName(mlBlock);
            createWorkPlan(chargeBlockName, scBlockName, SystemCache.TEMPORARY_LOCATION, WorkPlanConstant.TYPE_OFF_CAR);
        } else if (MachineConstant.BYTE_TYPE_MC.equals(machine.getType())) {
            WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(chargeBlockName);
            scBlockName = mcBlock.getBingScBlockName();
            createWorkPlan(chargeBlockName, scBlockName, SystemCache.TEMPORARY_LOCATION, WorkPlanConstant.TYPE_OFF_CAR);
        } else {
            Log4j2Util.getChargeLogger().info(String.format("未知类型的设备%s", chargeBlockName));
        }
    }
}
