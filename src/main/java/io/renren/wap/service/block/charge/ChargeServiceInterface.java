package io.renren.wap.service.block.charge;

import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.client.dto.MsgDTO;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/3/20  14:39
 * @Version: V1.0.0
 **/
public interface ChargeServiceInterface {


    /**
     * 充电开始
     *
     * @param workPlan 工作计划
     * @param object   数据block
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/3/20 15:09
     */
    MsgDTO chargeUp(WcsWorkplanEntity workPlan, Object object);

    /**
     * 充电完成
     *
     * @param workPlan 工作计划
     * @param object   数据block
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/3/20 15:09
     */
    MsgDTO chargeFinish(WcsWorkplanEntity workPlan, Object object);
}
