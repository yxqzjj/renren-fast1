package io.renren.wap.factory;

import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMachineEntity;

/**
 * block工厂
 *
 * @Author: CalmLake
 * @date 2019/7/25  14:24
 * @Version: V1.0.0
 **/
public interface BlockFactory {
    /**
     * 获取设备block信息（DB数据）
     *
     * @param machine 设备信息
     * @return com.wap.entity.BlockImpl
     * @author CalmLake
     * @date 2019/7/25 14:26
     */
     Block getBlock(WcsMachineEntity machine);

}
