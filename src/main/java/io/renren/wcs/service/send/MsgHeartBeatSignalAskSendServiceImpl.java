package io.renren.wcs.service.send;

import io.renren.wcs.client.contant.MsgConstant;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.client.dto.MsgHeartBeatSignalAskDTO;
import io.renren.wcs.service.MsgSendService;

/**
 * MsgHeartBeatSignalAskDTO 10 消息发送封装
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  18:04
 * @Version: V1.0.0
 **/
public class MsgHeartBeatSignalAskSendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgHeartBeatSignalAskDTO msgHeartBeatSignalAskDTO = (MsgHeartBeatSignalAskDTO) msgDTO;
        String messageNumber = msgHeartBeatSignalAskDTO.getMessageNumber();
        String commandType = msgHeartBeatSignalAskDTO.getCommandType();
        String reSend = msgHeartBeatSignalAskDTO.getReSend();
        String sendTime = msgHeartBeatSignalAskDTO.getSendTime();
        String wcsNo = msgHeartBeatSignalAskDTO.getWcsNo();
        String consoleNo = msgHeartBeatSignalAskDTO.getConsoleNo();
        String heartBeat = msgHeartBeatSignalAskDTO.getHeartBeat();
        String bcc = msgHeartBeatSignalAskDTO.getBcc();
        String message = MsgConstant.STX_STRING +
                messageNumber +
                commandType +
                reSend +
                sendTime +
                wcsNo +
                consoleNo +
                heartBeat +
                bcc +
                MsgConstant.ETX_STRING;
        return message.getBytes();
    }
}
