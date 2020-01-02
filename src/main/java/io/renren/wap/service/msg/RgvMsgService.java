package io.renren.wap.service.msg;


import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.Log4j2Util;

/**
 * RGV消息制作
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  10:17
 * @Version: V1.0.0
 **/
public class RgvMsgService extends MsgService {
    /**
     * 移载卸货
     *
     * @param workPlanType      工作计划类型
     * @param mcKey             mcKey
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 10:25
     */
    public MsgCycleOrderDTO transplantingTheUnloading(Integer workPlanType, String mcKey, String blockName, String withWorkBlockName) {
        WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(machine.getDockName());
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setLine(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setRow(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_PUT_IN_STORAGE_01);
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03);
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("堆垛机判断订单类型出错，没有解析的订单类型 ：%s", Integer.toString(workPlanType)));
        }
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TRANSPLANTING_THE_UNLOADING_08);
        msgCycleOrderDTO.setMachineName(blockName);
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
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 10:35
     */
    public MsgCycleOrderDTO transplantingPickUp(Integer workPlanType, String mcKey, String blockName, String withWorkBlockName) {
        WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setDock(machine.getDockName());
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setLine(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setRow(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_PUT_IN_STORAGE_01);
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03);
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("堆垛机判断订单类型出错，没有解析的订单类型 ：%s", Integer.toString(workPlanType)));
        }
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TRANSPLANTING_PICKUP_07);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 向码头移动
     * 排列层可以为00,00,00  只要站台号按照约定值传入就可以到达
     *
     * @param mcKey     mcKey
     * @param blockName 数据block名称
     * @param station   到达站台名称(目前出入的是数据block名称)
     * @param dockName  到达码头名称
     * @return com.wap.client.dto.MsgCycleOrderDTO
     */
    public MsgCycleOrderDTO move(String mcKey, String blockName, String station, String dockName) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setDock(dockName);
        msgCycleOrderDTO.setStation(station);
        msgCycleOrderDTO.setTier(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setLine(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setRow(MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_GO_STRAIGHT_02);
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }
}
