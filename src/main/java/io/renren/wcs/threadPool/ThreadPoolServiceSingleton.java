package io.renren.wcs.threadPool;
import io.renren.wcs.constant.SocketClientConstant;
import io.renren.wcs.systemConfig.SystemConfigInfo;
import java.util.concurrent.*;

/**
 * @author     ：zzwy
 * @date       ：2019/12/20
 * @description：线程池
 * @modified By：
 * @version: 1.0
 */
public class ThreadPoolServiceSingleton {
    /**
     * block制作消息03线程池（多线程并行）
     * 缓存线程，线程池中有直接拿来用
     */
    private final ExecutorService executorServiceBlockMakeMsg = Executors.newCachedThreadPool();

    /**
     * 消费者管理线程池
     */
    private final ExecutorService executorMachineCustomer = Executors.newCachedThreadPool();
    /**
     * 设备状态判断线程池
     */
    private final ExecutorService executorServiceCallable = Executors.newCachedThreadPool();
    /**
     * socket client 读写线程管理服务（多线程）
     */
    private final ExecutorService executorServiceReadWrite = Executors.newCachedThreadPool();
    /**
     * socket server 读写线程管理服务（多线程）
     */
    private final ExecutorService executorServiceSocketServerReadWrite = Executors.newCachedThreadPool();
    /**
     * socket server 管理服务(单线程)
     * 单例定时线程只能存在一个
     */
    private final ExecutorService executorServiceServer = Executors.newSingleThreadScheduledExecutor();
    /**
     * socket client 自动重连管理服务(单线程)
     * 单例定时线程只能存在一个
     */
    private final ScheduledExecutorService executorServiceAutoConnection = Executors.newSingleThreadScheduledExecutor();
    /**
     * 定时删除文件
     * 单例定时线程只能存在一个
     */
    private final ScheduledExecutorService executorServiceDelete = Executors.newSingleThreadScheduledExecutor();
    /**
     * 心跳询问
     * 单例定时线程只能存在一个
     */
    private final ScheduledExecutorService executorServiceHeartBeatAsk = Executors.newSingleThreadScheduledExecutor();
    /**
     * 状态询问
     * 单例定时线程只能存在一个
     */
    private final ScheduledExecutorService executorServiceMachineStatusAsk = Executors.newSingleThreadScheduledExecutor();
    /**
     * 默认线程工厂
     * 线程池工厂
     */
    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
    /**
     * 入库任务分配(单线程)  k-站台编号 v-线程管理服务
     */
    private final ConcurrentHashMap<String, ExecutorService> executorServiceConcurrentHashMap = new ConcurrentHashMap<>();

    private static ThreadPoolServiceSingleton ourInstance = new ThreadPoolServiceSingleton();

    public static ThreadPoolServiceSingleton getInstance() {
        return ourInstance;
    }

    private ThreadPoolServiceSingleton() {
    }

    /**
     * block消息制作
     *
     * @param callable 消息制作线程
     * @return java.util.concurrent.Future<?> boolean
     * @author CalmLake
     * @date 2019/3/1 11:26
     */
    public Future<?> submitBlockMakeMsg(Callable<Boolean> callable) {
        return executorServiceBlockMakeMsg.submit(callable);
    }

    /**
     * 获取入库任务分配时使用的线程管理器(每个站台对应一个管理器)
     *
     * @param station 站台编号
     * @return java.util.concurrent.ExecutorService
     * @author CalmLake
     * @date 2019/1/9 11:14
     */
    public ExecutorService getPutInStorageAssigningTaskExecutorService(String station) {
        if (!executorServiceConcurrentHashMap.containsKey(station)) {
            executorServiceConcurrentHashMap.putIfAbsent(station, Executors.newSingleThreadScheduledExecutor());
        }
        return executorServiceConcurrentHashMap.get(station);
    }

    /**
     * 开启执行一个读写线程
     *
     * @return java.util.concurrent.Future<?>
     * @author CalmLake
     * @date 2018/11/26 20:02
     * @Param [runnable] socket server 读写工作线程
     */
    public Future<?> submitSocketServerReadWrite(Runnable runnable) {
        return executorServiceSocketServerReadWrite.submit(runnable);
    }

    /**
     * @return java.util.concurrent.Future<?>
     * 开启唯一socket server服务 等待客户端连接
     * @author CalmLake
     * @date 2018/11/26 18:53
     * @Param [runnable]
     */
    public Future<?> startSocketServer(Runnable runnable) {
        return executorServiceServer.submit(runnable);
    }

    /**
     * @return java.util.concurrent.Future<?>
     * 开启执行一个读写线程
     * @author CalmLake
     * @date 2018/11/26 18:00
     * @Param [runnable]  socket client 读写工作线程
     */
    public Future<?> submitReadWrite(Runnable runnable) {
        return executorServiceReadWrite.submit(runnable);
    }

    /**
     * @return java.util.concurrent.ScheduledFuture<?>
     * 开启唯一周期性检测socket client连接线程 且断线重连
     * @author CalmLake
     * @date 2018/11/26 18:06
     * @Param [runnable]
     */
    public ScheduledFuture<?> cyclicExecutingConnection(Runnable runnable) {
        return executorServiceAutoConnection.scheduleAtFixedRate(runnable, SocketClientConstant.AUTO_CONNECTION_INITIAL_DELAY, SocketClientConstant.AUTO_CONNECTION_PERIOD, TimeUnit.SECONDS);
    }

    /**
     * @return java.util.concurrent.ScheduledFuture<?>
     * 开启唯一心跳状态检测
     * @author CalmLake
     * @date 2018/11/26 18:06
     * @Param [runnable]
     */
    public ScheduledFuture<?> cyclicExecutingHeartBeatAsk(Runnable runnable) {
        return executorServiceHeartBeatAsk.scheduleAtFixedRate(runnable, SocketClientConstant.AUTO_CONNECTION_INITIAL_DELAY,SystemConfigInfo.getSystemConfigInfoSingleton().getHeartBeatAsk(), TimeUnit.SECONDS);
    }

    /**
     * @return java.util.concurrent.ScheduledFuture<?>
     * 开启唯一设备状态检测
     * @author CalmLake
     * @date 2018/11/26 18:06
     * @Param [runnable]
     */
    public ScheduledFuture<?> cyclicExecutingMachineStatus(Runnable runnable) {
        return executorServiceMachineStatusAsk.scheduleAtFixedRate(runnable, SocketClientConstant.AUTO_CONNECTION_INITIAL_DELAY,SystemConfigInfo.getSystemConfigInfoSingleton().getMachineStatusAsk(), TimeUnit.SECONDS);
    }

    /**
     * 获取设备状态判断线程池
     *
     * @return java.util.concurrent.ExecutorService
     * @author CalmLake
     * @date 2019/2/28 14:35
     */
    public ExecutorService getExecutorServiceCallable() {
        return executorServiceCallable;
    }

    /**
     * 获取消费者线程池
     *
     * @return java.util.concurrent.ExecutorService
     * @author CalmLake
     * @date 2019/2/28 14:35
     */
    public ExecutorService getExecutorMachineCustomer() {
        return executorMachineCustomer;
    }

    /**
     * 默认线程工厂
     * @return java.util.concurrent.ThreadFactory
     */
    public ThreadFactory getDefaultThreadFactory() {
        return defaultThreadFactory;
    }

    /**
     * 定时删除文件
     * @author CalmLake
     * @date 2019/4/24 14:44
     * @return java.util.concurrent.ScheduledExecutorService
     */
    public ScheduledExecutorService getExecutorServiceDelete() {
        return executorServiceDelete;
    }
}
