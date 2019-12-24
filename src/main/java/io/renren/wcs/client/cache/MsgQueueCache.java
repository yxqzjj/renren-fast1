package io.renren.wcs.client.cache;

import io.renren.wcs.client.contant.MsgConstant;
import io.renren.wcs.client.contant.QueueConstant;
import io.renren.wcs.client.dto.MsgDTO;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送接收消息队列缓存
 *
 * @Author: CalmLake
 * @Date: 2018/11/19  20:24
 * @Version: V1.0.0
 **/
public class MsgQueueCache {

    /**
     * 接收消息队列 以plc为单位
     */
    private static ConcurrentHashMap<String, LinkedBlockingQueue<MsgDTO>> receiveMsg = new ConcurrentHashMap<>();
    /**
     * 发送消息队列 以plc为单位
     */
    private static ConcurrentHashMap<String, LinkedBlockingQueue<MsgDTO>> sendMsg = new ConcurrentHashMap<>();


    /**
     * @return int -1 表示map中不含此key值
     * 获取哈希表中该键对应的值——队列中的消息数量
     * @author CalmLake
     * @date 2018/11/24 17:00
     * @Param [type, plcName]
     */
    public static int getMapValueSize(int type, String plcName) {
        if (type == MsgConstant.QUEUE_TYPE_SEND) {
            return sendMsg.containsKey(plcName) ? sendMsg.get(plcName).size() : -1;
        } else {
            return receiveMsg.containsKey(plcName) ? receiveMsg.get(plcName).size() : -1;
        }
    }


    /**
     * @return java.util.Enumeration<java.lang.String>
     * 获取哈希表的键
     * @author CalmLake
     * @date 2018/11/24 16:59
     * @Param [type]
     */
    public static Enumeration<String> getMapKeys(int type) {
        if (type == MsgConstant.QUEUE_TYPE_SEND) {
            return sendMsg.keys();
        } else {
            return receiveMsg.keys();
        }
    }

    /**
     * 初始化plc收发消息队列
     *
     * @author CalmLake
     * @date 2018/11/21 13:15
     * @Param [plcName]
     */
    public static void initConcurrentHashMap(String plcName) {
        receiveMsg.putIfAbsent(plcName, new LinkedBlockingQueue<>(QueueConstant.CAPACITY_SIZE));
        sendMsg.putIfAbsent(plcName, new LinkedBlockingQueue<>(QueueConstant.CAPACITY_SIZE));
    }

    /**
     * 向发送消息队列中添加一条消息
     *
     * @author CalmLake
     * @date 2018/11/19 23:38
     * @Param msgDTO 消息对象
     */
    public static void addSendMsg(MsgDTO msgDTO) throws InterruptedException {
        String plcName = msgDTO.getPlcName();
        if (sendMsg.containsKey(plcName)) {
            sendMsg.get(plcName).put(msgDTO);
        } else {
            sendMsg.putIfAbsent(plcName, new LinkedBlockingQueue<>(QueueConstant.CAPACITY_SIZE));
            sendMsg.get(plcName).put(msgDTO);
        }
    }

    /**
     * @return com.wap.client.dto.MsgDTO
     * 从发送消息队列取一条消息
     * @author CalmLake
     * @date 2018/11/19 23:37
     * @Param [plcName]
     */
    public static MsgDTO getSendMsg(String plcName) throws InterruptedException {
        return sendMsg.get(plcName).take();
    }

    /**
     * 向接收消息队列中添加一条消息
     *
     * @author CalmLake
     * @date 2018/11/19 23:39
     * @Param [plcName, msgDTO]
     */
    public static void addReceiveMsg(String plcName, MsgDTO msgDTO) throws InterruptedException {
        if (receiveMsg.containsKey(plcName)) {
            receiveMsg.get(plcName).put(msgDTO);
        } else {
            receiveMsg.putIfAbsent(plcName, new LinkedBlockingQueue<>(QueueConstant.CAPACITY_SIZE));
            receiveMsg.get(plcName).put(msgDTO);
        }
    }

    /**
     * @return com.wap.client.dto.MsgDTO
     * 从接收消息队列取一条消息
     * @author CalmLake
     * @date 2018/11/19 23:37
     * @Param [plcName]
     */
    public static MsgDTO getReceiveMsg(String plcName) throws InterruptedException {
        return receiveMsg.get(plcName).take();
    }
}
