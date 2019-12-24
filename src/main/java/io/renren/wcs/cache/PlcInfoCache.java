package io.renren.wcs.cache;

import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * plc设备连接配置信息缓存
 * @Author: CalmLake
 * @Date: 2018/11/19  16:04
 * @Version: V1.0.0
 **/
public class PlcInfoCache {

    /**
     * plc连接地址信息
     */
    private  static ConcurrentHashMap<String, SocketAddress> plcConcurrentHashMap = new ConcurrentHashMap<>();

    /**
     * @return java.net.SocketAddress
     *  获取连接地址信息
     * @author CalmLake
     * @date 2018/11/22 15:59
     * @Param [plcName]
     */
    public static SocketAddress getSocketAddress(String plcName) {
        return plcConcurrentHashMap.getOrDefault(plcName, null);
    }

    /**
     *  新增plc连接地址信息
     * @author CalmLake
     * @date 2018/11/18 22:15
     * @Param [plcName, SocketAddress]
     */
    public static void addPlcSocketAddress(String plcName, SocketAddress SocketAddress) {
        plcConcurrentHashMap.putIfAbsent(plcName, SocketAddress);
    }

    /**
     * @return boolean
     *  替换plc连接地址信息
     * @author CalmLake
     * @date 2018/11/18 22:15
     * @Param [plcName, olcSocketAddress, newSocketAddress]
     */
    public static boolean replace(String plcName, SocketAddress olcSocketAddress, SocketAddress newSocketAddress) {
        return plcConcurrentHashMap.replace(plcName, olcSocketAddress, newSocketAddress);
    }

    /**
     *  清空plc连接地址信息哈希表
     * @author CalmLake
     * @date 2018/11/18 22:22
     * @Param []
     */
    public static void clear() {
        plcConcurrentHashMap.clear();
    }

    /**
     * @return java.util.concurrent.plcConcurrentHashMap<java.lang.String,java.net.SocketAddress>
     *  获取plc连接地址信息哈希表
     * @author CalmLake
     * @date 2018/11/18 22:19
     * @Param []
     */
    public static Enumeration<String> getPlcConcurrentHashMapKeys() {
        return plcConcurrentHashMap.keys();
    }

}
