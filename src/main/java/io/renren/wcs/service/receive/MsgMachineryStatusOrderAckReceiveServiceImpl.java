package io.renren.wcs.service.receive;

import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.client.dto.MsgMachineryStatusOrderAckDTO;
import io.renren.wcs.service.MsgReceiveService;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MsgMachineryStatusOrderAckDTO 26 消息接收解析
 * @Author: CalmLake
 * @Date: 2018/11/18  16:08
 * @Version: V1.0.0
 **/
public class MsgMachineryStatusOrderAckReceiveServiceImpl implements MsgReceiveService {
    @Override
    public MsgDTO getMsgDTO(String msg) {
        MsgMachineryStatusOrderAckDTO msgMachineryStatusOrderAckDTO = new MsgMachineryStatusOrderAckDTO();
        String messageNumber = StringUtils.substring(msg, 0, 4);
        String commandType = StringUtils.substring(msg, 4, 6);
        String reSend = StringUtils.substring(msg, 6, 7);
        String sendTime = StringUtils.substring(msg, 7, 13);
        String dataNum = StringUtils.substring(msg, 13, 14);
        String machineStatusStrings = StringUtils.substring(msg, 14, msg.length() - 2);
        String bcc = StringUtils.substring(msg, msg.length() - 2, msg.length());
        int num = Integer.valueOf(dataNum);
        List<MsgMachineryStatusOrderAckDTO.MachineStatus> machineStatusList = new ArrayList<>();
        for (int i = 0; i < num * 26; i += 26) {
            MsgMachineryStatusOrderAckDTO.MachineStatus machineStatus = new MsgMachineryStatusOrderAckDTO.MachineStatus();
            machineStatus.setMachineName(StringUtils.substring(machineStatusStrings, i, i + 4));
            machineStatus.setHostMachineNo(StringUtils.substring(machineStatusStrings, i + 4, i + 8));
            machineStatus.setRow(StringUtils.substring(machineStatusStrings, i + 8, i + 10));
            machineStatus.setLine(StringUtils.substring(machineStatusStrings, i + 10, i + 12));
            machineStatus.setTier(StringUtils.substring(machineStatusStrings, i + 12, i + 14));
            machineStatus.setStatus_running(StringUtils.substring(machineStatusStrings, i + 14, i + 15));
            machineStatus.setStatus_stopped(StringUtils.substring(machineStatusStrings, i + 15, i + 16));
            machineStatus.setStatus_exception(StringUtils.substring(machineStatusStrings, i + 16, i + 17));
            machineStatus.setStatus_e_stop(StringUtils.substring(machineStatusStrings, i + 17, i + 18));
            machineStatus.setStatus_offline(StringUtils.substring(machineStatusStrings, i + 18, i + 19));
            machineStatus.setStatus_kWh_ok(StringUtils.substring(machineStatusStrings, i + 19, i + 20));
            machineStatus.setStatus_kWh_small(StringUtils.substring(machineStatusStrings, i + 20, i + 21));
            machineStatus.setkWh(StringUtils.substring(machineStatusStrings, i + 21, i + 24));
            machineStatus.setExceptionCode(StringUtils.substring(machineStatusStrings, i + 24, i + 26));
            machineStatusList.add(machineStatus);
        }
        msgMachineryStatusOrderAckDTO.setMessageNumber(messageNumber);
        msgMachineryStatusOrderAckDTO.setCommandType(commandType);
        msgMachineryStatusOrderAckDTO.setReSend(reSend);
        msgMachineryStatusOrderAckDTO.setSendTime(sendTime);
        msgMachineryStatusOrderAckDTO.setDataNum(dataNum);
        msgMachineryStatusOrderAckDTO.setMachineStatusList(machineStatusList);
        msgMachineryStatusOrderAckDTO.setBcc(bcc);
        return msgMachineryStatusOrderAckDTO;
    }
}
