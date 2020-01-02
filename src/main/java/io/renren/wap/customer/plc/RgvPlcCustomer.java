package io.renren.wap.customer.plc;


import io.renren.wap.client.dto.MsgChangeStationModeAckDTO;
import io.renren.wap.client.dto.MsgConveyorLineDataReportDTO;
import io.renren.wap.client.dto.MsgDeleteDataAckDTO;
import io.renren.wap.client.dto.MsgMachineryStatusOrderAckDTO;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.util.DbUtil;

import java.util.List;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  17:23
 * @Version: V1.0.0
 **/
public class RgvPlcCustomer extends AbstractPlcCustomer {
    public RgvPlcCustomer(String plcName) {
        super(plcName);
    }

    @Override
    void doMsgChangeStationModeAckDTO(MsgChangeStationModeAckDTO msgChangeStationModeAckDTO) {
        // 无站台模式切换
    }

    @Override
    void doMsgMachineryStatusOrderAckDTO(MsgMachineryStatusOrderAckDTO msgMachineryStatusOrderAckDTO) {
        BlockService blockService = new BlockService();
        List<MsgMachineryStatusOrderAckDTO.MachineStatus> machineStatusList = msgMachineryStatusOrderAckDTO.getMachineStatusList();
        for (MsgMachineryStatusOrderAckDTO.MachineStatus machineStatus : machineStatusList) {
            String machineName = machineStatus.getMachineName();
            String exceptionCode = machineStatus.getExceptionCode();
            blockService.updateBlockErrorCode(exceptionCode, DbUtil.getRGVBlockDao(), machineName);
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
