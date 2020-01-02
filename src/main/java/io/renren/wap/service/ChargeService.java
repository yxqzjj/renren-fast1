package io.renren.wap.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsChargeDaoImpl;
import io.renren.modules.generator.dao.impl.WcsTaskingDaoImpl;
import io.renren.modules.generator.entity.WcsChargeEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.charge.ChargeImpl;
import io.renren.wap.service.charge.ChargeInterface;
import io.renren.wap.util.DbUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 充电工作计划创建
 *
 * @Author: CalmLake
 * @Date: 2019/3/19  17:05
 * @Version: V1.0.0
 **/
public class ChargeService {
    /**
     * 电量处理
     *
     * @param kWh       电量
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/19 16:21
     */
    public void chargeOperation(String kWh, String blockName) {
        int kwhInt = Integer.parseInt(kWh);
        charge(kwhInt, blockName);
    }

    /**
     * 充电开始处理
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/19 17:15
     */
    private void startCharge(String blockName) {
        ChargeInterface chargeInterface = new ChargeImpl();
        chargeInterface.startCharge(blockName);
    }

    /**
     * 充电完成处理
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/19 17:15
     */
    private void finishCharge(String blockName) {
        ChargeInterface chargeInterface = new ChargeImpl();
        WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
        String chargeBlockName = charge.getChargeBlockName();
        int countTasks = WcsTaskingDaoImpl.getTaskingDao().countByBlockName(chargeBlockName);
        int countWork = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                .eq("To_Station",chargeBlockName)
                .eq("From_Station",chargeBlockName)
                .eq("Status", WorkPlanConstant.STATUS_WAIT).or()
                .eq("Status",WorkPlanConstant.STATUS_WORKING).eq("Type",8)
        );
        if (countTasks > 0 || countWork > 0) {
            chargeInterface.finishCharge(blockName);
        }
    }

    /**
     * 低电量处理
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/20 10:14
     */
    private void lowPower(String blockName) {
        ChargeInterface chargeInterface = new ChargeImpl();
        chargeInterface.lowPower(blockName);
    }

    /**
     * 伽力森电量处理
     *
     * @param kwhInt    电量
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/3/26 17:04
     */
    private void charge(int kwhInt, String blockName) {
        if (kwhInt < SystemCache.SC_CHARGE_MIN_KWH) {
            startCharge(blockName);
        } else if (kwhInt > SystemCache.SC_CHARGE_FINISH_KWH) {
            finishCharge(blockName);
        } else {
            //  检测没有任务时且不处于充电状态  低电量工作状态  适用于单个堆垛机充电场景
            if (SystemCache.KWH_LOWER_SWITCH) {
                lowPower(blockName);
            }
        }
    }

    /**
     * 新增备车充电路径信息
     *
     * @param presentBlockName 当前穿梭车
     * @param newBlockName     备用穿梭车
     * @author CalmLake
     * @date 2019/4/19 14:59
     */
    public void createChargeRoute(String presentBlockName, String newBlockName) {
        WcsChargeEntity chargeOld = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(presentBlockName);
        WcsChargeEntity charge = new WcsChargeEntity();
        charge.setBlockName(newBlockName);
        charge.setChargeBlockName(chargeOld.getChargeBlockName());
        charge.setLocation(StringUtils.isEmpty(chargeOld.getLocation()) ? "" : chargeOld.getLocation());
        charge.setReserved1(StringUtils.isEmpty(chargeOld.getReserved1()) ? "" : chargeOld.getReserved1());
        charge.setReserved2(StringUtils.isEmpty(chargeOld.getReserved2()) ? "" : chargeOld.getReserved2());
        charge.setType(chargeOld.getType());
        DbUtil.getChargeDao().insert(charge);
    }
}
