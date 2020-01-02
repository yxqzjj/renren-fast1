package io.renren.wap.service.block.charge.shelf;


import io.renren.modules.generator.dao.impl.WcsChargeDaoImpl;
import io.renren.modules.generator.entity.WcsChargeEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.ChargeConstant;
import io.renren.wap.service.block.charge.ChargeServiceInterface;
import io.renren.wap.service.block.impl.ScBlockServiceImpl;
import io.renren.wap.service.msg.ScMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;

/**
 * (穿梭车在货架上充电)穿梭车充电消息制作逻辑
 *
 * @Author: CalmLake
 * @Date: 2019/4/9  15:06
 * @Version: V1.0.0
 **/
public class ShelfScBlockServiceImpl extends ScBlockServiceImpl implements ChargeServiceInterface {
    @Override
    public MsgDTO chargeUp(WcsWorkplanEntity workPlan, Object object) {
        WcsScblockEntity scBlock = (WcsScblockEntity) object;
        ScMsgService scMsgService = new ScMsgService();
        String blockName = scBlock.getName();
        String hostBlockName = scBlock.getHostBlockName();
        String withWorkBlockName = scBlock.getWithWorkBlockName();
        String appointmentMcKey = scBlock.getAppointmentMckey();
        String mcKey = scBlock.getMckey();
        String row = scBlock.getRow();
        String line = scBlock.getLine();
        String tier = scBlock.getTier();
        Integer workPlanType = workPlan.getType();
        String mcKeyWorkPlan = workPlan.getMckey();
        String toLocation = workPlan.getToLocation();
        WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
        Integer chargeType = charge.getType();
        String chargeBlockName = charge.getChargeBlockName();
        String reserved1 = charge.getReserved1();
        String chargeLocationA = SystemCache.SYS_CHARGE_LOCATION_A;
        String chargeLocationB = SystemCache.SYS_CHARGE_LOCATION_B;
        if (mcKeyWorkPlan.equals(mcKey) || mcKeyWorkPlan.equals(appointmentMcKey)) {
            if (StringUtils.isEmpty(hostBlockName)) {
                if (ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                    if (isSameSite(row, line, tier, toLocation)) {
                        return scMsgService.startCharge(mcKey, workPlanType, blockName, row, line, tier);
                    } else {
                        return getCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName);
                    }
                } else if (ChargeConstant.TYPE_OTHER_ONR_MACHINE.equals(chargeType)) {
                    if (isSameSite(row, line, tier, chargeLocationA)) {
                        return scMsgService.move(mcKey, blockName, chargeLocationB,workPlanType);
                    } else if (isSameSite(row, line, tier, toLocation)) {
                        return scMsgService.startCharge(mcKey, workPlanType, blockName, row, line, tier);
                    } else {
                        return getCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName);
                    }
                }
            } else {
                if (ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                    return offCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName, toLocation);
                } else if (ChargeConstant.TYPE_OTHER_ONR_MACHINE.equals(chargeType)) {
                    if (withWorkBlockName.equals(reserved1)) {
                        return offCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName, chargeLocationA);
                    } else if (withWorkBlockName.equals(chargeBlockName)) {
                        return offCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName, toLocation);
                    } else {
                        Log4j2Util.getChargeLogger().info(String.format("withWorkBlockName：%s, chargeBlockName：%s, reserved1：%s", withWorkBlockName, chargeBlockName, reserved1));
                        return null;
                    }
                }
            }
        } else {
            Log4j2Util.getChargeLogger().info(String.format("工作计划标识：%s, mcKey：%s, appointmentMcKey：%s", mcKeyWorkPlan, mcKey, appointmentMcKey));
            return null;
        }
        return null;
    }

    @Override
    public MsgDTO chargeFinish(WcsWorkplanEntity workPlan, Object object) {
        WcsScblockEntity scBlock = (WcsScblockEntity) object;
        ScMsgService scMsgService = new ScMsgService();
        String blockName = scBlock.getName();
        String hostBlockName = scBlock.getHostBlockName();
        String withWorkBlockName = scBlock.getWithWorkBlockName();
        String appointmentMcKey = scBlock.getAppointmentMckey();
        String mcKey = scBlock.getMckey();
        String row = scBlock.getRow();
        String line = scBlock.getLine();
        String tier = scBlock.getTier();
        Integer workPlanType = workPlan.getType();
        String mcKeyWorkPlan = workPlan.getMckey();
        String location = workPlan.getFromLocation();
        WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
        Integer chargeType = charge.getType();
        String reserved1 = charge.getChargeBlockName();
        String chargeBlockName = charge.getReserved1();
        String chargeLocationA = SystemCache.SYS_CHARGE_LOCATION_A;
        String chargeLocationB = SystemCache.SYS_CHARGE_LOCATION_B;
        if (mcKeyWorkPlan.equals(mcKey) || mcKeyWorkPlan.equals(appointmentMcKey)) {
            if (StringUtils.isEmpty(hostBlockName)) {
                if (ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                    if (isSameSite(row, line, tier, location)) {
                        if (BlockConstant.BERTH_BLOCK_NAME_CHARGE.equals(scBlock.getBerthBlockName())) {
                            return scMsgService.finishCharge(mcKey, workPlanType, blockName, row, line, tier);
                        }else {
                            return getCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName);
                        }
                    } else {
                        return getCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName);
                    }
                } else if (ChargeConstant.TYPE_OTHER_ONR_MACHINE.equals(chargeType)) {
                    if (isSameSite(row, line, tier, chargeLocationB)) {
                        return scMsgService.move(mcKey, blockName, chargeLocationA,workPlanType);
                    } else if (isSameSite(row, line, tier, location)) {
                        if (BlockConstant.BERTH_BLOCK_NAME_CHARGE.equals(scBlock.getBerthBlockName())) {
                            return scMsgService.finishCharge(mcKey, workPlanType, blockName, row, line, tier);
                        } else {
                            return getCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName);
                        }
                    }else {
                        return getCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName);
                    }
                }
            } else {
                if (ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(chargeType)) {
                    return null;
                } else if (ChargeConstant.TYPE_OTHER_ONR_MACHINE.equals(chargeType)) {
                    if (withWorkBlockName.equals(reserved1) && hostBlockName.equals(withWorkBlockName)) {
                        return offCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName, chargeLocationB);
                    } else if (withWorkBlockName.equals(chargeBlockName)) {
                        return null;
                    } else {
                        Log4j2Util.getChargeLogger().info(String.format("withWorkBlockName：%s, chargeBlockName：%s, reserved1：%s", withWorkBlockName, chargeBlockName, reserved1));
                        return null;
                    }
                }
            }
        } else {
            Log4j2Util.getChargeLogger().info(String.format("工作计划标识：%s, mcKey：%s, appointmentMcKey：%s", mcKeyWorkPlan, mcKey, appointmentMcKey));
            return null;
        }
        return null;
    }
}
