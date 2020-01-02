package io.renren.wap.command;

/**
 * 堆垛机/母车/升降机动作
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  15:26
 * @Version: V1.0.0
 **/
public interface MlMcAlCommandInterface {

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

    /**
     * 接车
     *
     * @author CalmLake
     * @date 2019/6/6 14:57
     * @throws InterruptedException 线程中断异常
     */

    void pickUpTheCar() throws InterruptedException;

    /**
     * 卸车
     *
     * @author CalmLake
     * @date 2019/6/6 15:03
     * @throws InterruptedException 线程中断异常
     */

    void unloadTheCar() throws InterruptedException;
}
