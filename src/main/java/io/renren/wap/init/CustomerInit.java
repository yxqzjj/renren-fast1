package io.renren.wap.init;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsPlcconfigEntity;
import io.renren.wap.customer.block.*;
import io.renren.wap.customer.plc.*;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.PlcConfigConstant;
import io.renren.wap.singleton.ThreadPoolServiceSingleton;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.logging.log4j.LogManager;

import java.util.List;

/**
 * 消费者初始化
 *
 * @Author: CalmLake
 * @Date: 2019/2/28  14:28
 * @Version: V1.0.0
 **/
public class CustomerInit {

    /**
     * 初始化入口
     *
     * @author CalmLake
     * @date 2019/2/28 14:44
     */
    public void init() {
        initPlcCustomer();
        initBlockCustomer();
        LogManager.getLogger().info("wcs-plc消息消费启动！");
    }

    /**
     * 初始化plc消费者
     *
     * @author CalmLake
     * @date 2019/2/28 14:43
     */
    private void initPlcCustomer() {
        List<WcsMachineEntity> machineList = DbUtil.getMachineDao().selectList(new QueryWrapper<WcsMachineEntity>().select("Block_Name","Plc_Name").groupBy("Block_Name","Plc_Name"));
        for (WcsMachineEntity machine : machineList) {
            String plcName = machine.getPlcName();
            WcsPlcconfigEntity plcConfig=DbUtil.getPlcConfigDao().selectOne(new QueryWrapper<WcsPlcconfigEntity>().eq("Name",plcName));
            if (!PlcConfigConstant.STATUS_BAN.equals(plcConfig.getStatus())) {
                AbstractPlcCustomer abstractPlcCustomer;
                if (plcName.contains(MachineConstant.TYPE_SC)) {
                    abstractPlcCustomer = new ScPlcCustomer(plcName);
                } else if (plcName.contains(MachineConstant.TYPE_MC)) {
                    abstractPlcCustomer = new McPlcCustomer(plcName);
                } else if (plcName.contains(MachineConstant.TYPE_ML)) {
                    abstractPlcCustomer = new MlPlcCustomer(plcName);
                } else if (plcName.contains(MachineConstant.TYPE_RGV)) {
                    abstractPlcCustomer = new RgvPlcCustomer(plcName);
                } else if (plcName.contains(MachineConstant.TYPE_AL)) {
                    abstractPlcCustomer = new AlPlcCustomer(plcName);
                } else {
                    abstractPlcCustomer = new ClPlcCustomer(plcName);
                }
                ThreadPoolServiceSingleton.getInstance().getExecutorMachineCustomer().submit(abstractPlcCustomer);
                Log4j2Util.getRoot().info(plcName + ",plc消费者启动！");
            } else {
                Log4j2Util.getRoot().info(plcName + ",plc 已禁用！");
            }
        }
    }

    /**
     * 初始化block消费者
     *
     * @author CalmLake
     * @date 2019/2/28 14:43
     */
    private void initBlockCustomer() {
        List<WcsMachineEntity> machineList = DbUtil.getMachineDao().selectList(new QueryWrapper<WcsMachineEntity>());
        for (WcsMachineEntity machine : machineList) {
            String blockName = machine.getBlockName();
            WcsPlcconfigEntity plcConfig=DbUtil.getPlcConfigDao().selectOne(new QueryWrapper<WcsPlcconfigEntity>().eq("Name",machine.getPlcName()));
            if (!PlcConfigConstant.STATUS_BAN.equals(plcConfig.getStatus())) {
                BlockCustomer blockCustomer;
                if (blockName.contains(MachineConstant.TYPE_SC)) {
                    blockCustomer = new ScBlockCustomer(blockName);
                } else if (blockName.contains(MachineConstant.TYPE_MC)) {
                    blockCustomer = new McBlockCustomer(blockName);
                } else if (blockName.contains(MachineConstant.TYPE_ML)) {
                    blockCustomer = new MlBlockCustomer(blockName);
                } else if (blockName.contains(MachineConstant.TYPE_RGV)) {
                    blockCustomer = new RgvBlockCustomer(blockName);
                } else if (blockName.contains(MachineConstant.TYPE_AL)) {
                    blockCustomer = new AlBlockCustomer(blockName);
                } else {
                    blockCustomer = new ClBlockCustomer(blockName);
                }
                ThreadPoolServiceSingleton.getInstance().getExecutorMachineCustomer().submit(blockCustomer);
                Log4j2Util.getRoot().info(blockName + ",block消费者启动！");
            }else {
                Log4j2Util.getRoot().info(blockName + ",block 禁用！");
            }

        }
    }

}
