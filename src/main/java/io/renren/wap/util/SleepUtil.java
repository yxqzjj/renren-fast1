package io.renren.wap.util;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/2/28  16:35
 * @Version: V1.0.0
 **/
public class SleepUtil {
    /**
     * 线程休眠 秒
     */
    public static void sleep(int num) {
        try {
            Thread.sleep(1000 * num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程休眠 秒
     */
    public static void sleep(double num) {
        try {
            Thread.sleep((long) ((long) 1000 * num));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程休眠默认10秒
     */
    public static void sleep() {
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
