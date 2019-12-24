package io.renren.wcs.service.send;


import io.renren.wcs.client.contant.MsgConstant;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.client.dto.MsgMachineryStatusOrderAskDTO;
import io.renren.wcs.service.MsgSendService;

/**
 * @ClassName: MsgMachineryStatusOrderAskSendServiceImpl
 * @Description: MsgMachineryStatusOrderAskDTO 06 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  18:04
 * @Version: V1.0.0
 **/
public class MsgMachineryStatusOrderAskSendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgMachineryStatusOrderAskDTO msgMachineryStatusOrderAskDTO = (MsgMachineryStatusOrderAskDTO) msgDTO;
        String messageNumber = msgMachineryStatusOrderAskDTO.getMessageNumber();
        String commandType = msgMachineryStatusOrderAskDTO.getCommandType();
        String reSend = msgMachineryStatusOrderAskDTO.getReSend();
        String sendTime = msgMachineryStatusOrderAskDTO.getSendTime();
        String machineName=msgMachineryStatusOrderAskDTO.getMachineName();
        String status=msgMachineryStatusOrderAskDTO.getStatus();
        String bcc = msgMachineryStatusOrderAskDTO.getBcc();
        String message = MsgConstant.STX_STRING +
                messageNumber +
                commandType +
                reSend +
                sendTime +
                machineName +
                status +
                bcc +
                MsgConstant.ETX_STRING;
        return message.getBytes();
    }
}
