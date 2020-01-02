package io.renren.wap.customer.block;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.impl.ClCycleCommandImplAbstract;
import io.renren.wap.command.impl.kerisom.KerisomClCycleCommandImplAbstract;
import io.renren.wap.entity.constant.CompanyConstant;

/**
 * 输送线 cycle完成报告处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/23  16:55
 * @Version: V1.0.0
 **/
public class ClBlockCustomer extends BlockCustomer {
    public ClBlockCustomer(String blockName) {
        super(blockName);
    }

    @Override
    public void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException {
        AbstractMachineCycleCommand abstractMachineCycleCommand_cl;
        if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)){
            abstractMachineCycleCommand_cl =new KerisomClCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        }else {
            abstractMachineCycleCommand_cl =new ClCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        }
        abstractMachineCycleCommand_cl.execute();
    }

}
