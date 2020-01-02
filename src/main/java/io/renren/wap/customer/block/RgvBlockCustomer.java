package io.renren.wap.customer.block;


import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.impl.RgvCycleCommandImplAbstract;

/**
 * rgv cycle完成报告处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/24  13:24
 * @Version: V1.0.0
 **/
public class RgvBlockCustomer extends BlockCustomer {
    public RgvBlockCustomer(String blockName) {
        super(blockName);
    }

    @Override
    public void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException {
        AbstractMachineCycleCommand abstractMachineCycleCommand = new RgvCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        abstractMachineCycleCommand.execute();
    }
}
