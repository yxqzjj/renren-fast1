package io.renren.wap.client.connection;



import io.renren.wap.client.constant.SocketClientConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 * 创建连接plc的socket client
 * @Author: CalmLake
 * @Date: 2018/11/19  16:27
 * @Version: V1.0.0
 **/
public class ConnectionPlc {
    private Socket socket;
    private SocketAddress socketAddress;

    public ConnectionPlc(Socket socket, SocketAddress socketAddress) {
        this.socket = socket;
        this.socketAddress = socketAddress;
    }

    /**
     * @return boolean
     *  创建socket client连接信息
     * @author CalmLake
     * @date 2018/11/19 18:07
     * @Param []
     */
    public boolean createConnection() {
        try {
            socket.setKeepAlive(true);
            socket.setReceiveBufferSize(SocketClientConstant.RECEIVE_BUFFER_SIZE);
            socket.setSendBufferSize(SocketClientConstant.SEND_BUFFER_SIZE);
            socket.connect(socketAddress);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
