package io.renren.wap.service.block;

/**
 * 消息制作
 *
 * @Author: CalmLake
 * @Date: 2019/3/27  11:10
 * @Version: V1.0.0
 **/
public interface OperationKeyService {
    /**
     * 消息制作
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @throws InterruptedException  异常
     * @date 2019/3/27 11:11
     */
    void operationKey(String blockName) throws InterruptedException;
}
