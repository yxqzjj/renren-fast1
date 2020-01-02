package io.renren.wap.service.msg;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgChangeStationModeDTO;
import io.renren.wap.client.dto.MsgCycleOrderDTO;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.client.util.MsgCreateUtil;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;

/**
 * 输送线消息
 *
 * @Author: CalmLake
 * @Date: 2019/1/15  16:56
 * @Version: V1.0.0
 **/
public class ClMsgService extends MsgService {

    /**
     * 移载卸货
     *
     * @param workPlanType      工作计划类型
     * @param mcKey             mcKey
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作数据block名称
     * @param plcName           plc名称
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/15 17:05
     */
    public MsgDTO transplantingTheUnloading(Integer workPlanType, String mcKey, String blockName, String withWorkBlockName, String plcName) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(plcName);
        msgCycleOrderDTO.setStation(MsgCreateUtil.replaceSendMsgBlockName(withWorkBlockName));
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setLine(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setRow(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TRANSPLANTING_THE_UNLOADING_08);
        msgCycleOrderDTO.setMachineName(MsgCreateUtil.replaceSendMsgBlockName(blockName));
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 移载取货
     *
     * @param workPlanType      工作计划类型
     * @param mcKey             mcKey
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作数据block名称
     * @param plcName           plc名称
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/15 17:05
     */
    public MsgDTO transplantingPickUp(Integer workPlanType, String mcKey, String blockName, String withWorkBlockName, String plcName) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(plcName);
        msgCycleOrderDTO.setStation(MsgCreateUtil.replaceSendMsgBlockName(withWorkBlockName));
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setLine(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setRow(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TRANSPLANTING_PICKUP_07);
        msgCycleOrderDTO.setMachineName(MsgCreateUtil.replaceSendMsgBlockName(blockName));
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 切换模式
     *
     *
     * @param station-站台名称, transportType-作业模式
     * @return com.wap.client.dto.MsgChangeStationModeDTO
     * @author CalmLake
     * @date 2019/1/15 10:20
     */
    public MsgChangeStationModeDTO changeMode(String station, String transportType)  {
        WcsMachineEntity machine = DbUtil.getMachineDao().selectOne(new QueryWrapper<WcsMachineEntity>().eq("stationName",station));
        MsgChangeStationModeDTO msgChangeStationModeDTO = new MsgChangeStationModeDTO();
        msgChangeStationModeDTO.setPlcName(machine.getPlcName());
        msgChangeStationModeDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgChangeStationModeDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CHANGE_STATION_MODE);
        msgChangeStationModeDTO.setReSend(MsgConstant.RESEND_SEND);
        msgChangeStationModeDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgChangeStationModeDTO.setStation(station);
        msgChangeStationModeDTO.setMode(transportType);
        msgChangeStationModeDTO.setBcc(BccUtil.getBcc(msgChangeStationModeDTO.getData()));
        return msgChangeStationModeDTO;
    }
}
