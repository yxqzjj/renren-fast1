package io.renren.wap.service.msg;


import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
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
 * 堆垛机消息
 *
 * @Author: CalmLake
 * @Date: 2019/1/14  16:18
 * @Version: V1.0.0
 **/
public class MlMsgService extends MsgService {

    /**
     * 移载卸货
     *
     * @param workPlan          工作计划
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 10:25
     */
    public MsgCycleOrderDTO transplantingTheUnloading(WcsWorkplanEntity workPlan, String blockName, String withWorkBlockName) {
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
        if (workPlan.getType().equals(WorkPlanConstant.TYPE_PUT_IN_STORAGE)) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_PUT_IN_STORAGE_01);
        } else if (workPlan.getType().equals(WorkPlanConstant.TYPE_OUT_PUT_STORAGE)) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03);
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("堆垛机判断订单类型出错，没有解析的订单类型 ：%s", Integer.toString(workPlan.getType())));
        }
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TRANSPLANTING_THE_UNLOADING_08);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(workPlan.getMckey());
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 移载取货
     *
     * @param workPlan          工作计划
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 10:35
     */
    public MsgCycleOrderDTO transplantingPickUp(WcsWorkplanEntity workPlan, String blockName, String withWorkBlockName) {
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
        if (workPlan.getType().equals(WorkPlanConstant.TYPE_PUT_IN_STORAGE)) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_PUT_IN_STORAGE_01);
        } else if (workPlan.getType().equals(WorkPlanConstant.TYPE_OUT_PUT_STORAGE)) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03);
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("堆垛机判断订单类型出错，没有解析的订单类型 ：%s", Integer.toString(workPlan.getType())));
        }
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TRANSPLANTING_PICKUP_07);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(workPlan.getMckey());
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }


    /**
     * 移动向货位
     *
     * @param blockName 数据block名称
     * @param row       排
     * @param line      列
     * @param tier      层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     */
    public MsgCycleOrderDTO move(String mcKey, String blockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_GO_STRAIGHT_02);
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }


    /**
     * 移动向穿梭车
     *
     * @param mcKey     mcKey
     * @param blockName 数据block名称
     * @param station   到达站台名称(目前出入的是数据block名称)
     * @param row       排
     * @param line      列
     * @param tier      层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     */
    public MsgCycleOrderDTO move(String mcKey, String blockName, String station, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_GO_STRAIGHT_02);
        //todo  目前所有哦移动都按照无货移动处理
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 向码头移动
     * 排列层可以为00,00,00  只要站台号按照约定值传入就可以到达
     *
     * @param mcKey     任务标识
     * @param type      工作计划类型
     * @param blockName 数据block名称
     * @param station   到达站台名称(目前出入的是数据block名称)
     * @param dockName  到达码头名称
     * @return com.wap.client.dto.MsgCycleOrderDTO
     */
    public MsgCycleOrderDTO move(String mcKey, Integer type, String blockName, String station, String dockName) {
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
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==type) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_PUT_IN_STORAGE_01);
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==type) {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_OUTPUT_ALL_STORAGE_03);
        } else {
            msgCycleOrderDTO.setCycleType(MsgCycleOrderConstant.CYCLE_TYPE_GO_STRAIGHT_02);
        }
        //todo  目前所有哦移动都按照无货移动处理
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 接车
     *
     * @param workPlanType  工作计划类型
     * @param mcKey         mcKey
     * @param blockName     数据block名称
     * @param withBlockName 一起交互工作的数据block名称
     * @param row           排
     * @param line          列
     * @param tier          层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 11:57
     */
    public MsgCycleOrderDTO getCar(Integer workPlanType, String mcKey, String blockName, String withBlockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_PICK_UP_THE_CAR_05);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }


    /**
     * 卸车
     *
     * @param workPlanType  工作计划类型
     * @param mcKey         mcKey
     * @param blockName     数据block名称
     * @param withBlockName 一起交互工作的数据block名称
     * @param row           排
     * @param line          列
     * @param tier          层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 12:11
     */
    public MsgCycleOrderDTO offCar(Integer workPlanType, String mcKey, String blockName, String withBlockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_UNCAR_06);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }


}
