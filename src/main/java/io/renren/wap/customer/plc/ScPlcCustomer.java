package io.renren.wap.customer.plc;


import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.wap.client.dto.MsgChangeStationModeAckDTO;
import io.renren.wap.client.dto.MsgConveyorLineDataReportDTO;
import io.renren.wap.client.dto.MsgDeleteDataAckDTO;
import io.renren.wap.client.dto.MsgMachineryStatusOrderAckDTO;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.service.ChargeService;
import io.renren.wap.util.DbUtil;

import java.util.List;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  17:22
 * @Version: V1.0.0
 **/
public class ScPlcCustomer extends AbstractPlcCustomer {
    public ScPlcCustomer(String plcName) {
        super(plcName);
    }

    @Override
    void doMsgChangeStationModeAckDTO(MsgChangeStationModeAckDTO msgChangeStationModeAckDTO) {
        // 无站台模式切换
    }

    @Override
    void doMsgMachineryStatusOrderAckDTO(MsgMachineryStatusOrderAckDTO msgMachineryStatusOrderAckDTO) {
        List<MsgMachineryStatusOrderAckDTO.MachineStatus> machineStatusList = msgMachineryStatusOrderAckDTO.getMachineStatusList();
        for (MsgMachineryStatusOrderAckDTO.MachineStatus machineStatus : machineStatusList) {
            String machineName = machineStatus.getMachineName();
            String exceptionCode = machineStatus.getExceptionCode();
            String kWh = machineStatus.getkWh();
            WcsScblockDaoImpl.getInstance().updateBlockErrorCodeAndKWHByPrimaryKey(kWh, machineName, exceptionCode);
            WcsScblockEntity scBlock=WcsScblockDaoImpl.getInstance().selectByPrimaryKey(machineName);
            if (!BlockConstant.STATUS_BAN.equals(scBlock.getStatus())){
                ChargeService chargeService = new ChargeService();
                chargeService.chargeOperation(kWh, machineName);
            }
        }
    }

    @Override
    void doMsgDeleteDataAckDTO(MsgDeleteDataAckDTO msgDeleteDataAckDTO) {
        // 暂无
    }

    @Override
    void doMsgConveyorLineDataReportDTO(MsgConveyorLineDataReportDTO msgConveyorLineDataReportDTO) {
        // 暂无
    }
}
