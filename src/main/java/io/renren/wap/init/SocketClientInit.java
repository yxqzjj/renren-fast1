package io.renren.wap.init;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.cache.ClientInfoCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.cache.PlcInfoCache;
import io.renren.wap.client.thread.AutoConnectClientThread;
import io.renren.wap.client.thread.HeartBeatAskThead;
import io.renren.wap.client.thread.MachineStatusAskThread;
import io.renren.wap.entity.constant.PlcConfigConstant;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.util.DbUtil;
import org.apache.logging.log4j.LogManager;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.List;

/**
 * socket客户端初始化
 *
 * @Author: CalmLake
 * @Date: 2018/11/22  15:43
 * @Version: V1.0.0
 **/
class SocketClientInit {
    /**
     * socket client 初始化入口
     *
     * @author CalmLake
     * @date 2018/11/22 15:54
     * @Param []
     */
    void startSocketClientInit() {
        initPlcInfo();
        initMsgQueue();
        initSocketClient();
        initHeartBeat();
        initMachineStatus();
        startSocketClientConnection();
    }

    /**
     * 初始化plc SocketAddress 信息
     *
     * @author CalmLake
     * @date 2019/6/21 15:53
     */
    private void initPlcInfo() {
        List<WcsPlcconfigEntity> plcConfigList = DbUtil.getPlcConfigDao().selectList(new QueryWrapper<WcsPlcconfigEntity>());
        for (WcsPlcconfigEntity plcConfig : plcConfigList) {
            //  plc是否启用
            if (!PlcConfigConstant.STATUS_BAN.equals(plcConfig.getStatus())) {
                String plcName = plcConfig.getName();
                SocketAddress socketAddress = new InetSocketAddress(plcConfig.getIp(), plcConfig.getPort());
                PlcInfoCache.addPlcSocketAddress(plcName, socketAddress);
            }
        }
    }

    /**
     * 开启socket client 自动连接线程
     *
     * @author CalmLake
     * @date 2018/11/22 16:10
     */
    private void startSocketClientConnection() {
        AutoConnectClientThread autoConnectClientThread = new AutoConnectClientThread();
        ThreadPoolServiceSingleton.getInstance().cyclicExecutingConnection(autoConnectClientThread);
    }

    /**
     * 初始化socket client连接信息
     *
     * @author CalmLake
     * @date 2018/11/22 16:08
     * @Param []
     */
    private void initSocketClient() {
        Enumeration<String> plcNames = PlcInfoCache.getPlcConcurrentHashMapKeys();
        while (plcNames.hasMoreElements()) {
            String plcName = plcNames.nextElement();
            ClientInfoCache.initConcurrentHashMap(plcName);
        }
    }

    /**
     * 初始化收发消息队列
     *
     * @author CalmLake
     * @date 2018/11/22 15:54
     */
    private void initMsgQueue() {
        Enumeration<String> plcNames = PlcInfoCache.getPlcConcurrentHashMapKeys();
        while (plcNames.hasMoreElements()) {
            String plcName = plcNames.nextElement();
            MsgQueueCache.initConcurrentHashMap(plcName);
        }
    }

    private void initHeartBeat() {
        if (SystemCache.HEART_BEAT_ASK_SWITCH) {
            Enumeration<String> plcNames = PlcInfoCache.getPlcConcurrentHashMapKeys();
            HeartBeatAskThead heartBeatAskThead = new HeartBeatAskThead(plcNames);
            ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(heartBeatAskThead).start();
            LogManager.getLogger().info(" plc  心跳问询开启！");
        } else {
            LogManager.getLogger().info(" plc  心跳问询关闭！");
        }
    }

    private void initMachineStatus() {
        if (SystemCache.MACHINE_STATUS_ASK_SWITCH) {
            Enumeration<String> plcNames = PlcInfoCache.getPlcConcurrentHashMapKeys();
            MachineStatusAskThread machineStatusAskThread = new MachineStatusAskThread(plcNames);
            ThreadPoolServiceSingleton.getInstance().getDefaultThreadFactory().newThread(machineStatusAskThread).start();
            LogManager.getLogger().info(" plc  设备状态询问开启！");
        } else {
            LogManager.getLogger().info(" plc  设备状态问询关闭！");
        }
    }
}
