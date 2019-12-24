package io.renren.wcs.service;

import io.renren.modules.generator.entity.WcsWcsmessagelogEntity;

import java.util.Date;

/**
 * wcs和设备交互消息操作
 *
 * @Author: CalmLake
 * @Date: 2019/6/3  15:16
 * @Version: V1.0.0
 **/
public class WcsMessageLogService {

    /**
     * 插入消息记录表
     *
     * @param plcName   plc名称
     * @param msg       消息
     * @param type      消息类型 1-发送，2-接收
     * @param reserved1 预留字段1
     * @param reserved2 预留字段2
     * @author CalmLake
     * @date 2019/2/19 10:36
     */
    public void insertIntoWcsMessageLog(String plcName, String msg, byte type, String reserved1, String reserved2) {
        WcsWcsmessagelogEntity wcsMessageLog = new WcsWcsmessagelogEntity();
        wcsMessageLog.setMessage(msg);
        wcsMessageLog.setCreateTime(new Date());
        wcsMessageLog.setPlcName(plcName);
        wcsMessageLog.setType(new Integer(type));
        wcsMessageLog.setReserved1(reserved1);
        wcsMessageLog.setReserved2(reserved2);
       // DbUtil.getWCSMessageLogDao().insert(wcsMessageLog);
    }

    /**
     * 记录命令消息
     *
     * @param msgDTO 消息对象
     * @author CalmLake
     * @date 2019/2/19 13:33
     */
   /* public void insertIntoCommandLog(MsgDTO msgDTO) {
        CommandLog commandLog = null;
        if (msgDTO instanceof MsgCycleOrderDTO) {
            commandLog = new CommandLog();
            commandLog.setBlockName(((MsgCycleOrderDTO) msgDTO).getMachineName());
            commandLog.setCommand(msgDTO.getCommandType());
            commandLog.setMcKey(((MsgCycleOrderDTO) msgDTO).getMcKey());
            commandLog.setCycleCommand(((MsgCycleOrderDTO) msgDTO).getCycleCommand());
            commandLog.setCycleType(((MsgCycleOrderDTO) msgDTO).getCycleType());
            commandLog.setCreateTime(new Date());
            commandLog.setSeqNo(msgDTO.getMessageNumber());
            commandLog.setResend(msgDTO.getReSend());
            commandLog.setDock(((MsgCycleOrderDTO) msgDTO).getDock());
            commandLog.setStation(((MsgCycleOrderDTO) msgDTO).getStation());
            commandLog.setTier(((MsgCycleOrderDTO) msgDTO).getTier());
            commandLog.setRow(((MsgCycleOrderDTO) msgDTO).getRow());
            commandLog.setLine(((MsgCycleOrderDTO) msgDTO).getLine());
            commandLog.setErrorType("");
            commandLog.setAckType("");
            commandLog.setFinishType("");
            commandLog.setLoad("");
            commandLog.setReserved1(msgDTO.getPlcName());
            commandLog.setReserved2("");
        } else if (msgDTO instanceof MsgCycleOrderAckDTO) {
            commandLog = new CommandLog();
            commandLog.setBlockName(((MsgCycleOrderAckDTO) msgDTO).getMachineName());
            commandLog.setCommand(msgDTO.getCommandType());
            commandLog.setMcKey(((MsgCycleOrderAckDTO) msgDTO).getMcKey());
            commandLog.setCycleCommand(((MsgCycleOrderAckDTO) msgDTO).getCycleCommand());
            commandLog.setCycleType(((MsgCycleOrderAckDTO) msgDTO).getCycleType());
            commandLog.setCreateTime(new Date());
            commandLog.setSeqNo(msgDTO.getMessageNumber());
            commandLog.setResend(msgDTO.getReSend());
            commandLog.setDock(((MsgCycleOrderAckDTO) msgDTO).getDock());
            commandLog.setStation(((MsgCycleOrderAckDTO) msgDTO).getStation());
            commandLog.setTier(((MsgCycleOrderAckDTO) msgDTO).getTier());
            commandLog.setRow(((MsgCycleOrderAckDTO) msgDTO).getRow());
            commandLog.setLine(((MsgCycleOrderAckDTO) msgDTO).getLine());
            commandLog.setErrorType(((MsgCycleOrderAckDTO) msgDTO).getExceptionType());
            commandLog.setAckType(((MsgCycleOrderAckDTO) msgDTO).getAckType());
            commandLog.setFinishType("");
            commandLog.setLoad(((MsgCycleOrderAckDTO) msgDTO).getLoadStatus());
            commandLog.setReserved1(msgDTO.getPlcName());
            commandLog.setReserved2("");
        } else if (msgDTO instanceof MsgCycleOrderFinishReportDTO) {
            commandLog = new CommandLog();
            commandLog.setBlockName(((MsgCycleOrderFinishReportDTO) msgDTO).getMachineName());
            commandLog.setCommand(msgDTO.getCommandType());
            commandLog.setMcKey(((MsgCycleOrderFinishReportDTO) msgDTO).getMcKey());
            commandLog.setCycleCommand(((MsgCycleOrderFinishReportDTO) msgDTO).getCycleCommand());
            commandLog.setCycleType(((MsgCycleOrderFinishReportDTO) msgDTO).getCycleType());
            commandLog.setCreateTime(new Date());
            commandLog.setSeqNo(msgDTO.getMessageNumber());
            commandLog.setResend(msgDTO.getReSend());
            commandLog.setDock(((MsgCycleOrderFinishReportDTO) msgDTO).getDock());
            commandLog.setStation(((MsgCycleOrderFinishReportDTO) msgDTO).getStation());
            commandLog.setTier(((MsgCycleOrderFinishReportDTO) msgDTO).getTier());
            commandLog.setRow(((MsgCycleOrderFinishReportDTO) msgDTO).getRow());
            commandLog.setLine(((MsgCycleOrderFinishReportDTO) msgDTO).getLine());
            commandLog.setErrorType("");
            commandLog.setAckType("");
            commandLog.setFinishType(((MsgCycleOrderFinishReportDTO) msgDTO).getFinishType());
            commandLog.setLoad(((MsgCycleOrderFinishReportDTO) msgDTO).getLoadStatus());
            commandLog.setReserved1(msgDTO.getPlcName());
            commandLog.setReserved2(((MsgCycleOrderFinishReportDTO) msgDTO).getFinishCode());
        } else if (msgDTO instanceof MsgCycleOrderFinishReportAckDTO) {
            commandLog = new CommandLog();
            commandLog.setCommand(msgDTO.getCommandType());
            commandLog.setMcKey(((MsgCycleOrderFinishReportAckDTO) msgDTO).getMcKey());
            commandLog.setCreateTime(new Date());
            commandLog.setSeqNo(msgDTO.getMessageNumber());
            commandLog.setResend(msgDTO.getReSend());
            commandLog.setBlockName(((MsgCycleOrderFinishReportAckDTO) msgDTO).getBlockName());
            commandLog.setCycleCommand(((MsgCycleOrderFinishReportAckDTO) msgDTO).getCycleCommand());
            commandLog.setErrorType("");
            commandLog.setAckType(((MsgCycleOrderFinishReportAckDTO) msgDTO).getAckType());
            commandLog.setFinishType("");
            commandLog.setLoad("");
            commandLog.setReserved1(msgDTO.getPlcName());
            commandLog.setReserved2("");
        }
        if (commandLog != null) {
            DbUtil.getCommandLogDao().insert(commandLog);
        }
    }*/
}
