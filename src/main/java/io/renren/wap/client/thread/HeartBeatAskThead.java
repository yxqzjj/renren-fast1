package io.renren.wap.client.thread;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgHeartBeatSignalAskDTO;
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
 * 心跳询问线程
 *
 * @Author: CalmLake
 * @Date: 2019/5/16  10:32
 * @Version: V1.0.0
 **/
public class HeartBeatAskThead implements Runnable {

    private List<String> plcNameList = new ArrayList<>();

    public HeartBeatAskThead(Enumeration<String> plcNames) {
        while (plcNames.hasMoreElements()) {
            String plcName = plcNames.nextElement();
            plcNameList.add(plcName);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String wcsNo = "1";
                String heartBeat = "1";
                for (String plcName : plcNameList) {
                    try {
                        WcsPlcconfigEntity plcConfig= DbUtil.getPlcConfigDao().selectOne(new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
                        if (PlcConfigConstant.STATUS_CONNECTED.equals(plcConfig.getStatus())){
                            if (!plcName.contains(MachineConstant.TYPE_PLC_NAME_BL)){
                                MsgHeartBeatSignalAskDTO msgHeartBeatSignalAskDTO = new MsgHeartBeatSignalAskDTO();
                                msgHeartBeatSignalAskDTO.setMessageNumber(CreateSequenceNumberSingleton.getInstance().getMessageNumber());
                                msgHeartBeatSignalAskDTO.setCommandType(MsgConstant.MSG_COMMAND_TYPE_HEART_BEAT_SIGNAL_ASK);
                                msgHeartBeatSignalAskDTO.setReSend(MsgConstant.RESEND_SEND);
                                msgHeartBeatSignalAskDTO.setSendTime(DateFormatUtil.getStringHHmmss());
                                msgHeartBeatSignalAskDTO.setPlcName(plcName);
                                msgHeartBeatSignalAskDTO.setConsoleNo(plcName);
                                msgHeartBeatSignalAskDTO.setWcsNo(wcsNo);
                                msgHeartBeatSignalAskDTO.setHeartBeat(heartBeat);
                                msgHeartBeatSignalAskDTO.setBcc(BccUtil.getBcc(msgHeartBeatSignalAskDTO.getData()));
                                MsgQueueCache.addSendMsg(msgHeartBeatSignalAskDTO);
                            }
                        }
                    } catch (InterruptedException e) {
                        Log4j2Util.getMsgHeartMachineStatus().info(String.format("心跳消息制作异常，异常信息：%s,", e.getMessage()));
                    }
                }
            } catch (Exception e) {
                Log4j2Util.getMsgHeartMachineStatus().info(String.format("心跳消息制作异常，异常信息：%s,", e.getMessage()));
            } finally {
                SleepUtil.sleep(SystemCache.HEART_BEAT_ASK_TIME);
            }
        }
    }
}
