package io.renren.wap.block.status;

import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.factory.BlockFactory;
import io.renren.wap.factory.BlockImpl;

/**
 * 设备任务信息
 *
 * @Author: CalmLake
 * @date 2019/7/25  11:57
 * @Version: V1.0.0
 **/
public class BlockStatus extends AbstractBlockStatus {
    private Block block;

    public BlockStatus(WcsMachineEntity machine) {
        super(machine);
    }

    /**
     * 给设备对象赋值————设备信息
     *
     * @author CalmLake
     * @date 2019/7/25 11:56
     */
    @Override
    public void setMachineValues() {
        BlockFactory blockFactory = new BlockImpl();
        block = blockFactory.getBlock(getMachine());
    }

    @Override
    public Block getBlock() {
        return block;
    }
}
