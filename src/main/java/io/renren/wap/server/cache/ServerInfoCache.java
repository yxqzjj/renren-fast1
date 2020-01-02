package io.renren.wap.server.cache;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: ServerInfoCache
 * @Description: 服务端配置信息缓存
 * @Author: CalmLake
 * @Date: 2018/11/21  10:55
 * @Version: V1.0.0
 **/
public class ServerInfoCache {
    /**
     * socket server 服务配置信息
     */
    private static ConcurrentHashMap<String, SocketAddress> plcConcurrentHashMap = new ConcurrentHashMap<String, SocketAddress>();

    /**
     * @return boolean
     *  新增服务配置信息
     * @author CalmLake
     * @date 2018/11/21 11:01
     * @Param [serverName, socketAddress]
     */
    public  static boolean add(String serverName, SocketAddress socketAddress) {
        plcConcurrentHashMap.putIfAbsent(serverName,socketAddress);
        return true;
    }


    /**
     * @return boolean
     *  替换服务配置信息
     * @author CalmLake
     * @date 2018/11/21 11:00
     * @Param [serverName, olcSocketAddress, newSocketAddress]
     */
    public  static boolean replace(String serverName, SocketAddress olcSocketAddress, SocketAddress newSocketAddress) {
        return plcConcurrentHashMap.replace(serverName, olcSocketAddress, newSocketAddress);
    }

    /**
     *  清空服务配置信息
     * @author CalmLake
     * @date 2018/11/21 10:59
     * @Param []
     */
    public  static void clear() {
        plcConcurrentHashMap.clear();
    }

}
