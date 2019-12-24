package io.renren.wcs.service.receive;

import io.renren.wcs.client.dto.MsgChangeStationModeAckDTO;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.service.MsgReceiveService;
import org.apache.commons.lang.StringUtils;

/**
 * MsgChangeStationModeAckDTO 42 消息接收解析 (除去报文头和尾string长度21)
 * @Author: CalmLake
 * @Date: 2018/11/18  12:38
 * @Version: V1.0.0
 **/
public class MsgChangeStationModeAckReceiveServiceImpl implements MsgReceiveService {
    @Override
    public MsgDTO getMsgDTO(String msg) {
        MsgChangeStationModeAckDTO msgChangeStationModeAckDTO=new MsgChangeStationModeAckDTO();
        String messageNumber = StringUtils.substring(msg, 0, 4);
        String commandType = StringUtils.substring(msg, 4, 6);
        String reSend = StringUtils.substring(msg, 6, 7);
        String sendTime = StringUtils.substring(msg, 7, 13);
        String station = StringUtils.substring(msg, 13, 17);
        String mode = StringUtils.substring(msg, 17, 19);
        String bcc = StringUtils.substring(msg, 19, 21);
        msgChangeStationModeAckDTO.setMessageNumber(messageNumber);
        msgChangeStationModeAckDTO.setCommandType(commandType);
        msgChangeStationModeAckDTO.setReSend(reSend);
        msgChangeStationModeAckDTO.setSendTime(sendTime);
        msgChangeStationModeAckDTO.setStation(station);
        msgChangeStationModeAckDTO.setMode(mode);
        msgChangeStationModeAckDTO.setBcc(bcc);
        return msgChangeStationModeAckDTO;
    }
}
