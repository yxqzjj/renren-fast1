package io.renren.wap.service.block.charge.shelf;


import io.renren.modules.generator.dao.impl.WcsChargeDaoImpl;
import io.renren.modules.generator.entity.WcsChargeEntity;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.ChargeConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.block.charge.ChargeServiceInterface;
import io.renren.wap.service.block.impl.MlBlockServiceImpl;
import io.renren.wap.service.msg.MlMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

/**
 * （子车在货架中充电）堆垛机充电消息制作逻辑
 *
 * @Author: CalmLake
 * @Date: 2019/4/9  11:30
 * @Version: V1.0.0
 **/
public class ShelfMlBlockServiceImpl extends MlBlockServiceImpl implements ChargeServiceInterface {

    @Override
    public MsgDTO chargeUp(WcsWorkplanEntity workPlan, Object object) {
        return operation(workPlan, object);
    }

    @Override
    public MsgDTO chargeFinish(WcsWorkplanEntity workPlan, Object object) {
        return operation(workPlan, object);
    }

    /**
     * 堆垛机逻辑消息处理
     *
     * @param workPlan 工作计划信息
     * @param object   设备block状态
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/4/9 15:03
     */
    private MsgDTO operation(WcsWorkplanEntity workPlan, Object object) {
        WcsMlblockEntity mlBlock = (WcsMlblockEntity) object;
        MlMsgService mlMsgService = new MlMsgService();
        Integer workPlanType = workPlan.getType();
        String toLocation = workPlan.getToLocation();
        String fromLocation = workPlan.getFromLocation();
        String blockName = mlBlock.getName();
        String appointmentMcKey = mlBlock.getAppointmentMckey();
        String mcKey = mlBlock.getMckey();
        String mcKeyWorkPlan = workPlan.getMckey();
        WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(mlBlock.getWithWorkBlockName());
        String chargeBlockName = charge.getChargeBlockName();
        String reserved1 = charge.getReserved1();
        Integer chargeType = charge.getType();
        if (mcKeyWorkPlan.equals(mcKey) || mcKeyWorkPlan.equals(appointmentMcKey)) {
            if (ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                String location;
                if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
                    location = toLocation;
                } else if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
                    location = fromLocation;
                } else {
                    Log4j2Util.getChargeLogger().info(String.format("充电时未解析的工作计划类型,workPlanType: %d",workPlanType));
                    return null;
                }
                return getCarAndPutCarToInStorage(mlMsgService, workPlanType, location, mlBlock, mcKey);
            } else if (ChargeConstant.TYPE_OTHER_ONR_MACHINE.equals(chargeType)) {
                String toStation;
                String transferStation;
                String chargeLocation;
                if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
                    toStation = chargeBlockName;
                    transferStation = reserved1;
                } else if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
                    toStation = reserved1;
                    transferStation = chargeBlockName;
                } else {
                    Log4j2Util.getChargeLogger().info(String.format("充电时未解析的工作计划类型,workPlanType: %d",workPlanType));
                    return null;
                }
                if (blockName.equals(toStation)) {
                    //  此处为目标站台
                    return getCarAndPutCarToInStorage(mlMsgService, workPlanType, toLocation, mlBlock, mcKey);
                } else if (blockName.equals(transferStation)) {
                    if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
                        chargeLocation = SystemCache.SYS_CHARGE_LOCATION_A;
                    } else {
                        chargeLocation = SystemCache.SYS_CHARGE_LOCATION_B;
                    }
                    //  此处为中转站台
                    return getCarAndPutCarToInStorage(mlMsgService, workPlanType, chargeLocation, mlBlock, mcKey);
                } else {
                    Log4j2Util.getChargeLogger().info(String.format("站台信息出错：toStation：%s , transferStation：%s ", toStation, transferStation));
                }
            } else {
                Log4j2Util.getChargeLogger().info(String.format("未解析的充电类型 chargeType：%d ", chargeType));
            }
        } else {
            Log4j2Util.getChargeLogger().info(String.format("工作计划标识：%s,block mcKey：%s,block appointmentMcKey：%s", mcKeyWorkPlan, mcKey, appointmentMcKey));
        }
        return null;
    }
}
