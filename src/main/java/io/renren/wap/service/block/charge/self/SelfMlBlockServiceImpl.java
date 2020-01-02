package io.renren.wap.service.block.charge.self;


import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.service.block.charge.ChargeServiceInterface;
import io.renren.wap.service.block.impl.MlBlockServiceImpl;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * (子车在堆垛机上充电）)堆垛机充电消息制作逻辑
 *
 * @Author: CalmLake
 * @Date: 2019/3/21  9:06
 * @Version: V1.0.0
 **/
public class SelfMlBlockServiceImpl extends MlBlockServiceImpl implements ChargeServiceInterface {
    @Override
    public MsgDTO chargeUp(WcsWorkplanEntity workPlan, Object object) {
        WcsMlblockEntity mlBlock = (WcsMlblockEntity) object;
        Integer workPlanType = workPlan.getType();
        String blockName = mlBlock.getName();
        String row = mlBlock.getRow();
        String line = mlBlock.getLine();
        String tier = mlBlock.getTier();
        String appointmentMcKey = mlBlock.getAppointmentMckey();
        String mcKey = mlBlock.getMckey();
        String scBlockName = mlBlock.getScBlockName();
        String bingScBlockName = mlBlock.getBingScBlockName();
        String mcKeyWorkPlan = workPlan.getMckey();
        if (mcKeyWorkPlan.equals(mcKey) || mcKeyWorkPlan.equals(appointmentMcKey)) {
            if (StringUtils.isEmpty(scBlockName) || !bingScBlockName.equals(scBlockName)) {
                if (StringUtils.isEmpty(scBlockName)) {
                    return scBlockNameIsEmptyWork(mlBlock, mcKey, blockName, workPlanType, row, line, tier);
                }
            }
            if (StringUtils.isNotEmpty(scBlockName) && bingScBlockName.equals(scBlockName)) {
                WcsScblockEntity scBlock= WcsScblockDaoImpl.getInstance().selectByPrimaryKey(scBlockName);
                if (StringUtils.isNotEmpty(scBlock.getHostBlockName()) && blockName.equals(scBlock.getHostBlockName())) {
                    WcsMlblockDaoImpl.getInstance().updateMcKey("",blockName);
                    WcsScblockDaoImpl.getInstance().updateMcKey("", scBlockName);
                    WcsScblockDaoImpl.getInstance().updateStatus(scBlockName, BlockConstant.STATUS_CHARGE);
                    WorkPlanService.finishWorkPlan(workPlan.getId(), mcKey);
                }
            }
        } else {
            Log4j2Util.getChargeLogger().info(String.format("工作计划标识：%s,block mcKey：%s,block appointmentMcKey：%s", mcKeyWorkPlan, mcKey, appointmentMcKey));
            return null;
        }
        return null;
    }

    @Override
    public MsgDTO chargeFinish(WcsWorkplanEntity workPlan, Object object) {
        return null;
    }
}
