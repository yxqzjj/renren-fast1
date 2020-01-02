package io.renren.wap.queue;

import com.alibaba.fastjson.JSONObject;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;

import java.util.Enumeration;

/**
 * @ClassName: ViewQueue
 * @Description: 查看队列信息
 * @Author: CalmLake
 * @Date: 2018/11/22  21:53
 * @Version: V1.0.0
 **/
public class ViewQueue {

    /**
     * @return com.alibaba.fastjson.JSONObject ["send":["8021":210,"20000",2563],"receive":["8021":210,"20000",2563]] 8021-端口号 210数据数量
     *  获取xml消息队列信息（SocketServer）
     * @author CalmLake
     * @date 2018/11/22 22:43
     * @Param []
     */
    public static JSONObject viewXmlQueue() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectReceive = new JSONObject();
        Enumeration<Integer> portReceive = XmlQueueCache.getMapKeys(XmlQueueConstant.QUEUE_TYPE_RECEIVE);
        while (portReceive.hasMoreElements()) {
            int port = portReceive.nextElement();
            try {
                int size = XmlQueueCache.getMapValueSize(XmlQueueConstant.QUEUE_TYPE_RECEIVE, port);
                jsonObjectReceive.put(port + "", size);
            } catch (NullPointerException e) {
                jsonObjectReceive.put(port + "", null);
            }
        }
        JSONObject jsonObjectSend = new JSONObject();
        Enumeration<Integer> portSend = XmlQueueCache.getMapKeys(XmlQueueConstant.QUEUE_TYPE_RECEIVE);
        while (portSend.hasMoreElements()) {
            int port = portSend.nextElement();
            try {
                int size = XmlQueueCache.getMapValueSize(XmlQueueConstant.QUEUE_TYPE_SEND, port);
                jsonObjectSend.put(port + "", size);
            } catch (NullPointerException e) {
                jsonObjectSend.put(port + "", null);
            }
        }
        jsonObject.put("send", jsonObjectSend);
        jsonObject.put("receive", jsonObjectReceive);
        return jsonObject;
    }

    /**
     * @return com.alibaba.fastjson.JSONObject ["send":["ML01":210],"receive":["ML01":210]] 8021-端口号 210数据数量
     *  获取msg消息队列信息 (socketClient)
     * @author CalmLake
     * @date 2018/11/23 20:50
     * @Param []
     */
    public static JSONObject viewMsgQueue() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectReceive = new JSONObject();
        Enumeration<String> plcNamesReceive = MsgQueueCache.getMapKeys(MsgConstant.QUEUE_TYPE_RECEIVE);
        while (plcNamesReceive.hasMoreElements()) {
            String plcName = plcNamesReceive.nextElement();
            int size = MsgQueueCache.getMapValueSize(MsgConstant.QUEUE_TYPE_RECEIVE, plcName);
            jsonObjectReceive.put(plcName + "", size);
        }
        JSONObject jsonObjectSend = new JSONObject();
        Enumeration<String> plcNamesSend = MsgQueueCache.getMapKeys(MsgConstant.QUEUE_TYPE_SEND);
        while (plcNamesSend.hasMoreElements()) {
            String plcName = plcNamesSend.nextElement();
            int size = MsgQueueCache.getMapValueSize(MsgConstant.QUEUE_TYPE_SEND, plcName);
            jsonObjectSend.put(plcName + "", size);
        }
        jsonObject.put("send", jsonObjectSend);
        jsonObject.put("receive", jsonObjectReceive);
        return jsonObject;
    }
}
