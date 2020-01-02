package io.renren.wap.customer.block;


import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.impl.McCycleCommandImplAbstract;

/**
 * 母车 cycle完成报告处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/23  17:30
 * @Version: V1.0.0
 **/
public class McBlockCustomer extends BlockCustomer {
    public McBlockCustomer(String blockName) {
        super(blockName);
    }

    @Override
    public void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException {
        AbstractMachineCycleCommand abstractMachineCycleCommand = new McCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        abstractMachineCycleCommand.execute();
    }
}
