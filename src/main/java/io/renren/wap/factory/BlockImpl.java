package io.renren.wap.factory;


import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMachineEntity;

/**
 * block对象实现
 *
 * @Author: CalmLake
 * @date 2019/7/25  14:31
 * @Version: V1.0.0
 **/
public class BlockImpl implements BlockFactory {
    private BlockDaoFactory blockDaoFactory;

    public BlockImpl() {
        this.blockDaoFactory = new BlockDaoImpl();
    }

    /**
     * 获取设备block信息
     *
     * @param machine 设备信息
     * @return com.wap.entity.BlockImpl
     * @author CalmLake
     * @date 2019/7/25 14:26
     */
    @Override
    public Block getBlock(WcsMachineEntity machine) {
        BlockDao blockDao=blockDaoFactory.getBlockDao(machine);
        return blockDao.selectByPrimaryKey(machine.getBlockName());
    }
}
