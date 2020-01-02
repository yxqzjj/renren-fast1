package io.renren.wap.service.block.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsAlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.BlockMsgSendService;
import io.renren.wap.service.block.OperationKeyService;
import io.renren.wap.service.msg.AlMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 升降机逻辑处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  11:16
 * @Version: V1.0.0
 **/
public class AlBlockServiceImpl extends BlockService implements OperationKeyService {

    @Override
    public void operationKey(String blockName) throws InterruptedException {
        WcsAlblockEntity alBlock = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (isCanSendMsg(alBlock)) {
            BlockMsgSendService blockMsgSendService = new BlockMsgSendService();
            MsgDTO msgDTO = doWork(alBlock.getMckey(), alBlock);
            boolean resultSend = blockMsgSendService.sendMsg(msgDTO, blockName);
            if (resultSend) {
                updateCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, WcsAlblockDaoImpl.getInstance(), blockName);
                blockMsgSendService.resendMsg(msgDTO, blockName);
            }
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，当前设备不满足消息制作条件！", blockName));
        }
    }

    /**
     * 逻辑处理
     *
     * @param mcKey   mcKey
     * @param alBlock 升降机状态
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/16 11:58
     */
    private MsgDTO doWork(String mcKey, WcsAlblockEntity alBlock) {
        MsgDTO msgDTO = null;
        AlMsgService alMsgService = new AlMsgService();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanType = workPlan.getType();
        String blockName = alBlock.getName();
        String withWorkBlockName = alBlock.getWithWorkBlockName();
        String berthBlockName = alBlock.getBerthBlockName();
        if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION ==workPlanType) {
            String dock1 = "0101";
            String dock2 = "0102";
            String dock3 = "0103";
            String toStation = workPlan.getToStation();
            String nextBlockName;
            String tier;
            if (dock1.equals(toStation)) {
                tier = "01";
                nextBlockName = "0003";
            } else if (dock2.equals(toStation)) {
                tier = "02";
                nextBlockName = "0004";
            } else if (dock3.equals(toStation)) {
                tier = "03";
                nextBlockName = "0005";
            } else {
                tier = "01";
                nextBlockName = "0003";
            }
            msgDTO = alMsgService.move(mcKey, blockName, nextBlockName, toStation, tier);
        } else if (WorkPlanConstant.TYPE_CHANGE_TIER ==workPlanType) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(alBlock.getWithWorkBlockName());
            String scBlockWithWorkBlockName = scBlock.getWithWorkBlockName();
            String scBlockName = alBlock.getReserved2();
            if (StringUtils.isNotEmpty(scBlockName)) {
                if (scBlockWithWorkBlockName.contains(MachineConstant.TYPE_MC)) {
                    String nextBlockName;
                    String tier;
                    String dock;
                    String dock1 = "0101";
                    String dock2 = "0102";
                    String dock3 = "0103";
                    if (scBlockWithWorkBlockName.equals("MC01")) {
                        tier = "01";
                        nextBlockName = "0003";
                        dock = dock1;
                    } else if (scBlockWithWorkBlockName.equals("MC02")) {
                        tier = "02";
                        nextBlockName = "0004";
                        dock = dock2;
                    } else if (scBlockWithWorkBlockName.equals("MC03")) {
                        tier = "03";
                        nextBlockName = "0005";
                        dock = dock3;
                    } else {
                        tier = "01";
                        nextBlockName = "0003";
                        dock = dock1;
                    }
                    if (berthBlockName.equals(dock)) {
                        msgDTO = alMsgService.offCar(mcKey, blockName, alBlock.getWithWorkBlockName(), alBlock.getWithWorkBlockName(), tier, MsgCycleOrderConstant.CYCLE_UNCAR_06);
                    } else {
                        msgDTO = alMsgService.move(mcKey, blockName, nextBlockName, dock, tier);
                    }
                }else {
                    String toStation = workPlan.getToStation();
                    String nextBlockName;
                    String tier;
                    String dock;
                    String dock1 = "0101";
                    String dock2 = "0102";
                    String dock3 = "0103";
                    if (toStation.equals("MC01")) {
                        tier = "01";
                        nextBlockName = "0003";
                        dock = dock1;
                    } else if (toStation.equals("MC02")) {
                        tier = "02";
                        nextBlockName = "0004";
                        dock = dock2;
                    } else if (toStation.equals("MC03")) {
                        tier = "03";
                        nextBlockName = "0005";
                        dock = dock3;
                    } else {
                        tier = "01";
                        nextBlockName = "0003";
                        dock = dock1;
                    }
                    if (berthBlockName.equals(dock)) {
                        msgDTO = alMsgService.offCar(mcKey, blockName, alBlock.getWithWorkBlockName(), alBlock.getWithWorkBlockName(), tier, MsgCycleOrderConstant.CYCLE_UNCAR_06);
                    } else {
                        msgDTO = alMsgService.move(mcKey, blockName, nextBlockName, dock, tier);
                    }
                }
            } else {
                if (scBlockWithWorkBlockName.contains(MachineConstant.TYPE_MC)) {
                    String fromStation = workPlan.getFromStation();
                    String nextBlockName;
                    String tier;
                    String dock;
                    String dock1 = "0101";
                    String dock2 = "0102";
                    String dock3 = "0103";
                    if (fromStation.equals("MC01")) {
                        tier = "01";
                        nextBlockName = "0003";
                        dock = dock1;
                    } else if (fromStation.equals("MC02")) {
                        tier = "02";
                        nextBlockName = "0004";
                        dock = dock2;
                    } else if (fromStation.equals("MC03")) {
                        tier = "03";
                        nextBlockName = "0005";
                        dock = dock3;
                    } else {
                        tier = "01";
                        nextBlockName = "0003";
                        dock = dock1;
                    }
                    if (berthBlockName.equals(dock)) {
                        msgDTO = alMsgService.getCar(mcKey, blockName, alBlock.getWithWorkBlockName(), alBlock.getWithWorkBlockName(), tier, MsgCycleOrderConstant.CYCLE_PICK_UP_THE_CAR_05);
                    } else {
                        msgDTO = alMsgService.move(mcKey, blockName, nextBlockName, dock, tier);
                    }
                }
            }
        } else {
            WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
            if (alBlock.getIsLoad()) {
                if (StringUtils.isNotEmpty(berthBlockName) && machine.getDockName().equals(berthBlockName)) {
                    // 卸货
                    msgDTO = alMsgService.transplantingTheUnloading(workPlanType, mcKey, blockName, withWorkBlockName, machine.getDockName());
                } else {
                    // 移动向该设备（输送线）
                    msgDTO = alMsgService.move(mcKey, blockName, machine.getBlockName(), machine.getDockName(), MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
                }
            } else {
                if (StringUtils.isNotEmpty(berthBlockName) && machine.getDockName().equals(berthBlockName)) {
                    // 取货
                    msgDTO = alMsgService.transplantingPickUp(workPlanType, mcKey, blockName, withWorkBlockName, machine.getDockName());
                } else {
                    // 移动向该设备（输送线）
                    msgDTO = alMsgService.move(mcKey, blockName, machine.getBlockName(), machine.getDockName(), MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
                }
            }
        }
        return msgDTO;
    }
}
