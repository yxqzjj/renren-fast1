package io.renren.wap.server.thread;


import io.renren.wap.constant.ExecutorConstant;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.server.xml.util.XStreamUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 服务端读写线程
 *
 * @Author: CalmLake
 * @Date: 2018/11/25  18:43
 * @Version: V1.0.0
 **/
public class SocketServerReadWriteThread implements Runnable {
    /**
     * 读写线程
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 当前连接信息
     */
    private Socket socket;

    SocketServerReadWriteThread(String serverName, Socket socket) {
        this.serverName = serverName;
        this.socket = socket;
    }

    @Override
    public void run() {
        int port = socket.getPort();
        SocketServerReadCallable socketServerReadCallable = new SocketServerReadCallable();
        SocketServerWriteCallable socketServerWriteCallable = new SocketServerWriteCallable();
        Future<Boolean> booleanFutureRead = executorService.submit(socketServerReadCallable);
        Future<Boolean> booleanFutureWrite = executorService.submit(socketServerWriteCallable);
        Log4j2Util.getRoot().info(serverName + "," + port + ",socket 读写线程开启");
        try {
            if (!booleanFutureRead.get() || !booleanFutureWrite.get()) {
                booleanFutureRead.cancel(ExecutorConstant.FUTURE_CANCEL_FLAG);
                booleanFutureWrite.cancel(ExecutorConstant.FUTURE_CANCEL_FLAG);
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            XmlQueueCache.removeTheKey(XmlQueueConstant.QUEUE_TYPE_SEND, port);
            XmlQueueCache.removeTheKey(XmlQueueConstant.QUEUE_TYPE_RECEIVE, port);
        }
        Log4j2Util.getRoot().info(serverName + "," + port + ",socket 读写线程退出");
    }

    /**
     * server读取信息线程
     *
     * @Author: CalmLake
     * @Date: 2018/11/21  11:16
     * @Version: V1.0.0
     **/
    private class SocketServerReadCallable implements Callable<Boolean> {

        @Override
        public Boolean call() {
            int port = socket.getPort();
            try {
                StringBuilder message = new StringBuilder();
                while (socket.isConnected() && ServerConfigCache.SOCKET_SERVER_CONNECTION_ON_OFF_FLAG && !Thread.currentThread().isInterrupted()) {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    byte rcvByte = dataInputStream.readByte();
                    message.append((char) rcvByte);
                    while ((message.toString()).contains(XmlInfoConstant.XML_END_FLAG)) {
                        try {
                            ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT = port;
                            String xmlString = message.toString().trim();
                            Log4j2Util.getXmlMsgQueue().info(String.format("wcs接收，端口：%d ，消息：%s", port, xmlString));
                            EnvelopeDTO envelopeDTO = XStreamUtil.stringToEnvelopeDto(xmlString);
                            XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_RECEIVE, port, envelopeDTO);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log4j2Util.getXmlMsgQueue().error(String.format("读取消息放入队列异常，端口号：%d", port));
                        }
                        message = new StringBuilder();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log4j2Util.getXmlMsgQueue().error(String.format("socket server 读取异常，端口号：%d,异常信息：%s", port, e.getMessage()));
            }
            return false;
        }
    }

    /**
     * server写入信息线程
     *
     * @Author: CalmLake
     * @Date: 2018/11/21  11:18
     * @Version: V1.0.0
     **/
    private class SocketServerWriteCallable implements Callable<Boolean> {

        @Override
        public Boolean call() {
            int port = socket.getPort();
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                while (socket.isConnected() && ServerConfigCache.SOCKET_SERVER_CONNECTION_ON_OFF_FLAG) {
                    String xmlString = null;
                    EnvelopeDTO envelopeDTO;
                    try {
                        envelopeDTO = XmlQueueCache.getSendMsg(port);
                        xmlString = XStreamUtil.toXMLString(envelopeDTO);
                        xmlString = xmlString.replace("__", "_");
                        dataOutputStream.write(xmlString.getBytes());
                        Log4j2Util.getXmlMsgQueue().info(String.format("wcs发送，端口：%d ，消息：%s", port, xmlString));
                        SleepUtil.sleep(0.100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (EOFException e) {
                        Log4j2Util.getXmlMsgQueue().error(String.format("wcs发送，端口：%d ，消息：%s，异常：%s", port, xmlString, e.getMessage()));
                        throw new EOFException();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log4j2Util.getXmlMsgQueue().error(String.format("socket server 写入异常，端口号：%d", port));
            }
            return false;
        }
    }

}
