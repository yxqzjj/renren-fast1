package io.renren.wap.cache;



import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.constant.QueueConstant;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 队列缓存
 *
 * @Author: CalmLake
 * @Date: 2019/1/9  15:14
 * @Version: V1.0.0
 **/
public class BlockQueueCache {
    /**
     * block消息队列
     */
    private static ConcurrentHashMap<String, LinkedBlockingQueue<MsgDTO>> concurrentHashMapBlockReceiveMsg = new ConcurrentHashMap<>();
    private static LinkedBlockingQueue<String> finishWorkPlan = new LinkedBlockingQueue<>(QueueConstant.CAPACITY_SIZE);


    /**
     * 添加接收消息
     *
     * @param blockName 数据block编号
     * @param msgDTO    消息
     * @author CalmLake
     * @date 2019/1/9 15:24
     */
    public static void addMsg(String blockName, MsgDTO msgDTO) throws InterruptedException {
        if (!concurrentHashMapBlockReceiveMsg.containsKey(blockName)) {
            concurrentHashMapBlockReceiveMsg.put(blockName, new LinkedBlockingQueue<>(QueueConstant.CAPACITY_SIZE));
        }
        concurrentHashMapBlockReceiveMsg.get(blockName).put(msgDTO);
    }

    /**
     * 取出接收消息
     *
     * @param blockName 数据block编号
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/23 14:36
     */
    public static MsgDTO getMsg(String blockName) throws InterruptedException {
        if (!concurrentHashMapBlockReceiveMsg.containsKey(blockName)) {
            concurrentHashMapBlockReceiveMsg.put(blockName, new LinkedBlockingQueue<>(QueueConstant.CAPACITY_SIZE));
        }
        return concurrentHashMapBlockReceiveMsg.get(blockName).take();
    }

    /**
     * 向队列添加任务完成信息
     *
     * @param string 任务完成信息
     * @author CalmLake
     * @date 2019/4/3 11:59
     */
    public static void addWorkPlanFishString(String string) throws InterruptedException {
        finishWorkPlan.put(string);
    }

    /**
     * 从队列取出任务完成信息
     *
     * @author CalmLake
     * @date 2019/4/3 11:59
     */
    public static String getWorkPlanFishString() throws InterruptedException {
        return finishWorkPlan.take();
    }
}
