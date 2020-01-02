package io.renren.wap.cache;



import io.renren.wap.lock.LockInterface;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 锁 缓存
 *
 * @Author: CalmLake
 * @date 2019/7/25  10:10
 * @Version: V1.0.0
 **/
public class LockCache {
    private static ConcurrentHashMap<String, LockInterface> stringLockHashMap = new ConcurrentHashMap<>();

    /**
     * 放入键值对
     *
     * @param key           键
     * @param lockInterface 值 （控制等待与唤醒的对象）
     * @author CalmLake
     * @date 2019/7/25 10:16
     */
    public static void put(String key, LockInterface lockInterface) {
        stringLockHashMap.putIfAbsent(key, lockInterface);
    }

    /**
     * 取得值 （控制等待与唤醒的对象）
     *
     * @param key 键
     * @return com.wap.lock.LockInterface 值
     * @author CalmLake
     * @date 2019/7/25 10:17
     */
    public static LockInterface getValue(String key) {
        return stringLockHashMap.get(key);
    }
}
