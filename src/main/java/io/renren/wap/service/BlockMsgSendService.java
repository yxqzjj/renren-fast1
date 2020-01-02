package io.renren.wap.service;


import io.renren.wap.cache.BlockCache;
import io.renren.wap.client.cache.MsgQueueCache;
import io.renren.wap.client.dto.MsgCycleOrderDTO;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.thread.callable.ResendMsgCallable;
import io.renren.wap.util.Log4j2Util;

/**
 * 消息发送服务
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  16:10
 * @Version: V1.0.0
 **/
public class BlockMsgSendService {
    /**
     * 将发送消息放入对应队列
     *
     * @param msgDTO    消息对象
     * @param blockName 数据block名称
     * @return boolean
     * @author CalmLake
     * @date 2019/1/16 9:27
     */
    public boolean sendMsg(MsgDTO msgDTO, String blockName) throws InterruptedException {
        if (msgDTO == null) {
            Log4j2Util.getBlockBrickLogger().error(String.format("%s，消息制作失败，消息对象为空，条件不符合", blockName));
            return false;
        }
        if (msgDTO instanceof MsgCycleOrderDTO) {
            BlockCache.addString(blockName, ((MsgCycleOrderDTO) msgDTO).getCycleCommand());
        }
        MsgQueueCache.addSendMsg(msgDTO);
        return true;
    }

    /**
     * 消息重发
     *
     * @param msgDTO    消息
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/1/29 15:42
     */
    public void resendMsg(MsgDTO msgDTO, String blockName) {
        ResendMsgCallable resendMsgCallable = new ResendMsgCallable(blockName, msgDTO);
        ThreadPoolServiceSingleton.getInstance().getExecutorServiceCallable().submit(resendMsgCallable);
    }
}
