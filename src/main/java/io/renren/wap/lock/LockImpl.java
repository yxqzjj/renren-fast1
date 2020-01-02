package io.renren.wap.lock;


import io.renren.wap.dto.LockDTO;
import io.renren.wap.util.Log4j2Util;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @date 2019/7/25  9:58
 * @Version: V1.0.0
 **/
public class LockImpl implements LockInterface {
    private LockDTO lockDTO;

    public LockImpl(LockDTO lockDTO) {
        this.lockDTO = lockDTO;
    }

    /**
     * 线程等待
     *
     * @author CalmLake
     * @date 2019/7/25 9:55
     */
    @Override
    public void await() {
        Log4j2Util.getAssigningTaskLogger().error(String.format("数据block分配任务：%s ，开始等待....", lockDTO.getName()));
        lockDTO.getLock().lock();
        try {
            lockDTO.getCondition().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log4j2Util.getAssigningTaskLogger().error("任务等待与唤醒出错：" + e.getMessage());
        } finally {
            lockDTO.getLock().unlock();
        }
    }

    /**
     * 线程唤醒
     *
     * @author CalmLake
     * @date 2019/7/25 9:56
     */
    @Override
    public void signal() {
        lockDTO.getLock().lock();
        lockDTO.getCondition().signal();
        lockDTO.getLock().unlock();
        Log4j2Util.getAssigningTaskLogger().error(String.format("数据block分配任务：%s ，打脸唤醒....", lockDTO.getName()));
    }
}
