package io.renren.wap.lock;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @date 2019/7/25  9:55
 * @Version: V1.0.0
 **/
public interface LockInterface {
    /**
     * 线程等待
     *
     * @author CalmLake
     * @date 2019/7/25 9:55
     */
    void await();

    /**
     * 线程唤醒
     *
     * @author CalmLake
     * @date 2019/7/25 9:56
     */
    void signal();
}
