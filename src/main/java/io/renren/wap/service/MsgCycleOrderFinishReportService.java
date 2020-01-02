package io.renren.wap.service;


import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * wcs 处理cycle完成报告
 *
 * @Author: CalmLake
 * @Date: 2019/1/18  12:13
 * @Version: V1.0.0
 **/
public class MsgCycleOrderFinishReportService {

    /**
     * cycle指示是否成功完成
     *
     * @param finishCode 完成代码
     * @param finishType 完成区分
     * @param blockName  数据block名称
     * @return boolean
     * @author CalmLake
     * @date 2019/1/18 16:18
     */
    public static boolean isFinishedSuccess(String finishCode, String finishType, String blockName) {
        if (MsgCycleOrderConstant.FINISH_TYPE_NORMAL.equals(finishType)) {
            if (MsgCycleOrderConstant.FINISH_CODE_FINISHED.equals(finishCode)) {
                return true;
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,任务完成完成代码校验失败：finishCode:%s", blockName, finishCode));
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s,任务完成完成区分校验失败：finishType:%s", blockName, finishType));
        }
        return false;
    }

    /**
     * 校验接收数据
     *
     * @param msgMcKey              消息中mcKey
     * @param blockMcKey            设备block表中的mcKey
     * @param blockAppointmentMcKey 设备block表中的AppointmentMcKey
     * @param blockName             数据block名称
     * @return boolean
     * @author CalmLake
     * @date 2019/1/18 12:16
     */
    public static boolean checkData(String msgMcKey, String blockMcKey, String blockAppointmentMcKey, String blockName) {
        if (StringUtils.isNotEmpty(msgMcKey)) {
            if (msgMcKey.equals(blockMcKey) || msgMcKey.equals(blockAppointmentMcKey)) {
                return true;
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,校验失败，当前消息中任务与设备正在执行任务不一致！msgMcKey：%s，blockMcKey：%s，blockAppointmentMcKey：%s", blockName, msgMcKey, blockMcKey, blockAppointmentMcKey));
                return false;
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s,校验失败：消息中的mcKey为空", blockName));
        }
        return false;
    }

}
