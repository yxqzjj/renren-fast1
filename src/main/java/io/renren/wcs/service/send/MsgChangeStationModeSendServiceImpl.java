package io.renren.wcs.service.send;


import io.renren.wcs.client.contant.MsgConstant;
import io.renren.wcs.client.dto.MsgChangeStationModeDTO;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.service.MsgSendService;

/**
 * @ClassName: MsgChangeStationModeSendServiceImpl
 * @Description: MsgChangeStationModeDTO 40 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  18:04
 * @Version: V1.0.0
 **/
public class MsgChangeStationModeSendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgChangeStationModeDTO msgChangeStationModeDTO = (MsgChangeStationModeDTO) msgDTO;
        String messageNumber = msgChangeStationModeDTO.getMessageNumber();
        String commandType = msgChangeStationModeDTO.getCommandType();
        String reSend = msgChangeStationModeDTO.getReSend();
        String sendTime = msgChangeStationModeDTO.getSendTime();
        String station = msgChangeStationModeDTO.getStation();
        String mode = msgChangeStationModeDTO.getMode();
        String bcc = msgChangeStationModeDTO.getBcc();
        StringBuilder message = new StringBuilder();
        message.append(MsgConstant.STX_STRING);
        message.append(messageNumber);
        message.append(commandType);
        message.append(reSend);
        message.append(sendTime);
        message.append(station);
        message.append(mode);
        message.append(bcc);
        message.append(MsgConstant.ETX_STRING);
        return message.toString().getBytes();
    }
}
