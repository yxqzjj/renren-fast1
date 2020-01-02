package io.renren.wap.service;


import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportAckDTO;
import io.renren.wap.client.dto.condition.MsgCycleOrderFinishReportAckConditionDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.client.util.MsgCreateUtil;
import io.renren.wap.factory.BlockDaoFactory;
import io.renren.wap.factory.BlockDaoImpl;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.Log4j2Util;

/**
 * cycle完成应答
 *
 * @Author: CalmLake
 * @Date: 2019/1/18  16:14
 * @Version: V1.0.0
 **/
public class MsgCycleOrderFinishReportAckService {

    /**
     * cycle完成应答 回复及block状态修改
     *
     * @param msgCycleOrderFinishReportAckConditionDTO 05消息制作条件
     * @author CalmLake
     * @date 2019/1/23 16:44
     */
    public static void replay05AndUpdateBlock(MsgCycleOrderFinishReportAckConditionDTO msgCycleOrderFinishReportAckConditionDTO) throws InterruptedException {
        String mcKey = msgCycleOrderFinishReportAckConditionDTO.getMsgMcKey();
        String blockName = msgCycleOrderFinishReportAckConditionDTO.getBlockName();
        String cycleCommand = msgCycleOrderFinishReportAckConditionDTO.getCycleCommand();
        sendMsgCycleOrderFinishReportAck(msgCycleOrderFinishReportAckConditionDTO);
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
        BlockDao blockDao = blockDaoFactory.getBlockDao(machine);
        int num = blockDao.updateCommandByPrimaryKey(blockName, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，mcKey：%s，cycleCommand：%S 修改block中Command：05 结果：%d", blockName, mcKey, cycleCommand, num));
    }

    /**
     * cycle完成应答 回复及block状态修改
     *
     * @param msgCycleOrderFinishReportAckConditionDTO 05消息制作条件
     * @author CalmLake
     * @date 2019/1/23 16:44
     */
    public static void replay05(MsgCycleOrderFinishReportAckConditionDTO msgCycleOrderFinishReportAckConditionDTO) throws InterruptedException {
        sendMsgCycleOrderFinishReportAck(msgCycleOrderFinishReportAckConditionDTO);
    }



    /**
     * cycle完成应答制作放入消息发送队列
     *
     * @param msgCycleOrderFinishReportAckConditionDTO 05消息制作条件
     * @author CalmLake
     * @date 2019/1/18 16:16
     */
    public static void sendMsgCycleOrderFinishReportAck(MsgCycleOrderFinishReportAckConditionDTO msgCycleOrderFinishReportAckConditionDTO) throws InterruptedException {
        String mcKey = msgCycleOrderFinishReportAckConditionDTO.getMsgMcKey();
        String plcName = msgCycleOrderFinishReportAckConditionDTO.getPlcName();
        String blockName = msgCycleOrderFinishReportAckConditionDTO.getBlockName();
        String cycleCommand = msgCycleOrderFinishReportAckConditionDTO.getCycleCommand();
        blockName = MsgCreateUtil.replaceSendMsgBlockName(blockName);
        MsgCycleOrderFinishReportAckDTO msgCycleOrderFinishReportAckDTO = new MsgCycleOrderFinishReportAckDTO();
        msgCycleOrderFinishReportAckDTO.setAckType(MsgCycleOrderConstant.ACK_TYPE_NORMAL);
        msgCycleOrderFinishReportAckDTO.setCycleCommand(cycleCommand);
        msgCycleOrderFinishReportAckDTO.setMcKey(mcKey);
        msgCycleOrderFinishReportAckDTO.setBlockName(blockName);
        msgCycleOrderFinishReportAckDTO.setPlcName(plcName);
        msgCycleOrderFinishReportAckDTO.setBcc(BccUtil.getBcc(mcKey + blockName + MsgCycleOrderConstant.ACK_TYPE_NORMAL));
        msgCycleOrderFinishReportAckDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK);
        msgCycleOrderFinishReportAckDTO.setSendTime(DateFormatUtil.getStringHHmmss());
        msgCycleOrderFinishReportAckDTO.setReSend(MsgConstant.RESEND_SEND);
        msgCycleOrderFinishReportAckDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber().trim());
        MsgQueueCache.addSendMsg(msgCycleOrderFinishReportAckDTO);
    }
}
