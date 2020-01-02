package io.renren.wap.dto;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 锁组合对象
 *
 * @Author: CalmLake
 * @date 2019/7/25  9:56
 * @Version: V1.0.0
 **/
public class LockDTO {
    private String name;
    private Lock lock;
    private Condition condition;

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
