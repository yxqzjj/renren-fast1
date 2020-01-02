package io.renren.wap.cache;

import java.util.HashMap;

/**
 * 优先级（与工作计划类型对应）
 *
 * @Author: CalmLake
 * @Date: 2019/3/8  14:27
 * @Version: V1.0.0
 **/
public class PriorityCache {
    /**
     * K-工作计划类型，V-优先级
     */
    private static HashMap<Integer, Integer> hashMap = new HashMap<>();

    public static void addByte(int workPlanType, int priority) {
        hashMap.put(workPlanType, priority);
    }

    public static Integer getPriority(Integer workPlanType) {
        return hashMap.get(workPlanType);
    }
}
