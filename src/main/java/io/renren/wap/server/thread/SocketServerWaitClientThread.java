package io.renren.wap.server.thread;


import io.renren.wap.customer.WmsXmlQueueCustomer;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.util.Log4j2Util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 服务端等待连接线程
 *
 * @Author: CalmLake
 * @Date: 2018/11/21  10:43
 * @Version: V1.0.0
 **/
public class SocketServerWaitClientThread implements Runnable {
    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 服务地址信息
     */
    private SocketAddress socketAddress;

    public SocketServerWaitClientThread(String serverName, SocketAddress socketAddress) {
        this.serverName = serverName;
        this.socketAddress = socketAddress;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.setSoTimeout(0);
            serverSocket.bind(socketAddress);
            while (ServerConfigCache.ALWAYS_ACCEPT_CLIENT && !Thread.currentThread().isInterrupted()) {
                Socket socket = new Socket();
                socket.setKeepAlive(true);
                socket.setSoTimeout(0);
                socket = serverSocket.accept();
                Integer port = socket.getPort();
                Log4j2Util.getRoot().info(serverName + ",端口:" + port);
                ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT = port;
                XmlQueueCache.initReceiveConcurrentHashMap(port);
                XmlQueueCache.initSendConcurrentHashMap(port);
                //  初始化消息消费者
                WmsXmlQueueCustomer wmsXmlQueueCustomer = new WmsXmlQueueCustomer(port);
                ThreadPoolServiceSingleton.getInstance().getExecutorMachineCustomer().submit(wmsXmlQueueCustomer);
                //  启动服务端线程
                SocketServerReadWriteThread socketServerReadWriteThread = new SocketServerReadWriteThread(serverName, socket);
                ThreadPoolServiceSingleton.getInstance().submitSocketServerReadWrite(socketServerReadWriteThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log4j2Util.getRoot().info(serverName + "服务" + socketAddress.toString() + "关闭");
        }
    }
}
