package io.renren.wap.service;

import io.renren.modules.generator.dao.impl.WcsAlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMcblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsRgvblockDaoImpl;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.util.DbUtil;

/**
 * 设备服务
 *
 * @Author: CalmLake
 * @Date: 2019/1/22  16:20
 * @Version: V1.0.0
 **/
public class MachineService {


    /**
     * 输送线和其交互设备位置是否相同（判断码头号）
     *
     * @param blockName         输送线数据block名称
     * @param withWorkBlockName 交互设备数据block名称
     * @return boolean
     * @author CalmLake
     * @date 2019/3/14 17:39
     */
    public static boolean isSameDock(String blockName, String withWorkBlockName) {
        WcsMachineEntity machineBlock = MachineCache.getMachine(blockName);
        WcsMachineEntity machineWithWorkBlock = MachineCache.getMachine(withWorkBlockName);
        String berthBlockName;
        if (MachineConstant.BYTE_TYPE_ML.equals(machineWithWorkBlock.getType())) {
            berthBlockName = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName).getBerthBlockName();
        } else if (MachineConstant.BYTE_TYPE_AL.equals(machineWithWorkBlock.getType())) {
            berthBlockName = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName).getBerthBlockName();
        } else if (MachineConstant.BYTE_TYPE_MC.equals(machineWithWorkBlock.getType())) {
            berthBlockName = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName).getBerthBlockName();
        } else if (MachineConstant.BYTE_TYPE_RGV.equals(machineWithWorkBlock.getType())) {
            berthBlockName = WcsRgvblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName).getBerthBlockName();
        } else {
            berthBlockName = null;
        }
        return machineBlock.getDockName().equals(berthBlockName);
    }

    /**
     * 是否为穿梭车
     *
     * @param blockName 数据block名称
     * @return boolean
     * @author CalmLake
     * @date 2019/1/22 16:29
     */
    public static boolean isScMachine(String blockName) {
        return blockName.contains(MachineConstant.TYPE_SC);
    }

    /**
     * 是否为输送线
     *
     * @param blockName 数据block名称
     * @return boolean
     * @author CalmLake
     * @date 2019/1/22 16:23
     */
    public static boolean isClMachine(String blockName) {
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        if (machine == null) {
            return false;
        }
        return MachineConstant.BYTE_TYPE_CL == machine.getType();
    }
}
