package io.renren.wap.factory;


import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.WcsMachineEntity;

/**
 * blockDao工厂
 *
 * @Author: CalmLake
 * @date 2019/7/25  14:24
 * @Version: V1.0.0
 **/
public interface BlockDaoFactory {

    /**
     * 获取设备blockDao信息(操作数据库对象)
     *
     * @param blockName 数据block名称
     * @return com.wap.dao.BlockDao
     * @author CalmLake
     * @date 2019/7/25 14:39
     */
    BlockDao getBlockDao(String blockName);

    /**
     * 获取设备blockDao信息(操作数据库对象)
     *
     * @param machine 设备信息
     * @return com.wap.dao.BlockDao
     * @author CalmLake
     * @date 2019/7/25 14:39
     */
    BlockDao getBlockDao(WcsMachineEntity machine);
}
