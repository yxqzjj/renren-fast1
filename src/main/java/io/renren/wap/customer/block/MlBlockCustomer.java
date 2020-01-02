package io.renren.wap.customer.block;

import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.impl.MlCycleCommandImplAbstract;

/**
 * 堆垛机 cycle完成报告处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/24  13:23
 * @Version: V1.0.0
 **/
public class MlBlockCustomer extends BlockCustomer {

    public MlBlockCustomer(String blockName) {
        super(blockName);
    }

    @Override
    public void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException {
        AbstractMachineCycleCommand abstractMachineCycleCommand =new MlCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        abstractMachineCycleCommand.execute();
    }
}
