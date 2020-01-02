package io.renren.wap.command;

/**
 * 输送线动作
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  15:23
 * @Version: V1.0.0
 **/
public interface RgvCommandInterface {
    /**
     * 取货
     *
     * @author CalmLake
     * @date 2019/6/6 14:54
     * @throws InterruptedException 线程中断异常
     */
    void pickup() throws InterruptedException;

    /**
     * 卸货
     *
     * @author CalmLake
     * @date 2019/6/6 14:54
     * @throws InterruptedException 线程中断异常
     */
    void unload() throws InterruptedException;

    /**
     * 移动
     *
     * @author CalmLake
     * @date 2019/6/6 14:55
     * @throws InterruptedException 线程中断异常
     */

    void move() throws InterruptedException;
}
