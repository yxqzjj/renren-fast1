package io.renren.wcs.service.receive;

import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.client.dto.MsgHeartBeatSignalAckDTO;
import io.renren.wcs.service.MsgReceiveService;
import org.apache.commons.lang.StringUtils;

/**
 * MsgHeartBeatSignalAckDTO 30 消息接收解析 (除去报文头和尾string长度21)
 * @Author: CalmLake
 * @Date: 2018/11/18  16:06
 * @Version: V1.0.0
 **/
public class MsgHeartBeatSignalAckReceiveServiceImpl implements MsgReceiveService {
    @Override
    public MsgDTO getMsgDTO(String msg) {
        MsgHeartBeatSignalAckDTO msgHeartBeatSignalAckDTO = new MsgHeartBeatSignalAckDTO();
        String messageNumber = StringUtils.substring(msg, 0, 4);
        String commandType = StringUtils.substring(msg, 4, 6);
        String reSend = StringUtils.substring(msg, 6, 7);
        String sendTime = StringUtils.substring(msg, 7, 13);
        String wcsNo = StringUtils.substring(msg, 13, 14);
        String consoleNo = StringUtils.substring(msg, 14, 18);
        String heartBeat = StringUtils.substring(msg, 18, 19);
        String bcc = StringUtils.substring(msg, 19, 21);
        msgHeartBeatSignalAckDTO.setMessageNumber(messageNumber);
        msgHeartBeatSignalAckDTO.setCommandType(commandType);
        msgHeartBeatSignalAckDTO.setReSend(reSend);
        msgHeartBeatSignalAckDTO.setSendTime(sendTime);
        msgHeartBeatSignalAckDTO.setWcsNo(wcsNo);
        msgHeartBeatSignalAckDTO.setConsoleNo(consoleNo);
        msgHeartBeatSignalAckDTO.setHeartBeat(heartBeat);
        msgHeartBeatSignalAckDTO.setBcc(bcc);
        return msgHeartBeatSignalAckDTO;
    }
}
