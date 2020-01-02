package io.renren.wap.client.thread;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgMachineryStatusOrderAskDTO;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.client.util.BccUtil;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.PlcConfigConstant;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 设备状态询问线程
 *
 * @Author: CalmLake
 * @Date: 2019/5/16  10:33
 * @Version: V1.0.0
 **/
public class MachineStatusAskThread implements Runnable {
    private List<String> plcNameList = new ArrayList<>();

    public MachineStatusAskThread(Enumeration<String> plcNames) {
        while (plcNames.hasMoreElements()) {
            String plcName = plcNames.nextElement();
            plcNameList.add(plcName);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (String plcName : plcNameList) {
                    if (!plcName.contains(MachineConstant.TYPE_PLC_NAME_BL)&&!plcName.contains(MachineConstant.TYPE_AL)) {
                        try {
                            WcsPlcconfigEntity plcConfig = DbUtil.getPlcConfigDao().selectOne(new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
                            if (PlcConfigConstant.STATUS_CONNECTED.equals(plcConfig.getStatus())) {
                                MsgMachineryStatusOrderAskDTO msgMachineryStatusOrderAskDTO = new MsgMachineryStatusOrderAskDTO();
                                msgMachineryStatusOrderAskDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
                                msgMachineryStatusOrderAskDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_MACHINERY_STATUS_ORDER_ASK);
                                msgMachineryStatusOrderAskDTO.setReSend(MsgConstant.RESEND_SEND);
                                msgMachineryStatusOrderAskDTO.setSendTime(DateFormatUtil.getStringHHmmss());
                                msgMachineryStatusOrderAskDTO.setPlcName(plcName);
                                msgMachineryStatusOrderAskDTO.setMachineName(plcName);
                                msgMachineryStatusOrderAskDTO.setStatus(MsgMachineryStatusOrderAskDTO.STATUS_REQUEST_STATUS);
                                msgMachineryStatusOrderAskDTO.setBcc(BccUtil.getBcc(msgMachineryStatusOrderAskDTO.getData()));
                                MsgQueueCache.addSendMsg(msgMachineryStatusOrderAskDTO);
                            }
                        } catch (InterruptedException e) {
                            Log4j2Util.getMsgHeartMachineStatus().info(String.format("状态消息制作异常，异常信息：%s,", e.getMessage()));
                        }
                    }
                }
            } catch (Exception e) {
                Log4j2Util.getMsgHeartMachineStatus().info(String.format("状态消息制作异常，异常信息：%s,", e.getMessage()));
            } finally {
                SleepUtil.sleep(SystemCache.MACHINE_STATUS_ASK_TIME);
            }
        }
    }
}
