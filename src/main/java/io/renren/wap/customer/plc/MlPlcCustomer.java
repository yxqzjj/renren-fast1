package io.renren.wap.customer.plc;


import io.renren.wap.client.dto.MsgChangeStationModeAckDTO;
import io.renren.wap.client.dto.MsgConveyorLineDataReportDTO;
import io.renren.wap.client.dto.MsgDeleteDataAckDTO;
import io.renren.wap.client.dto.MsgMachineryStatusOrderAckDTO;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.util.DbUtil;

import java.util.List;

/**
 * wcs接收堆垛机消息处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  13:24
 * @Version: V1.0.0
 **/
public class MlPlcCustomer extends AbstractPlcCustomer {
    public MlPlcCustomer(String plcName) {
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
            blockService.updateBlockErrorCode(exceptionCode, DbUtil.getMLBlockDao(), machineName);
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
