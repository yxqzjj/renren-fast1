package io.renren.wcs.constant;

/**
 * socket使用常量
 * @Author: CalmLake
 * @Date: 2018/11/19  22:11
 * @Version: V1.0.0
 **/
public class SocketClientConstant {
    /**
     * 自动连接线程周期(秒)
     */
    public static final int AUTO_CONNECTION_PERIOD = 5;
    /**
     * 自动连接线程初始化延时时长(秒)
     */
    public static final int AUTO_CONNECTION_INITIAL_DELAY = 3;
    /**
     * 连接失败后等待时间
     */
    public static final long CONNECTION_FAIL_WAIT_TIME = 1000;
    /**
     * 接收缓冲区大小
     */
    public static final int RECEIVE_BUFFER_SIZE = 1024;
    /**
     * 发送缓冲区大小
     */
    public static final int SEND_BUFFER_SIZE = 1024;
}
