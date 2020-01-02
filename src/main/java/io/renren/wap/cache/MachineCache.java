package io.renren.wap.cache;


import io.renren.modules.generator.entity.WcsMachineEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 设备信息缓存
 *
 * @Author: CalmLake
 * @Date: 2019/1/23  17:02
 * @Version: V1.0.0
 **/
public class MachineCache {
    private static HashMap<String, WcsMachineEntity> hashMap = new HashMap<>();

    /**
     * 获取有原点位置的设备信息
     *
     * @return java.util.List<com.wap.entity.Machine>
     * @author CalmLake
     * @date 2019/3/8 15:57
     */
    public static List<WcsMachineEntity> getHaveDefaultLocationMachine() {
        List<WcsMachineEntity> machineList = new ArrayList<>();
        for (WcsMachineEntity machine : hashMap.values()) {
            if (StringUtils.isNotEmpty(machine.getDefaultLocation())) {
                WcsMachineEntity machineNew = new WcsMachineEntity();
                machineNew.setDefaultLocation(StringUtils.isNotEmpty(machine.getDefaultLocation()) ? machine.getDefaultLocation() : "");
                machineNew.setReserved2(StringUtils.isNotEmpty(machine.getReserved2()) ? machine.getReserved2() : "");
                machineNew.setReserved1(StringUtils.isNotEmpty(machine.getReserved1()) ? machine.getReserved1() : "");
                machineNew.setStationName(StringUtils.isNotEmpty(machine.getStationName()) ? machine.getStationName() : "");
                machineNew.setPlcName(StringUtils.isNotEmpty(machine.getPlcName()) ? machine.getPlcName() : "");
                machineNew.setDockName(StringUtils.isNotEmpty(machine.getDockName()) ? machine.getDockName() : "");
                machineNew.setBlockName(StringUtils.isNotEmpty(machine.getBlockName()) ? machine.getBlockName() : "");
                machineNew.setName(StringUtils.isNotEmpty(machine.getName()) ? machine.getName() : "");
                machineNew.setType(machine.getType() > 0 ? machine.getType() : 0);
                machineNew.setWarehouseNo(machine.getWarehouseNo() > 0 ? machine.getWarehouseNo() : 0);
                machineNew.setTaskFlag(machine.getTaskFlag());
                machineList.add(machineNew);
            }
        }
        return machineList;
    }

    /**
     * 获取任务分配设备
     *
     * @return java.util.List<com.wap.entity.Machine>
     * @author CalmLake
     * @date 2019/7/26 16:46
     */
    public static List<WcsMachineEntity> getTaskFlagMachine() {
        List<WcsMachineEntity> machineList = new ArrayList<>();
        for (WcsMachineEntity machine : hashMap.values()) {
            if (machine.getTaskFlag()) {
                WcsMachineEntity machineNew = new WcsMachineEntity();
                machineNew.setDefaultLocation(StringUtils.isNotEmpty(machine.getDefaultLocation()) ? machine.getDefaultLocation() : "");
                machineNew.setReserved2(StringUtils.isNotEmpty(machine.getReserved2()) ? machine.getReserved2() : "");
                machineNew.setReserved1(StringUtils.isNotEmpty(machine.getReserved1()) ? machine.getReserved1() : "");
                machineNew.setStationName(StringUtils.isNotEmpty(machine.getStationName()) ? machine.getStationName() : "");
                machineNew.setPlcName(StringUtils.isNotEmpty(machine.getPlcName()) ? machine.getPlcName() : "");
                machineNew.setDockName(StringUtils.isNotEmpty(machine.getDockName()) ? machine.getDockName() : "");
                machineNew.setBlockName(StringUtils.isNotEmpty(machine.getBlockName()) ? machine.getBlockName() : "");
                machineNew.setName(StringUtils.isNotEmpty(machine.getName()) ? machine.getName() : "");
                machineNew.setType(machine.getType() > 0 ? machine.getType() : 0);
                machineNew.setWarehouseNo(machine.getWarehouseNo() > 0 ? machine.getWarehouseNo() : 0);
                machineNew.setTaskFlag(machine.getTaskFlag());
                machineList.add(machineNew);
            }
        }
        return machineList;
    }

    /**
     * 添加设备信息
     *
     * @param blockName 数据block名称
     * @param machine   设备信息
     * @author CalmLake
     * @date 2019/1/23 17:06
     */
    public static void addMachine(String blockName, WcsMachineEntity machine) {
        hashMap.putIfAbsent(blockName, machine);
    }

    /**
     * 获取设备信息
     *
     * @param blockName 数据block名称
     * @return com.wap.entity.Machine
     * @author CalmLake
     * @date 2019/1/23 17:07
     */
    public static WcsMachineEntity getMachine(String blockName) {
        return hashMap.get(blockName);
    }
}
