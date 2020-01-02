package io.renren.wap.customer.plc;


import io.renren.wap.cache.BlockCache;
import io.renren.wap.cache.BlockQueueCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.dto.*;
import io.renren.wap.service.PlcService;
import io.renren.wap.util.Log4j2Util;

/**
 * wcs接收消息处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  13:12
 * @Version: V1.0.0
 **/
public abstract class AbstractPlcCustomer implements Runnable {
    private String plcName;

    AbstractPlcCustomer(String plcName) {
        this.plcName = plcName;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MsgDTO msgDTO = MsgQueueCache.getReceiveMsg(plcName);
                if (msgDTO instanceof MsgHeartBeatSignalAckDTO) {
                    doMsgHeartBeatSignalAckDTO((MsgHeartBeatSignalAckDTO) msgDTO);
                } else if (msgDTO instanceof MsgChangeStationModeAckDTO) {
                    doMsgChangeStationModeAckDTO((MsgChangeStationModeAckDTO) msgDTO);
                } else if (msgDTO instanceof MsgCycleOrderAckDTO) {
                    String blockName = ((MsgCycleOrderAckDTO) msgDTO).getMachineName();
                    String cycleCommand = ((MsgCycleOrderAckDTO) msgDTO).getCycleCommand();
                    if (checkCycleCommand(blockName, cycleCommand)) {
                        BlockQueueCache.addMsg(blockName, msgDTO);
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s 检验指令不一致，23指令：%s,03指令：%s", blockName, cycleCommand, BlockCache.getString(blockName)));
                    }
                } else if (msgDTO instanceof MsgCycleOrderFinishReportDTO) {
                    String blockName = ((MsgCycleOrderFinishReportDTO) msgDTO).getMachineName();
                    BlockQueueCache.addMsg(blockName, msgDTO);
                } else if (msgDTO instanceof MsgMachineryStatusOrderAckDTO) {
                    doMsgMachineryStatusOrderAckDTO((MsgMachineryStatusOrderAckDTO) msgDTO);
                } else if (msgDTO instanceof MsgDeleteDataAckDTO) {
                    doMsgDeleteDataAckDTO((MsgDeleteDataAckDTO) msgDTO);
                } else if (msgDTO instanceof MsgConveyorLineDataReportDTO) {
                    doMsgConveyorLineDataReportDTO((MsgConveyorLineDataReportDTO) msgDTO);
                } else {
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s 无效的消息类型：程序未解析", plcName));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log4j2Util.getMsgCustomerLogger().error(String.format("%s 消费者出现异常，异常信息：%s", plcName, e.getMessage()));
            }
        }
    }

    /**
     * 检验动作是否一致
     *
     * @param blockName    数据block名称
     * @param cycleCommand 设备回复消息中cycle指令
     * @return boolean
     * @author CalmLake
     * @date 2019/3/6 16:38
     */
    private boolean checkCycleCommand(String blockName, String cycleCommand) {
        String cycleCommand_ = BlockCache.getString(blockName);
        if (cycleCommand_ == null) {
            return true;
        }
        return cycleCommand_.equals(cycleCommand);
    }

    /**
     * 站台模式切换应答处理
     *
     * @param msgChangeStationModeAckDTO 站台模式切换应答消息
     * @throws InterruptedException 队列异常
     * @author CalmLake
     * @date 2019/1/23 14:41
     */
    abstract void doMsgChangeStationModeAckDTO(MsgChangeStationModeAckDTO msgChangeStationModeAckDTO) throws InterruptedException;

    /**
     * 设备状态应答处理
     *
     * @param msgMachineryStatusOrderAckDTO 设备状态应答消息
     * @author CalmLake
     * @date 2019/1/16 14:21
     */
    abstract void doMsgMachineryStatusOrderAckDTO(MsgMachineryStatusOrderAckDTO msgMachineryStatusOrderAckDTO);

    /**
     * 数据删除应答处理
     *
     * @param msgDeleteDataAckDTO 数据删除应答消息
     * @author CalmLake
     * @date 2019/1/16 14:21
     */
    abstract void doMsgDeleteDataAckDTO(MsgDeleteDataAckDTO msgDeleteDataAckDTO);

    /**
     * 输送线Data报告处理
     *
     * @param msgConveyorLineDataReportDTO 输送线Data报告消息
     * @throws InterruptedException 线程中断异常
     * @author CalmLake
     * @date 2019/1/16 14:21
     */
    abstract void doMsgConveyorLineDataReportDTO(MsgConveyorLineDataReportDTO msgConveyorLineDataReportDTO) throws InterruptedException;

    /**
     * 心跳信号应答处理
     *
     * @param msgHeartBeatSignalAckDTO 心跳信号应答消息
     * @author CalmLake
     * @date 2019/1/16 14:21
     */
    private void doMsgHeartBeatSignalAckDTO(MsgHeartBeatSignalAckDTO msgHeartBeatSignalAckDTO) {
        String plcName = msgHeartBeatSignalAckDTO.getPlcName();
        PlcService plcService = new PlcService();
        plcService.updateHeartTime(plcName);
    }
}
