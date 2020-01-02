package io.renren.wap.init;


import io.renren.wap.server.thread.SocketServerWaitClientThread;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;

import java.net.SocketAddress;

/**
 * socket服务初始化
 *
 * @Author: CalmLake
 * @Date: 2018/11/22  11:47
 * @Version: V1.0.0
 **/
public class SocketServerInit {
    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 地址信息
     */
    private SocketAddress socketAddress;

    public SocketServerInit(String serverName, SocketAddress socketAddress) {
        this.serverName = serverName;
        this.socketAddress = socketAddress;
    }

    /**
     * 开启socketServer服务
     *
     * @author CalmLake
     * @date 2018/11/22 15:39
     * @Param []
     */
    public void startSocketServerService() {
        SocketServerWaitClientThread socketServerWaitClientThread = new SocketServerWaitClientThread(serverName, socketAddress);
        ThreadPoolServiceSingleton.getInstance().startSocketServer(socketServerWaitClientThread);
    }
}
