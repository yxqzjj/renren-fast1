package io.renren.wcs.service.receive;


import io.renren.wcs.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.service.MsgReceiveService;
import org.apache.commons.lang.StringUtils;
/**
 * MsgCycleOrderFinishReportDTO 35 消息接收解析 (除去报文头和尾string长度46)
 * @Author: CalmLake
 * @Date: 2018/11/18  16:01
 * @Version: V1.0.0
 **/
public class MsgCycleOrderFinishReportReceiveServiceImpl implements MsgReceiveService {
    @Override
    public MsgDTO getMsgDTO(String msg) {
        MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO=new MsgCycleOrderFinishReportDTO();
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
        String finishedType = StringUtils.substring(msg, 42, 43);
        String finishedCode = StringUtils.substring(msg, 43, 44);
        String bcc = StringUtils.substring(msg, 44, 46);
        msgCycleOrderFinishReportDTO.setMessageNumber(messageNumber);
        msgCycleOrderFinishReportDTO.setCommandType(commandType);
        msgCycleOrderFinishReportDTO.setReSend(reSend);
        msgCycleOrderFinishReportDTO.setSendTime(sendTime);
        msgCycleOrderFinishReportDTO.setMcKey(mcKey);
        msgCycleOrderFinishReportDTO.setMachineName(machineName);
        msgCycleOrderFinishReportDTO.setCycleCommand(cycleCommand);
        msgCycleOrderFinishReportDTO.setCycleType(cycleType);
        msgCycleOrderFinishReportDTO.setHeight(height);
        msgCycleOrderFinishReportDTO.setWidth(width);
        msgCycleOrderFinishReportDTO.setRow(row);
        msgCycleOrderFinishReportDTO.setLine(line);
        msgCycleOrderFinishReportDTO.setTier(tier);
        msgCycleOrderFinishReportDTO.setStation(station);
        msgCycleOrderFinishReportDTO.setDock(dock);
        msgCycleOrderFinishReportDTO.setLoadStatus(loadStatus);
        msgCycleOrderFinishReportDTO.setFinishType(finishedType);
        msgCycleOrderFinishReportDTO.setFinishCode(finishedCode);
        msgCycleOrderFinishReportDTO.setBcc(bcc);
        return msgCycleOrderFinishReportDTO;
    }
}
