package io.renren.wcs.util;

import java.util.concurrent.ThreadFactory;

/**
 * 线程工厂类
 *
 * @Author: CalmLake
 * @date 2019/6/21  16:22
 * @Version: V1.0.0
 **/
public class ThreadFactoryUtil implements ThreadFactory {
    /**
     * 线程名称
     */
    private String name;

    public ThreadFactoryUtil(String name) {
        this.name = name;
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name );
    }
}
