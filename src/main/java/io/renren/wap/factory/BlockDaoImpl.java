package io.renren.wap.factory;


import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.dao.impl.*;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.util.DbUtil;

/**
 * 获取设备blockDao信息(操作数据库对象)
 *
 * @Author: CalmLake
 * @date 2019/7/25  14:43
 * @Version: V1.0.0
 **/
public class BlockDaoImpl implements BlockDaoFactory {

    @Override
    public BlockDao getBlockDao(String blockName) {
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        return getBlockDao(machine);
    }

    /**
     * 获取设备blockDao信息(操作数据库对象)
     *
     * @param machine 设备信息
     * @return com.wap.dao.BlockDao
     * @author CalmLake
     * @date 2019/7/25 14:39
     */
    @Override
    public BlockDao getBlockDao(WcsMachineEntity machine) {
        BlockDao blockDao = null;
        if (MachineConstant.BYTE_TYPE_SC.equals(machine.getType())) {
            blockDao = WcsScblockDaoImpl.getInstance();
        } else if (MachineConstant.BYTE_TYPE_MC.equals(machine.getType())) {
            blockDao = WcsMcblockDaoImpl.getInstance();
        } else if (MachineConstant.BYTE_TYPE_ML.equals(machine.getType())) {
            blockDao = WcsMlblockDaoImpl.getInstance();
        } else if (MachineConstant.BYTE_TYPE_RGV.equals(machine.getType())) {
            blockDao = WcsRgvblockDaoImpl.getInstance();
        } else if (MachineConstant.BYTE_TYPE_AL.equals(machine.getType())) {
            blockDao = WcsAlblockDaoImpl.getInstance();
        } else if (MachineConstant.BYTE_TYPE_CL.equals(machine.getType())) {
            blockDao =WcsClblockDaoImpl.getInstance();
        }
        return blockDao;
    }
}
