package io.renren.wap.customer.block;


import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.impl.AlCycleCommandImplAbstract;

/**
 * 升降机 cycle完成报告处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/23  15:19
 * @Version: V1.0.0
 **/
public class AlBlockCustomer extends BlockCustomer {
    public AlBlockCustomer(String blockName) {
        super(blockName);
    }

    @Override
    public void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException {
        AbstractMachineCycleCommand abstractMachineCycleCommand = new AlCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        abstractMachineCycleCommand.execute();
    }
}
