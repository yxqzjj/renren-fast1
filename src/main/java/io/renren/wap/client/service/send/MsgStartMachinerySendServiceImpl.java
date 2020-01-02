package io.renren.wap.client.service.send;


import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.client.dto.MsgStartMachineryDTO;
import io.renren.wap.client.service.MsgSendService;
import io.renren.wap.client.util.MsgMakeBytesUtil;

import java.util.List;

/**
 * @ClassName: MsgStartMachinerySendServiceImpl
 * @Description: MsgStartMachineryDTO 01 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  16:04
 * @Version: V1.0.0
 **/
public class MsgStartMachinerySendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgStartMachineryDTO msgStartMachineryDTO = (MsgStartMachineryDTO) msgDTO;
        String messageNumber = msgStartMachineryDTO.getMessageNumber();
        String commandType = msgStartMachineryDTO.getCommandType();
        String reSend = msgStartMachineryDTO.getReSend();
        String sendTime = msgStartMachineryDTO.getSendTime();
        String dataNum = msgStartMachineryDTO.getDataNum();
        List<String> machineNameList = msgStartMachineryDTO.getMachineNameList();
        String bcc = msgStartMachineryDTO.getBcc();
        return MsgMakeBytesUtil.makeStartOrStopMachineryBytes(messageNumber, commandType, reSend, sendTime, dataNum, machineNameList, bcc);
    }

}
