package io.renren.wap.service;

import io.renren.modules.generator.dao.impl.WcsChargeDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.Block;
import io.renren.modules.generator.entity.WcsMcblockEntity;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.service.charge.ChargeImpl;
import io.renren.wap.service.charge.ChargeInterface;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;

import java.util.List;

/**
 * 切换备车逻辑
 *
 * @Author: CalmLake
 * @Date: 2019/4/18  10:56
 * @Version: V1.0.0
 **/
public class StandbyCarService {
    //  4.修改备车充电路径，删除和新增

    public void changeStandbyCar(String blockName) {
        try {
            //  1.自动切换备车逻辑开关
            if (SystemCache.STANDBY_CAR_SWITCH) {
                //  2.当前设备是否为备车
                WcsScblockEntity presentScBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                if (presentScBlock.getIsStandbyCar()) {
                    //  2.1 原配上场
                    WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectMLBlockByIsStandbyCarAndStandbyCarBlockName(true, blockName);
                    //  3.修改堆垛机状态
                    WcsMlblockDaoImpl.getInstance().updateIsStandbyCarByName(mlBlock.getName(), false, "");
                    //  4.删除备车充电路径信息
                    WcsChargeDaoImpl.getChargeDao().deleteByPrimaryKey(blockName);
                    //  5.创建原车充电完成任务
                    String scBingBlockName = mlBlock.getBingScBlockName();
                    ChargeInterface chargeInterface = new ChargeImpl();
                    chargeInterface.finishCharge(scBingBlockName);
                } else {
                    //  2.2 是否有备车
                    List<WcsScblockEntity> scBlockList = WcsScblockDaoImpl.getInstance().getScBlockListByStandbyCar(true);
                    if (scBlockList.size() > 0) {
                        //  3.备车是否可用
                        for (WcsScblockEntity scBlock : scBlockList) {
                            if (scBlock.getIsUse()) {
                                Log4j2Util.getStandbyCar().info(String.format("%s,该备车正在使用中......！", scBlock.getName()));
                            } else {
                                if (!BlockConstant.STATUS_CHARGE.equals(scBlock.getStatus())) {
                                    WcsScblockDaoImpl.getInstance().updateStatus(scBlock.getName(), BlockConstant.STATUS_CHARGE);
                                }
                                //  3.修改堆垛机状态
                                WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByBingScBlockName(blockName);
                                WcsMlblockDaoImpl.getInstance().updateIsStandbyCarByName(mlBlock.getName(), true, scBlock.getName());
                                //  4.新增备车充电路径信息
                                ChargeService chargeService = new ChargeService();
                                chargeService.createChargeRoute(blockName, scBlock.getName());
                                //  5.创建备车充电完成任务
                                ChargeInterface chargeInterface = new ChargeImpl();
                                chargeInterface.finishCharge(scBlock.getName());
                            }
                        }
                    } else {
                        Log4j2Util.getStandbyCar().info("穿梭车列表中无备车!您需要购买两只备胎，会赠送您一只备车！");
                    }
                }
            } else {
                Log4j2Util.getStandbyCar().info("备车逻辑开关：关闭");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log4j2Util.getStandbyCar().error(String.format("blockName:%s ，出现异常：%s", blockName, e.getMessage()));
        }
    }

    /**
     * 获取当前交互穿梭车名称
     *
     * @param block 设备状态信息对象
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/4/18 15:52
     */
    public static String getScBlockName(Block block) {
        String name = block.getName();
        String scBlockName = null;
        if (block instanceof WcsMlblockEntity) {
            WcsMlblockEntity mlBlock = (WcsMlblockEntity) block;
            if (mlBlock.getIsStandbyCar()) {
                scBlockName = mlBlock.getStandbyCarBlockName();
            } else {
                scBlockName = mlBlock.getBingScBlockName();
            }
        } else if (block instanceof WcsMcblockEntity) {
            WcsMcblockEntity mcBlock = (WcsMcblockEntity) block;
            if (mcBlock.getIsStandbyCar()) {
                scBlockName = mcBlock.getStandbyCarBlockName();
            } else {
                scBlockName = mcBlock.getBingScBlockName();
            }
        } else {
            Log4j2Util.getStandbyCar().info(String.format("%s 设备类型未解析", name));
        }
        return scBlockName;
    }
}
