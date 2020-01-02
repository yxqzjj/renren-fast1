package io.renren.wap.customer.block;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.impl.ScCycleCommandImplAbstract;
import io.renren.wap.command.impl.jiatian.JiaTianScCycleCommandImplAbstract;
import io.renren.wap.entity.constant.CompanyConstant;

/**
 * 穿梭车 cycle完成报告处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/24  13:24
 * @Version: V1.0.0
 **/
public class ScBlockCustomer extends BlockCustomer {
    public ScBlockCustomer(String blockName) {
        super(blockName);
    }

    @Override
    public void doMsgCycleOrderFinishReportDTO(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) throws InterruptedException {
        AbstractMachineCycleCommand machineCycleCommand_sc;
        if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
            machineCycleCommand_sc = new JiaTianScCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        } else {
            machineCycleCommand_sc = new ScCycleCommandImplAbstract(msgCycleOrderFinishReportDTO);
        }
        machineCycleCommand_sc.execute();
    }
}
