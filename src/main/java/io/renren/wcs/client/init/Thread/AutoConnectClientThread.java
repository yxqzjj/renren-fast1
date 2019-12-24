package io.renren.wcs.client.init.Thread;

import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wcs.cache.ClientInfoCache;
import io.renren.wcs.cache.PlcInfoCache;
import io.renren.wcs.client.cache.MsgQueueCache;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.connection.ConnectionPlc;
import io.renren.wcs.constant.PlcConfigConstant;
import io.renren.wcs.systemConfig.SystemConfigInfo;
import io.renren.wcs.threadPool.ThreadPoolServiceSingleton;
import io.renren.wcs.util.DbUtil;
import io.renren.wcs.util.Log4j2Util;
import io.renren.wcs.util.MsgCreateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Enumeration;

/**
 * @author ：zzwy
 * @date ：Created in 2019/12/23 9:46
 * @description：客户端自动连接线程
 * @modified By：
 * @version: 1.0
 */
public class AutoConnectClientThread implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void run() {
        autoConnection();
    }

    /**
     * 自动连接
     */
    public void autoConnection(){
        //1.plcName的集合
        Enumeration<String> plcNameEnumeration = PlcInfoCache.getPlcConcurrentHashMapKeys();
        while (plcNameEnumeration.hasMoreElements()) {
            String plcName = plcNameEnumeration.nextElement();
            //2.获取SocketAddress从缓存重
            SocketAddress socketAddress = PlcInfoCache.getSocketAddress(plcName);
            //3.获取Socket
            Socket oldSocket = ClientInfoCache.getSocket(plcName);
            //4.判断心跳是否关闭
            if (SystemConfigInfo.getSystemConfigInfoSingleton().isHeartBeatAskOutCloseFlag()) {
                WcsPlcconfigEntity plcConfig = DbUtil.getWcsPlcconfigDao().selectById(1l);
                long oldTime = plcConfig.getHeartbeatTime().getTime();
                long nowDate = System.currentTimeMillis();
                if (((nowDate - oldTime) / 1000) > SystemConfigInfo.getSystemConfigInfoSingleton().getHeartBeatAskOutTime() && PlcConfigConstant.STATUS_CONNECTED.equals(plcConfig.getStatus())) {
                    try {
                        oldSocket.close();
                        Log4j2Util.getMsgQueueLogger().error(String.format("plcName：%s，2分钟未收到心跳信号，关闭旧连接！", plcName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //5.判断连接
            if (null == oldSocket || !oldSocket.isConnected() || oldSocket.isClosed()) {
                Socket newSocket = new Socket();
                ConnectionPlc connectionPlc = new ConnectionPlc(newSocket, socketAddress);
                boolean result = connectionPlc.createConnection();
                logger.info(plcName+"连接结果"+result);
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
