package io.renren.wap.server.cache;

/**
 * 服务端配置数据
 *
 * @Author: CalmLake
 * @Date: 2018/11/21  11:02
 * @Version: V1.0.0
 **/
public class ServerConfigCache {
    /**
     * 服务一直开启等待连接
     */
    public static boolean ALWAYS_ACCEPT_CLIENT = true;
    /**
     * 服务中的新建连接开关 默认开启
     */
    public static boolean SOCKET_SERVER_CONNECTION_ON_OFF_FLAG = true;
    /**
     * 服务中的默认活跃连接端口号 以最后一次读取消息的端口号为活跃
     */
    public static int DEFAULT_LIVING_CLIENT_PORT = 0;
}
