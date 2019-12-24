package io.renren.wcs.service.send;


import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.client.dto.MsgStopMachineryDTO;
import io.renren.wcs.service.MsgSendService;
import io.renren.wcs.client.util.MsgMakeBytesUtil;

import java.util.List;

/**
 * @ClassName: MsgStopMachinerySendServiceImpl
 * @Description: MsgStopMachineryDTO 02 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  16:39
 * @Version: V1.0.0
 **/
public class MsgStopMachinerySendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgStopMachineryDTO msgStopMachineryDTO = (MsgStopMachineryDTO) msgDTO;
        String messageNumber = msgStopMachineryDTO.getMessageNumber();
        String commandType = msgStopMachineryDTO.getCommandType();
        String reSend = msgStopMachineryDTO.getReSend();
        String sendTime = msgStopMachineryDTO.getSendTime();
        String dataNum = msgStopMachineryDTO.getDataNum();
        List<String> machineNameList = msgStopMachineryDTO.getMachineNameList();
        String bcc = msgStopMachineryDTO.getBcc();
        byte[] bytes = MsgMakeBytesUtil.makeStartOrStopMachineryBytes(messageNumber, commandType, reSend, sendTime, dataNum, machineNameList, bcc);
        return bytes;
    }
}
