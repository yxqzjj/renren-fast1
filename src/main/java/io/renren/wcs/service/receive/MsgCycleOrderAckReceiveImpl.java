package io.renren.wcs.service.receive;

import io.renren.wcs.client.dto.MsgCycleOrderAckDTO;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.service.MsgReceiveService;
import org.apache.commons.lang.StringUtils;

/**
 * MsgCycleOrderAckDTO 23 消息接收解析 (除去报文头和尾string长度46)
 * @Author: CalmLake
 * @Date: 2018/11/18  15:58
 * @Version: V1.0.0
 **/
public class MsgCycleOrderAckReceiveImpl implements MsgReceiveService {
    @Override
    public MsgDTO getMsgDTO(String msg) {
        MsgCycleOrderAckDTO msgCycleOrderAckDTO = new MsgCycleOrderAckDTO();
        String messageNumber = StringUtils.substring(msg, 0, 4);
        String commandType = StringUtils.substring(msg, 4, 6);
        String reSend = StringUtils.substring(msg, 6, 7);
        String sendTime = StringUtils.substring(msg, 7, 13);
        String mcKey = StringUtils.substring(msg, 13, 17);
        String machineName = StringUtils.substring(msg, 17, 21);
        String cycleCommand = StringUtils.substring(msg, 21, 23);
        String cycleType = StringUtils.substring(msg, 23, 25);
        String height = StringUtils.substring(msg, 25, 26);
        String width = StringUtils.substring(msg, 26, 27);
        String row = StringUtils.substring(msg, 27, 29);
        String line = StringUtils.substring(msg, 29, 31);
        String tier = StringUtils.substring(msg, 31, 33);
        String station = StringUtils.substring(msg, 33, 37);
        String dock = StringUtils.substring(msg, 37, 41);
        String loadStatus = StringUtils.substring(msg, 41, 42);
        String ackType = StringUtils.substring(msg, 42, 43);
        String exceptionType = StringUtils.substring(msg, 43, 44);
        String bcc = StringUtils.substring(msg, 44, 46);
        msgCycleOrderAckDTO.setMessageNumber(messageNumber);
        msgCycleOrderAckDTO.setCommandType(commandType);
        msgCycleOrderAckDTO.setReSend(reSend);
        msgCycleOrderAckDTO.setSendTime(sendTime);
        msgCycleOrderAckDTO.setMcKey(mcKey);
        msgCycleOrderAckDTO.setMachineName(machineName);
        msgCycleOrderAckDTO.setCycleCommand(cycleCommand);
        msgCycleOrderAckDTO.setCycleType(cycleType);
        msgCycleOrderAckDTO.setHeight(height);
        msgCycleOrderAckDTO.setWidth(width);
        msgCycleOrderAckDTO.setRow(row);
        msgCycleOrderAckDTO.setLine(line);
        msgCycleOrderAckDTO.setTier(tier);
        msgCycleOrderAckDTO.setStation(station);
        msgCycleOrderAckDTO.setDock(dock);
        msgCycleOrderAckDTO.setLoadStatus(loadStatus);
        msgCycleOrderAckDTO.setAckType(ackType);
        msgCycleOrderAckDTO.setExceptionType(exceptionType);
        msgCycleOrderAckDTO.setBcc(bcc);
        return msgCycleOrderAckDTO;
    }
}
