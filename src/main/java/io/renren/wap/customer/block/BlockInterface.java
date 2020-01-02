package io.renren.wap.customer.block;

import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/1/23  14:47
 * @Version: V1.0.0
 **/
public interface BlockInterface {
    /**
     * cycle完成报告处理
     *
     * @param msgCycleOrderFinishReportDTO cycle完成报告消息
     * @author CalmLake
     * @date 2019/1/23 14:49
     */
    void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException;

}
