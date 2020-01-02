package io.renren.wap.block.status;


import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMachineEntity;

/**
 * 设备状态超类
 *
 * @Author: CalmLake
 * @date 2019/7/25  11:51
 * @Version: V1.0.0
 **/
public abstract class AbstractBlockStatus {
    private WcsMachineEntity machine;

    AbstractBlockStatus(WcsMachineEntity machine) {
        this.machine = machine;
    }

    public WcsMachineEntity getMachine() {
        return machine;
    }


    /**
     * 给设备对象赋值
     *
     * @author CalmLake
     * @date 2019/7/25 11:56
     */
    public abstract void setMachineValues();

    public abstract Block getBlock();
}
