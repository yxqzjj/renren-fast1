package io.renren.wap.command.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.renren.modules.generator.dao.impl.WcsAlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.LockCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.MlMcAlCommandInterface;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.*;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 升降机动作消息处理
 *
 * @Author: CalmLake
 * @date 2019/8/15  11:02
 * @Version: V1.0.0
 **/
public class AlCycleCommandImplAbstract extends AbstractMachineCycleCommand implements MlMcAlCommandInterface {
    private WcsAlblockEntity block;
    private boolean isLoad;
    private boolean loadStorage;
    private String loadCar;
    private String station;
    private String dockName;

    public AlCycleCommandImplAbstract(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
        super(msgCycleOrderFinishReportDTO);
    }

    /**
     * 处理消息
     *
     * @throws InterruptedException 线程中断异常
     * @author CalmLake
     * @date 2019/6/6 17:00
     */
    @Override
    public void execute() throws InterruptedException {
        block = WcsAlblockDaoImpl.getInstance().getInstance().selectByPrimaryKey(blockName);
        String command = block.getCommand();
        String blockMcKey = block.getMckey();
        String blockAppointmentMcKey = block.getAppointmentMckey();
        String withWorkBlockName = block.getWithWorkBlockName();
        station = msgCycleOrderFinishReportDTO.getStation();
        dockName = msgCycleOrderFinishReportDTO.getDock();
        isLoad = block.getIsLoad();
        loadCar = null;
        switch (loadStatus) {
            case MsgCycleOrderConstant.LOAD_STATUS_NONE:
                loadStorage = false;
                loadCar = "";
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVE:
                loadStorage = true;
                loadCar = "";
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVA_CAR:
                loadStorage = false;
                if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
                    loadCar = withWorkBlockName;
                }
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVE_CAR_LOAD:
                loadStorage = true;
                if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
                    loadCar = withWorkBlockName;
                }
                break;
            default:
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,未知状态信息: %s", blockName, loadStatus));
                throw new IllegalStateException("未知状态信息: " + loadStatus);
        }
        if (MsgCycleOrderFinishReportService.checkData(msgMcKey, blockMcKey, blockAppointmentMcKey, blockName)) {
            if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT.equals(command) || MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK.equals(command)) {
                // 回复05
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            } else {
                if (MsgCycleOrderFinishReportService.isFinishedSuccess(finishCode, finishType, blockName)) {
                    if (MsgCycleOrderConstant.CYCLE_TRANSPLANTING_PICKUP_07.equals(cycleCommand)) {
                        pickup();
                    } else if (MsgCycleOrderConstant.CYCLE_TRANSPLANTING_THE_UNLOADING_08.equals(cycleCommand)) {
                        unload();
                    } else if (MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04.equals(cycleCommand)) {
                        move();
                    } else if (MsgCycleOrderConstant.CYCLE_PICK_UP_THE_CAR_05.equals(cycleCommand)) {
                        pickUpTheCar();
                    } else if (MsgCycleOrderConstant.CYCLE_UNCAR_06.equals(cycleCommand)) {
                        unloadTheCar();
                    } else if (MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_LOAD_11.equals(cycleCommand)) {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，载货移动暂不使用，cycleCommand：%s", blockName, cycleCommand));
                        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，未解析的指示命令，cycleCommand：%s", blockName, cycleCommand));
                        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                    }
                } else {
                    WcsMlblockDaoImpl.getInstance().updateBlockErrorCodeByPrimaryKey(blockName, finishCode);
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，异常完成，cycleCommand：%s,finishType：%s，finishCode：%s", blockName, cycleCommand, finishType, finishCode));
                    MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                }
            }
        } else {
            MsgCycleOrderFinishReportAckService.sendMsgCycleOrderFinishReportAck(msgCycleOrderFinishReportAckConditionDTO);
        }
    }

    /**
     * 取货
     *
     * @throws InterruptedException 线程中断异常
     * @author CalmLake
     * @date 2019/6/6 14:54
     */
    @Override
    public void pickup() throws InterruptedException {
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        String toStation = workPlan.getToStation();
        // 移载取货
        WcsAlblockDaoImpl.getInstance().updateBlockTransplantingPickUpFinished(blockName);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        // 继续新任务
        String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
        if (MachineService.isClMachine(nextBlockName)) {
            WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(nextBlockName);
            String clMcKey = clBlock.getMckey();
            String clAppointmentMcKey = clBlock.getAppointmentMckey();
            //  当前设备分配任务  当前有任务  修改交互设备名称
            WcsAlblockDaoImpl.getInstance().updateTwoALBlock(msgMcKey, nextBlockName, blockName);
            //  制作动作消息
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            if (StringUtils.isEmpty(clMcKey) && StringUtils.isEmpty(clAppointmentMcKey)) {
                //  下一设备设备无任务 无预约任务 分配任务
                WcsClblockDaoImpl.getInstance().updateMcKeyCLBlock(msgMcKey, blockName, nextBlockName);
                //   输送线  制作动作消息
                BlockServiceImplFactory.blockServiceDoKey(nextBlockName);
            } else if (StringUtils.isNotEmpty(clMcKey) && StringUtils.isEmpty(clAppointmentMcKey)) {
                //  1.当前下一设备有任务无预约任务 分配预约任务
                WcsClblockDaoImpl.getInstance().updateAppointmentMcKeyCLBlock(msgMcKey, blockName, nextBlockName);
                //  制作自己动作消息
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，该设备的条件不满足，McKey：%s,AppointmentMcKey：%s", blockName, clMcKey, clAppointmentMcKey));
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载取货完成，制作队列消息时未解析的交互设备类型，nextBlockName：%s", blockName, nextBlockName));
        }

    }

    /**
     * 卸货
     *
     * @throws InterruptedException 线程中断异常
     * @author CalmLake
     * @date 2019/6/6 14:54
     */
    @Override
    public void unload() throws InterruptedException {
        WcsAlblockDaoImpl.getInstance().updateBlockTransplantingTheUnloadingFinished(blockName);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        WcsAlblockEntity block = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String blockAppointmentMcKey = block.getAppointmentMckey();
        if (StringUtils.isNotEmpty(blockAppointmentMcKey)) {
            String withWorkBlockName = block.getReserved1();
            WcsAlblockDaoImpl.getInstance().updateThreeALBlock(blockAppointmentMcKey, "", withWorkBlockName, blockName);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载卸货完成，当前无预约任务", blockName));
            LockCache.getValue(blockName).signal();
        }
    }

    /**
     * 移动
     *
     * @throws InterruptedException 线程中断异常
     * @author CalmLake
     * @date 2019/6/6 14:55
     */
    @Override
    public void move() throws InterruptedException {
        // 移动
        WcsAlblockDaoImpl.getInstance().updateMoveFinishByPrimaryKey(blockName, tier, dockName);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        int workPlanType = workPlan.getType();
        //  回原点任务
        if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            //  移动 且处于默认位置时完成任务
            if (StringUtils.isNotEmpty(dockName) && dockName.equals(workPlan.getToStation())) {
                WcsAlblockDaoImpl.getInstance().updateMcKey("", blockName);
                WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
                LockCache.getValue(blockName).signal();
            }
        }
        BlockServiceImplFactory.blockServiceDoKey(blockName);
        WcsAlblockEntity block = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String blockMcKey = block.getMckey();
        String withWorkBlockName = block.getWithWorkBlockName();
        if (StringUtils.isNotEmpty(blockMcKey)) {
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            if (MachineService.isClMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            }
            if (MachineService.isScMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，制作队列消息时未解析的交互设备类型，withWorkBlockName：%s", blockName, withWorkBlockName));
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，当前设备无任务", blockName));
            LockCache.getValue(blockName).signal();
        }
    }

    /**
     * 接车
     *
     * @throws InterruptedException 线程中断异常
     * @author CalmLake
     * @date 2019/6/6 14:57
     */
    @Override
    public void pickUpTheCar() throws InterruptedException {
        // 接车
        WcsAlblockDaoImpl.getInstance().updateGetCarFinishByPrimaryKey(blockName, block.getWithWorkBlockName());
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);

        WcsAlblockEntity block = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String blockMcKey = block.getMckey();
        String withWorkBlockName = block.getWithWorkBlockName();
        if (StringUtils.isNotEmpty(blockMcKey)) {
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            if (MachineService.isClMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            }
            if (MachineService.isScMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，制作队列消息时未解析的交互设备类型，withWorkBlockName：%s", blockName, withWorkBlockName));
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，当前设备无任务", blockName));
            LockCache.getValue(blockName).signal();
        }
    }

    /**
     * 卸车
     *
     * @throws InterruptedException 线程中断异常
     * @author CalmLake
     * @date 2019/6/6 15:03
     */
    @Override
    public void unloadTheCar() throws InterruptedException {
        // 卸车
        WcsAlblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, "", "", "");
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);

        WcsAlblockEntity block = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String blockMcKey = block.getMckey();
        String withWorkBlockName = block.getWithWorkBlockName();
        if (StringUtils.isNotEmpty(blockMcKey)) {
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            if (MachineService.isClMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            }
            if (MachineService.isScMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，制作队列消息时未解析的交互设备类型，withWorkBlockName：%s", blockName, withWorkBlockName));
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，当前设备无任务", blockName));
            LockCache.getValue(blockName).signal();
        }
    }

}
