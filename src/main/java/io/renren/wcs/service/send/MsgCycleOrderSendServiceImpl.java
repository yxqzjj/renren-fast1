package io.renren.wcs.service.send;

import io.renren.wcs.client.contant.MsgConstant;
import io.renren.wcs.client.dto.MsgCycleOrderDTO;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.service.MsgSendService;

/**
 * @ClassName: MsgCycleOrderSendServiceImpl
 * @Description: MsgCycleOrderDTO 03 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  18:04
 * @Version: V1.0.0
 **/
public class MsgCycleOrderSendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgCycleOrderDTO msgCycleOrderDTO = (MsgCycleOrderDTO) msgDTO;
        String messageNumber = msgCycleOrderDTO.getMessageNumber();
        String commandType = msgCycleOrderDTO.getCommandType();
        String reSend = msgCycleOrderDTO.getReSend();
        String sendTime = msgCycleOrderDTO.getSendTime();
        String mcKey = msgCycleOrderDTO.getMcKey();
        String machineName = msgCycleOrderDTO.getMachineName();
        String cycleCommand = msgCycleOrderDTO.getCycleCommand();
        String cycleType = msgCycleOrderDTO.getCycleType();
        String height = msgCycleOrderDTO.getHeight();
        String width = msgCycleOrderDTO.getWidth();
        String row = msgCycleOrderDTO.getRow();
        String line = msgCycleOrderDTO.getLine();
        String tier = msgCycleOrderDTO.getTier();
        String station = msgCycleOrderDTO.getStation();
        String dock = msgCycleOrderDTO.getDock();
        String bcc = msgCycleOrderDTO.getBcc();
        String message = MsgConstant.STX_STRING +
                messageNumber +
                commandType +
                reSend +
                sendTime +
                mcKey +
                machineName +
                cycleCommand +
                cycleType +
                height +
                width +
                row +
                line +
                tier +
                station +
                dock +
                bcc +
                MsgConstant.ETX_STRING;
        return message.getBytes();
    }
}
