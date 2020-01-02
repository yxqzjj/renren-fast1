package io.renren.wap.client.service.send;


import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.client.dto.MsgDeleteDataDTO;
import io.renren.wap.client.service.MsgSendService;

/**
 * @ClassName: MsgDeleteDataSendServiceImpl
 * @Description: MsgDeleteDataDTO 04 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  18:04
 * @Version: V1.0.0
 **/
public class MsgDeleteDataSendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgDeleteDataDTO msgDeleteDataDTO = (MsgDeleteDataDTO) msgDTO;
        String messageNumber = msgDeleteDataDTO.getMessageNumber();
        String commandType = msgDeleteDataDTO.getCommandType();
        String reSend = msgDeleteDataDTO.getReSend();
        String sendTime = msgDeleteDataDTO.getSendTime();
        String mcKey = msgDeleteDataDTO.getMcKey();
        String machineName = msgDeleteDataDTO.getMachineName();
        String operationType = msgDeleteDataDTO.getOperationType();
        String bcc = msgDeleteDataDTO.getBcc();
        StringBuilder message = new StringBuilder();
        message.append(MsgConstant.STX_STRING);
        message.append(messageNumber);
        message.append(commandType);
        message.append(reSend);
        message.append(sendTime);
        message.append(mcKey);
        message.append(machineName);
        message.append(operationType);
        message.append(bcc);
        message.append(MsgConstant.ETX_STRING);
        return message.toString().getBytes();
    }
}
