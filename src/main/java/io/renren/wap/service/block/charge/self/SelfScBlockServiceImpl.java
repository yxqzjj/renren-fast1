package io.renren.wap.service.block.charge.self;


import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.service.block.charge.ChargeServiceInterface;
import io.renren.wap.service.block.impl.ScBlockServiceImpl;
import io.renren.wap.service.msg.ScMsgService;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * (穿梭车在堆垛机上充电)穿梭车充电消息制作逻辑
 *
 * @Author: CalmLake
 * @Date: 2019/3/20  15:13
 * @Version: V1.0.0
 **/
public class SelfScBlockServiceImpl extends ScBlockServiceImpl implements ChargeServiceInterface {

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
        if (mcKeyWorkPlan.equals(mcKey) || mcKeyWorkPlan.equals(appointmentMcKey)) {
            if (StringUtils.isEmpty(hostBlockName)) {
                return getCar(scMsgService, mcKeyWorkPlan, workPlanType, blockName, row, line, tier, withWorkBlockName);
            }
        }else {
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
