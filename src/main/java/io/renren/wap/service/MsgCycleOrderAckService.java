package io.renren.wap.service;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * wcs 处理cycle指示应答
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  16:20
 * @Version: V1.0.0
 **/
public class MsgCycleOrderAckService {

    /**
     * cycle指示应答解析
     *
     * @param blockName             数据block名称
     * @param msgMcKey              消息中的mcKey
     * @param ackType               应答区分
     * @param exceptionType         异常区分
     * @param blockMcKey            设备运行状态中的mcKey
     * @param blockAppointmentMcKey 设备运行状态中的blockAppointmentMcKey
     * @param command               设备运行状态中的命令类型
     * @param object                操作修改该设备block表的dao对象
     * @author CalmLake
     * @date 2019/1/16 16:24
     */
    public static void resolveMsg(String blockName, String msgMcKey, String ackType, String exceptionType, String blockMcKey, String blockAppointmentMcKey, String command, Object object, boolean loadStatus) {
        if (StringUtils.isNotEmpty(msgMcKey)) {
//            if (MsgCycleOrderConstant.ACK_TYPE_NORMAL.equals(ackType)) {
//                if (MsgCycleOrderConstant.EXCEPTION_TYPE_NORMAL.equals(exceptionType)) {
            if (msgMcKey.equals(blockMcKey) || msgMcKey.equals(blockAppointmentMcKey)) {
                if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER.equals(command)) {
                    int resultUpdate;
                    if (SystemCache.MSG_CYCLE_ORDER_ACK_UPDATE_LOAD) {
                        resultUpdate = BlockService.updateBlockCommandAndLoadStatus(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_ACK, loadStatus, object, blockName);
                    } else {
                        BlockService blockService = new BlockService();
                        resultUpdate = blockService.updateCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_ACK, object, blockName);
                    }
                    Log4j2Util.getMsgCustomerLogger().info(String.format("修改载荷状态开关：%b,%s,msgMcKey: %s,23已回复 状态修改结果：%d！", SystemCache.MSG_CYCLE_ORDER_ACK_UPDATE_LOAD, blockName, msgMcKey, resultUpdate));
                } else {
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s,接收23消息,命令校验失败，当前命令值为：%s！", blockName, command));
                }
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,校验失败，当前消息中任务与设备正在执行任务不一致！msgMcKey：%s，blockMcKey：%s，blockAppointmentMcKey：%s", blockName, msgMcKey, blockMcKey, blockAppointmentMcKey));
            }
//                } else {
//                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s,设备出现异常，exceptionType：%s", blockName, exceptionType));
//                }
//            } else {
//                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,设备应答异常，ackType：%s", blockName, ackType));
//            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s,校验失败：消息中的mcKey为空", blockName));
        }
    }
}
