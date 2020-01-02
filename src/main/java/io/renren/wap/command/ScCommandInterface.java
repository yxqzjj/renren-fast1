package io.renren.wap.command;

/**
 * 穿梭车动作
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  15:29
 * @Version: V1.0.0
 **/
public interface ScCommandInterface {
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
     * 空车下车
     *
     * @author CalmLake
     * @date 2019/6/6 15:05
     * @throws InterruptedException 线程中断异常
     */
    void emptyOutOfTheCar() throws InterruptedException;

    /**
     * 空车上车
     *
     * @author CalmLake
     * @date 2019/6/6 15:06
     * @throws InterruptedException 线程中断异常
     */
    void emptyGetOnTheCar() throws InterruptedException;

    /**
     * 载货下车
     *
     * @author CalmLake
     * @date 2019/6/6 15:11
     * @throws InterruptedException 线程中断异常
     */
    void carryingOutOfTheCar() throws InterruptedException;

    /**
     * 载货上车
     *
     * @author CalmLake
     * @date 2019/6/6 15:11
     * @throws InterruptedException 线程中断异常
     */
    void carryingGetOnTheCar() throws InterruptedException;

    /**
     * 充电开始
     *
     * @author CalmLake
     * @date 2019/6/6 15:14
     * @throws InterruptedException 线程中断异常
     */
    void chargingStarted() throws InterruptedException;

    /**
     * 充电结束
     *
     * @author CalmLake
     * @date 2019/6/6 15:14
     * @throws InterruptedException 线程中断异常
     */
    void chargeEnd() throws InterruptedException;

    /**
     * 盘点（数数）
     *
     * @author CalmLake
     * @date 2019/6/6 15:17
     * @throws InterruptedException 线程中断异常
     */
    void takeStock() throws InterruptedException;

    /**
     * 理货（同一巷道内货物移动）
     *
     * @author CalmLake
     * @date 2019/6/6 15:19
     * @throws InterruptedException 线程中断异常
     */
    void tallying() throws InterruptedException;
}
