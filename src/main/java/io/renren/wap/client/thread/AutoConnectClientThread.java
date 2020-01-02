package io.renren.wap.client.thread;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.ClientInfoCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.cache.PlcInfoCache;
import io.renren.wap.client.connection.ConnectionPlc;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.client.util.MsgCreateUtil;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.PlcConfigConstant;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Enumeration;

/**
 * 自动连接线程
 *
 * @Author: CalmLake
 * @Date: 2018/11/18  22:25
 * @Version: V1.0.0
 **/
public class AutoConnectClientThread implements Runnable {

    @Override
    public void run() {
        try {
            autoConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 自动重连
     *
     * @author CalmLake
     * @date 2018/11/19 22:32
     * @Param []
     */
    private void autoConnect() {
        Enumeration<String> plcNameEnumeration = PlcInfoCache.getPlcConcurrentHashMapKeys();
        while (plcNameEnumeration.hasMoreElements()) {
            String plcName = plcNameEnumeration.nextElement();
            SocketAddress socketAddress = PlcInfoCache.getSocketAddress(plcName);
            Socket oldSocket = ClientInfoCache.getSocket(plcName);
            if (SystemCache.HEART_BEAT_ASK_OUT_CLOSE_FLAG && !plcName.contains(MachineConstant.TYPE_PLC_NAME_BL)) {
                WcsPlcconfigEntity plcConfig = DbUtil.getPlcConfigDao().selectOne(new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
                long oldTime = plcConfig.getHeartbeatTime().getTime();
                long nowDate = System.currentTimeMillis();
                if (((nowDate - oldTime) / 1000) > SystemCache.HEART_BEAT_ASK_OUT_TIME && PlcConfigConstant.STATUS_CONNECTED.equals(plcConfig.getStatus())) {
                    try {
                        oldSocket.close();
                        Log4j2Util.getMsgQueueLogger().error(String.format("plcName：%s，2分钟未收到心跳信号，关闭旧连接！", plcName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (null == oldSocket || !oldSocket.isConnected() || oldSocket.isClosed()) {
                Socket newSocket = new Socket();
                ConnectionPlc connectionPlc = new ConnectionPlc(newSocket, socketAddress);
                boolean result = connectionPlc.createConnection();
                Log4j2Util.getMsgQueueLogger().error(plcName + "连接结果:" + result);
                if (result) {
                    SocketClientReadWriteThread socketClientReadWriteThread = new SocketClientReadWriteThread(newSocket, plcName);
                    ThreadPoolServiceSingleton.getInstance().submitReadWrite(socketClientReadWriteThread);
                    ClientInfoCache.replace(plcName, oldSocket, newSocket);
                    MsgDTO msgStartMachineryDTO = MsgCreateUtil.createMsgStartMachineryDTO(plcName);
                    try {
                        MsgQueueCache.addSendMsg(msgStartMachineryDTO);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log4j2Util.getMsgQueueLogger().error(String.format("plcName：%s，设备启动消息发送失败！", plcName));
                    }
                }
            }
        }
    }
}
