package io.renren.wcs.cache;

import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端连接信息缓存
 * @Author: CalmLake
 * @Date: 2018/11/18  22:06
 * @Version: V1.0.0
 **/
public class ClientInfoCache {
    /**
     * socket连接信息
     * key - plc名称
     * Socket - 当前使用连接
     */
    private static ConcurrentHashMap<String, Socket> concurrentHashMap = new ConcurrentHashMap<>();


    /**
     * @return java.util.Enumeration<java.lang.String>
     * 获取socket连接的归属plc集合
     * @author CalmLake
     * @date 2018/11/25 14:00
     */
    public static Enumeration<String> getConcurrentHashMapKeys() {
        return concurrentHashMap.keys();
    }

    /**
     * @return java.net.Socket
     * 获取plc对应的连接
     * @author CalmLake
     * @date 2018/11/25 14:01
     * @Param [plcName]
     */
    public static Socket getSocket(String plcName) {
        return concurrentHashMap.getOrDefault(plcName, null);
    }

    /**
     * 初始化socket client 连接信息
     *
     * @author CalmLake
     * @date 2018/11/22 16:06
     * @Param [plcName]
     */
    public static void initConcurrentHashMap(String plcName) {
        concurrentHashMap.putIfAbsent(plcName, new Socket());
    }

    /**
     * @return boolean
     * 新增连接信息
     * @author CalmLake
     * @date 2018/11/18 22:15
     * @Param [plcName, socket]
     */
    public static boolean add(String plcName, Socket socket) {
        concurrentHashMap.putIfAbsent(plcName, socket);
        return true;
    }

    /**
     * @return boolean
     * 替换废弃连接
     * @author CalmLake
     * @date 2018/11/18 22:15
     * @Param [plcName, olcSocket, newSocket]
     */
    public static boolean replace(String plcName, Socket olcSocket, Socket newSocket) {
        return concurrentHashMap.replace(plcName, olcSocket, newSocket);
    }

    /**
     * 清空连接信息哈希表
     *
     * @author CalmLake
     * @date 2018/11/18 22:22
     * @Param []
     */
    public static void clear() {
        concurrentHashMap.clear();
    }
}
