package io.renren.wap.client.service.send;


import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportAckDTO;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.client.service.MsgSendService;

/**
 * @ClassName: MsgCycleOrderFinishReportAckSendServiceImpl
 * @Description: MsgCycleOrderFinishReportAckDTO 05 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  18:04
 * @Version: V1.0.0
 **/
public class MsgCycleOrderFinishReportAckSendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgCycleOrderFinishReportAckDTO msgCycleOrderFinishReportAckDTO = (MsgCycleOrderFinishReportAckDTO) msgDTO;
        String messageNumber = msgCycleOrderFinishReportAckDTO.getMessageNumber();
        String commandType = msgCycleOrderFinishReportAckDTO.getCommandType();
        String reSend = msgCycleOrderFinishReportAckDTO.getReSend();
        String sendTime = msgCycleOrderFinishReportAckDTO.getSendTime();
        String mcKey = msgCycleOrderFinishReportAckDTO.getMcKey();
        String blockName = msgCycleOrderFinishReportAckDTO.getBlockName();
        String cycleCommand = msgCycleOrderFinishReportAckDTO.getCycleCommand();
        String ackType = msgCycleOrderFinishReportAckDTO.getAckType();
        String bcc = msgCycleOrderFinishReportAckDTO.getBcc();
        StringBuilder message = new StringBuilder();
        message.append(MsgConstant.STX_STRING);
        message.append(messageNumber);
        message.append(commandType);
        message.append(reSend);
        message.append(sendTime);
        message.append(mcKey);
        message.append(blockName);
        message.append(cycleCommand);
        message.append(ackType);
        message.append(bcc);
        message.append(MsgConstant.ETX_STRING);
        return message.toString().getBytes();
    }
}
