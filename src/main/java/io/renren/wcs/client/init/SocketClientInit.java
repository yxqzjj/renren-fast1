package io.renren.wcs.client.init;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wcs.cache.ClientInfoCache;
import io.renren.wcs.cache.PlcInfoCache;
import io.renren.wcs.client.init.Thread.AutoConnectClientThread;
import io.renren.wcs.constant.PlcConfigConstant;
import io.renren.wcs.threadPool.ThreadPoolServiceSingleton;
import io.renren.wcs.util.DbUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.List;

/**
 * @author ：zzwy
 * @date ：Created in 2019/12/23 9:29
 * @description：Socket客户端初始化
 * @modified By：
 * @version: 1.0
 */
public class SocketClientInit {
    /**
     * socket初始化程序入口
     */
    public void startSocketClientInit(){
           //1.初始化plc
           initPlcInfo();
           //2.开启客户端连接
           startSocketClientConnection();
    }

    /**
     * 初始化plc
     */
    private void initPlcInfo() {
        List<WcsPlcconfigEntity> plcConfigList = DbUtil.getWcsPlcconfigDao().selectList((Wrapper<WcsPlcconfigEntity>) new Object());
        for (WcsPlcconfigEntity plcConfig : plcConfigList) {
            //1.plc是否启用
            if (!PlcConfigConstant.STATUS_BAN.equals(plcConfig.getStatus())) {
                //2.获取plcName
                String plcName = plcConfig.getName();
                //3.获取SocketAddress
                SocketAddress socketAddress = new InetSocketAddress(plcConfig.getIp(), plcConfig.getPort());
                PlcInfoCache.addPlcSocketAddress(plcName, socketAddress);
            }
        }
    }

    /**
     * 开启客户端连接
     */
    public void startSocketClientConnection(){
        AutoConnectClientThread autoConnectClientThread = new AutoConnectClientThread();
        //自动连接线程
        ThreadPoolServiceSingleton.getInstance().cyclicExecutingConnection(autoConnectClientThread);
    }

    /**
     * socket信息初始化
     */
    private void initSocketClient() {
        Enumeration<String> plcNames = PlcInfoCache.getPlcConcurrentHashMapKeys();
        while (plcNames.hasMoreElements()) {
            String plcName = plcNames.nextElement();
            //plcName 对应 socket
            ClientInfoCache.initConcurrentHashMap(plcName);
        }
    }
}
