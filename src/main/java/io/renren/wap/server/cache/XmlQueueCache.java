package io.renren.wap.server.cache;



import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: XmlQueueCache
 * @Description: xml消息队列
 * @Author: CalmLake
 * @Date: 2018/11/21  11:37
 * @Version: V1.0.0
 **/
public class XmlQueueCache {
    /**
     * 接收消息队列  以端口为匹配标准
     */
    private static ConcurrentHashMap<Integer, LinkedBlockingQueue<EnvelopeDTO>> receiveMsg = new ConcurrentHashMap<>();
    /**
     * 发送消息队列 以端口为匹配标准
     */
    private static ConcurrentHashMap<Integer, LinkedBlockingQueue<EnvelopeDTO>> sendMsg = new ConcurrentHashMap<>();

    /**
     * @return int  -1 表示map中不含此key值
     * 获取哈希表中该键对应的值——队列中的消息数量
     * @author CalmLake
     * @date 2018/11/24 17:04
     * @Param [type, port]
     */
    public static int getMapValueSize(int type, int port) {
        if (type == XmlQueueConstant.QUEUE_TYPE_SEND) {
            return sendMsg.containsKey(port) ? sendMsg.get(port).size() : -1;
        } else {
            return receiveMsg.containsKey(port) ? receiveMsg.get(port).size() : -1;
        }
    }

    /**
     * @return java.util.Enumeration<java.lang.Integer>
     * 获取哈希表的键
     * @author CalmLake
     * @date 2018/11/24 17:04
     * @Param [type]
     */
    public static Enumeration<Integer> getMapKeys(int type) {
        if (type == XmlQueueConstant.QUEUE_TYPE_SEND) {
            return sendMsg.keys();
        } else {
            return receiveMsg.keys();
        }
    }

    /**
     * 初始化xml收消息队列
     *
     * @author CalmLake
     * @date 2018/11/21 13:09
     * @Param [port] 端口号
     */
    public static void initReceiveConcurrentHashMap(Integer port) {
        receiveMsg.put(port, new LinkedBlockingQueue<>());
    }

    /**
     * 初始化xml发消息队列
     *
     * @author CalmLake
     * @date 2018/11/21 13:09
     * @Param [port] 端口号
     */
    public static void initSendConcurrentHashMap(Integer port) {
        sendMsg.put(port, new LinkedBlockingQueue<>());
    }

    /**
     * 向消息队列中添加一条消息
     *
     * @param type        类型
     * @param port        端口号
     * @param envelopeDTO xml消息
     * @author CalmLake
     * @date 2019/1/16 17:50
     */
    public static void addMsg(Integer type, Integer port, EnvelopeDTO envelopeDTO) throws InterruptedException {
        assert envelopeDTO != null;
        if (DbUtil.getYmlReadUtil().getSystemConfigInfo().isWmsOpen()) {
            ConcurrentHashMap<Integer, LinkedBlockingQueue<EnvelopeDTO>> msg;
            if (type == XmlQueueConstant.QUEUE_TYPE_SEND) {
                msg = sendMsg;
            } else {
                msg = receiveMsg;
            }
            if (msg.containsKey(port)) {
                msg.get(port).put(envelopeDTO);
            }
        }
    }

    /**
     * @return com.wap.client.dto.MsgDTO
     * 从发送消息队列取一条消息
     * @author CalmLake
     * @date 2018/11/19 23:37
     * @Param [plcName]
     */
    public static EnvelopeDTO getSendMsg(Integer port) throws InterruptedException {
        EnvelopeDTO envelopeDTO;
        envelopeDTO = sendMsg.get(port).take();
        return envelopeDTO;
    }

    /**
     * @return com.wap.client.dto.MsgDTO
     * 从接收消息队列取一条消息
     * @author CalmLake
     * @date 2018/11/19 23:37
     * @Param [plcName]
     */
    public static EnvelopeDTO getReceiveMsg(Integer port) throws InterruptedException {
        return receiveMsg.get(port).take();
    }

    /**
     * 移除此哈希表中key为此port的k-v
     *
     * @author CalmLake
     * @date 2018/11/21 12:32
     * @Param [type, port]
     */
    public synchronized static void removeTheKey(Integer type, Integer port) {
        if (type == XmlQueueConstant.QUEUE_TYPE_SEND) {
            sendMsg.remove(port);
        } else {
            receiveMsg.remove(port);
        }
        Log4j2Util.getXmlMsgQueue().info(String.format("端口： %d ，消息队列类型：%d，消息队列已移除", port, type));
    }
}
