package io.renren.wap.cache;

import java.util.HashMap;

/**
 * block缓存
 *
 * @Author: CalmLake
 * @Date: 2019/3/6  16:25
 * @Version: V1.0.0
 **/
public class BlockCache {
    /**
     * block缓存信息 k-blockName，V-string（cycleCommand）  值为 当前执行中的命令
     */
    private static HashMap<String, String> hashMap = new HashMap<>();

    public static synchronized void addString(String blockName, String string) {
        hashMap.put(blockName, string);
    }

    public static String getString(String blockName) {
        return hashMap.get(blockName);
    }
}
