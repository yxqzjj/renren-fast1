package io.renren.wap.service.msg;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.util.DateFormatUtil;

/**
 * 穿梭车消息
 *
 * @Author: CalmLake
 * @Date: 2019/1/15  13:21
 * @Version: V1.0.0
 **/
public class ScMsgService extends MsgService {

    /**
     * 载货上车
     *
     * @param mcKey             mcKey
     * @param workPlanType      工作计划类型
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @param row               排
     * @param line              列
     * @param tier              层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:30
     */
    public MsgCycleOrderDTO getCarLoad(String mcKey, Integer workPlanType, String blockName, String withWorkBlockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_GO_ON_CAR_12);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 载货下车
     *
     * @param mcKey             mcKey
     * @param workPlanType      工作计划类型
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @param row               排
     * @param line              列
     * @param tier              层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:34
     */
    public MsgCycleOrderDTO offCarLoad(String mcKey, Integer workPlanType, String blockName, String withWorkBlockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_GO_OFF_CAR_13);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 空车下车
     *
     * @param mcKey             mcKey
     * @param workPlanType      工作计划类型
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @param row               排
     * @param line              列
     * @param tier              层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:36
     */
    public MsgCycleOrderDTO offCar(String mcKey, Integer workPlanType, String blockName, String withWorkBlockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_GO_OFF_CAR_EMPTY_10);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    public MsgCycleOrderDTO offCar(String mcKey, Integer workPlanType, String blockName, String withWorkBlockName, String row, String line, String tier, String cycleCommand) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(cycleCommand);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 空车上车
     *
     * @param mcKey             mcKey
     * @param workPlanType      工作计划类型
     * @param blockName         数据block名称
     * @param withWorkBlockName 一起交互工作的数据block名称
     * @param row               排
     * @param line              列
     * @param tier              层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:37
     */
    public MsgCycleOrderDTO getCar(String mcKey, Integer workPlanType, String blockName, String withWorkBlockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_GO_ON_CAR_EMPTY_09);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    public MsgCycleOrderDTO getCar(String mcKey, Integer workPlanType, String blockName, String withWorkBlockName, String row, String line, String tier, String cycleCommand) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(withWorkBlockName);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(cycleCommand);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 取货
     *
     * @param mcKey        mcKey
     * @param workPlanType 工作计划类型
     * @param blockName    数据block名称
     * @param location     货位
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:39
     */
    public MsgCycleOrderDTO pick(String mcKey, Integer workPlanType, String blockName, String location) {
        String line = getLine(location);
        String tier = getTier(location);
        String row = getRow(location);
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_COMMAND_PICK_UP_02);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 卸货
     *
     * @param mcKey        mcKey
     * @param workPlanType 工作计划类型
     * @param blockName    数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:41
     */
    public MsgCycleOrderDTO loadOff(String mcKey, Integer workPlanType, String blockName, String row, String line, String tier, String withWorkBlockName) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_COMMAND_UNLOAD_03);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 充电开始
     *
     * @param mcKey        mcKey
     * @param workPlanType 工作计划类型
     * @param blockName    数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:44
     */
    public MsgCycleOrderDTO startCharge(String mcKey, Integer workPlanType, String blockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
            msgCycleOrderDTO.setCycleCommand("11");
        } else {
            msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_CHARGE_START_14);
        }
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 充电完成
     *
     * @param mcKey        mcKey
     * @param workPlanType 工作计划类型
     * @param blockName    数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:44
     */
    public MsgCycleOrderDTO finishCharge(String mcKey, Integer workPlanType, String blockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        if (CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
            msgCycleOrderDTO.setCycleCommand("12");
        } else {
            msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_CHARGE_FINISH_15);
        }
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 盘点
     *
     * @param mcKey        mcKey
     * @param workPlanType 工作计划类型
     * @param blockName    数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 15:46
     */
    public MsgCycleOrderDTO takeStock(String mcKey, Integer workPlanType, String blockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TAKE_STOCK_16);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 理货
     *
     * @param mcKey        mcKey
     * @param workPlanType 工作计划类型
     * @param blockName    数据block名称
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 16:15
     */
    public MsgCycleOrderDTO tally(String mcKey, Integer workPlanType, String blockName, String row, String line, String tier) {
        MsgCycleOrderDTO msgCycleOrderDTO = new MsgCycleOrderDTO();
        msgCycleOrderDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
        msgCycleOrderDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER);
        msgCycleOrderDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderDTO.setPlcName(blockName);
        msgCycleOrderDTO.setDock(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setStation(MsgCycleOrderConstant.DEFAULT_ZERO);
        msgCycleOrderDTO.setTier(tier);
        msgCycleOrderDTO.setLine(line);
        msgCycleOrderDTO.setRow(row);
        msgCycleOrderDTO.setWidth(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setHeight(MsgCycleOrderConstant.DEFAULT_HEIGHT_WIDTH);
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_TALLY_17);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }

    /**
     * 直行
     *
     * @param mcKey        mcKey
     * @param blockName    数据block名称
     * @param location     位置信息
     * @param workPlanType 工作计划类型
     * @return com.wap.client.dto.MsgCycleOrderDTO
     * @author CalmLake
     * @date 2019/1/15 16:15
     */
    public MsgCycleOrderDTO move(String mcKey, String blockName, String location, Integer workPlanType) {
        String row = location.substring(1, 3);
        String line = location.substring(4, 6);
        String tier = location.substring(7, 9);
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
        msgCycleOrderDTO.setCycleType(getCycleType(workPlanType));
        msgCycleOrderDTO.setCycleCommand(MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04);
        msgCycleOrderDTO.setMachineName(blockName);
        msgCycleOrderDTO.setMcKey(mcKey);
        msgCycleOrderDTO.setBcc(BccUtil.getBcc(msgCycleOrderDTO.getData()));
        return msgCycleOrderDTO;
    }
}
