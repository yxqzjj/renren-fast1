package io.renren.wap.command.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsChargeDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMcblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsChargeEntity;
import io.renren.modules.generator.entity.WcsMcblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.PriorityCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.ScCommandInterface;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.ChargeConstant;
import io.renren.wap.entity.constant.TaskingConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.*;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.service.warehouse.FinishWorkPlanInterface;
import io.renren.wap.service.warehouse.impl.FinishWorkPlanImpl;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 穿梭车动作
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  17:13
 * @Version: V1.0.0
 **/
public class ScCycleCommandImplAbstract extends AbstractMachineCycleCommand implements ScCommandInterface {
    public WcsScblockEntity scBlock;
    public WcsWorkplanEntity workPlan;

    public ScCycleCommandImplAbstract(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
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
        scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String command = scBlock.getCommand();
        String scBlockMcKey = scBlock.getMckey();
        String scBlockAppointmentMcKey = scBlock.getAppointmentMckey();
        if (MsgCycleOrderFinishReportService.checkData(msgMcKey, scBlockMcKey, scBlockAppointmentMcKey, blockName)) {
            if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT.equals(command) || MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK.equals(command)) {
                // 回复05
                MsgCycleOrderFinishReportAckService.replay05(msgCycleOrderFinishReportAckConditionDTO);
            } else {
                if (MsgCycleOrderFinishReportService.isFinishedSuccess(finishCode, finishType, blockName)) {
                    workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));


                    if (MsgCycleOrderConstant.CYCLE_COMMAND_PICK_UP_02.equals(cycleCommand)) {
                        pickup();
                    } else if (MsgCycleOrderConstant.CYCLE_COMMAND_UNLOAD_03.equals(cycleCommand)) {
                        unload();
                    } else if (MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04.equals(cycleCommand)) {
                        move();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_ON_CAR_EMPTY_09.equals(cycleCommand)) {
                        emptyGetOnTheCar();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_OFF_CAR_EMPTY_10.equals(cycleCommand)) {
                        emptyOutOfTheCar();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_ON_CAR_12.equals(cycleCommand)) {
                        carryingGetOnTheCar();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_OFF_CAR_13.equals(cycleCommand)) {
                        carryingOutOfTheCar();
                    } else if (MsgCycleOrderConstant.CYCLE_CHARGE_START_14.equals(cycleCommand)) {
                        chargingStarted();
                    } else if (MsgCycleOrderConstant.CYCLE_CHARGE_FINISH_15.equals(cycleCommand)) {
                        chargeEnd();
                    } else if (MsgCycleOrderConstant.CYCLE_TALLY_17.equals(cycleCommand)) {
                        tallying();
                    } else if (MsgCycleOrderConstant.CYCLE_TAKE_STOCK_16.equals(cycleCommand)) {
                        takeStock();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_CL_A_18.equals(cycleCommand)) {
                        goClA();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_CL_B_19.equals(cycleCommand)) {
                        goClB();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_AL_20.equals(cycleCommand)) {
                        goAl();
                    } else if (MsgCycleOrderConstant.CYCLE_GO_MC_21.equals(cycleCommand)) {
                        goMc();
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，未解析的指示命令，cycleCommand：%s", blockName, cycleCommand));
                    }
                } else {
                    WcsScblockDaoImpl.getInstance().updateBlockErrorCodeByPrimaryKey(blockName, finishCode);
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，异常完成，cycleCommand：%s,finishType：%s，finishCode：%s", blockName, cycleCommand, finishType, finishCode));
                }
            }
        }
    }

    /**
     * 取货
     *
     * @author CalmLake
     * @date 2019/6/6 14:54
     */
    @Override
    public void pickup() throws InterruptedException {
        WcsScblockDaoImpl.getInstance().updatePickUpFinishByPrimaryKey(blockName, true, new Date(), row, line, tier);
        //  取货  任务没有结束
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        BlockServiceImplFactory.blockServiceDoKey(blockName);
    }

    /**
     * 卸货
     *
     * @author CalmLake
     * @date 2019/6/6 14:54
     */
    @Override
    public void unload() throws InterruptedException {
        //  卸货  任务结束
        WcsScblockDaoImpl.getInstance().updateUnloadFinishByPrimaryKey(blockName, false, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        //  任务完成操作
        try {
            FinishWorkPlanInterface finishWorkPlan = new FinishWorkPlanImpl();
            finishWorkPlan.finishOutStorage(workPlan);
        } catch (Exception e) {
            e.printStackTrace();
            Log4j2Util.getMsgCustomerLogger().error(String.format("数据block：%s,异常：%s", blockName, e.getLocalizedMessage()));
        }
        //  消息发送
        BlockServiceImplFactory.blockServiceDoKey(blockName);
    }

    /**
     * 移动
     *
     * @author CalmLake
     * @date 2019/6/6 14:55
     */
    @Override
    public void move() throws InterruptedException {
        int workPlanType = workPlan.getType();
        String toStation = workPlan.getToStation();
        int warehouseNo = workPlan.getWarehouseNo();
        //  充电且需要在货架中穿梭时使用
        //  移动完成
        WcsScblockDaoImpl.getInstance().updateMoveFinishByPrimaryKey(blockName, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        BlockService blockService = new BlockService();
        WcsChargeEntity charge = WcsChargeDaoImpl.getChargeDao().selectByPrimaryKey(blockName);
        short mlMcNum;
        if (ChargeConstant.TYPE_MACHINE.equals(charge.getType()) || ChargeConstant.TYPE_AT_PRESENT_MACHINE.equals(charge.getType())) {
            mlMcNum = TaskingConstant.ML_MC_ONE;
        } else {
            mlMcNum = TaskingConstant.ML_MC_TWO;
        }
        if (blockService.isSameSite(row, line, tier, SystemCache.SYS_CHARGE_LOCATION_A)) {
            if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
                //  终点设备分配任务    tasking
                createTasking(msgMcKey, toStation, workPlanType, blockName, mlMcNum, warehouseNo);
            }
        } else if (blockService.isSameSite(row, line, tier, SystemCache.SYS_CHARGE_LOCATION_B)) {
            if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
                //  终点设备分配任务    tasking
                createTasking(msgMcKey, toStation, workPlanType, blockName, mlMcNum, warehouseNo);
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("移动完成：未处理，mcKey：%s", msgMcKey));
        }
    }

    /**
     * 空车下车
     *
     * @author CalmLake
     * @date 2019/6/6 15:05
     */
    @Override
    public void emptyOutOfTheCar() throws InterruptedException {
        String reserved2 = scBlock.getReserved2();
        int workPlanType = workPlan.getType();
        WcsScblockDaoImpl.getInstance().updateOffCarEmptyFinishByPrimaryKey(blockName, "", false, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType && BlockConstant.SC_PRIORITY_OFF_CAR_RESERVED2.equals(reserved2)) {
            WcsScblockDaoImpl.getInstance().updateReserved2ByName(blockName, BlockConstant.DEFAULT_RESERVED2);
        } else if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
            // 卸车任务 子车下车后结束 清除mcKey
            WcsScblockDaoImpl.getInstance().updateMcKey("", blockName);
        } else if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
            WcsScblockDaoImpl.getInstance().updateMcKey("", blockName);
            String scBlockName = workPlan.getReserved2();
            if (blockName.equals(scBlockName)) {
                WcsScblockDaoImpl.getInstance().updateBerthBlockNameByPrimaryKey(blockName,BlockConstant.BERTH_BLOCK_NAME_LOCATION);
                WcsScblockDaoImpl.getInstance().updateStatus(blockName, BlockConstant.STATUS_BAN);
                FinishWorkPlanInterface finishWorkPlan = new FinishWorkPlanImpl();
                finishWorkPlan.finishOutStorage(workPlan);
            }
        }
        BlockServiceImplFactory.blockServiceDoKey(blockName);
    }

    /**
     * 空车上车
     *
     * @author CalmLake
     * @date 2019/6/6 15:06
     */
    @Override
    public void emptyGetOnTheCar() throws InterruptedException {
        int workPlanType = workPlan.getType();
        String mcKey = workPlan.getMckey();
        String toStation = workPlan.getToStation();
        String withWorkBlockName = scBlock.getWithWorkBlockName();
        boolean loadStorage = false;
        if (MsgCycleOrderConstant.LOAD_STATUS_HAVE.equals(loadStatus)) {
            loadStorage = true;
        }
        if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType && withWorkBlockName.equals(toStation)) {
            String scBlockMcKey = scBlock.getMckey();
            String scBlockAppointmentMcKey = scBlock.getAppointmentMckey();
            if (mcKey.equals(scBlockMcKey)) {
                //  充电完成任务完成
                WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, "", withWorkBlockName, loadStorage, new Date(), row, line, tier);
            } else if (mcKey.equals(scBlockAppointmentMcKey)) {
                //  充电完成任务完成
                WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishAppointmentMcKeyByPrimaryKey(blockName, "", withWorkBlockName, loadStorage, new Date(), row, line, tier);
            }
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        } else if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            // 返回原点任务 子车上车后结束
            WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, "", withWorkBlockName, loadStorage, new Date(), row, line, tier);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        } else if (WorkPlanConstant.TYPE_GET_CAR==workPlanType) {
            // 接车任务 子车上车后结束
            WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, "", withWorkBlockName, loadStorage, new Date(), row, line, tier);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            //  任务执行
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
        } else {
            WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, msgMcKey, withWorkBlockName, loadStorage, new Date(), row, line, tier);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        }
    }

    /**
     * 载货下车
     *
     * @author CalmLake
     * @date 2019/6/6 15:11
     */
    @Override
    public void carryingOutOfTheCar() throws InterruptedException {
        WcsScblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, "", true, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        BlockServiceImplFactory.blockServiceDoKey(blockName);
    }

    /**
     * 载货上车
     *
     * @author CalmLake
     * @date 2019/6/6 15:11
     */
    @Override
    public void carryingGetOnTheCar() throws InterruptedException {
        int workPlanType = workPlan.getType();
        String withWorkBlockName = scBlock.getWithWorkBlockName();
        boolean loadStorage = false;
        if (MsgCycleOrderConstant.LOAD_STATUS_HAVE.equals(loadStatus)) {
            loadStorage = true;
        }
        //  载货上车
        if (WorkPlanConstant.TYPE_MOVEMENT==workPlanType) {
            //  移库 不清除任务标识
            WcsScblockDaoImpl.getInstance().updateGoOnCarFinishByPrimaryKey(blockName, msgMcKey, withWorkBlockName, loadStorage, new Date(), row, line, tier);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        } else {
            WcsScblockDaoImpl.getInstance().updateGoOnCarFinishByPrimaryKey(blockName, "", withWorkBlockName, loadStorage, new Date(), row, line, tier);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        }
        scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String scBlockAppointmentMcKey = scBlock.getAppointmentMckey();
        if (StringUtils.isNotEmpty(scBlockAppointmentMcKey)) {
            String withWorkBlockNames = scBlock.getReserved1();
            int resultInt1 = WcsScblockDaoImpl.getInstance().updateThreeScBlock(scBlockAppointmentMcKey, "", withWorkBlockNames, blockName);
            Log4j2Util.getAssigningTaskLogger().info(String.format("穿梭车 %s 优先卸车任务分配，结果： %d", blockName, resultInt1));
            //  优先出库卸车逻辑
            if (SystemCache.OUT_STORAGE_OFF_CAR) {
                WcsWorkplanEntity workPlan1 = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",scBlockAppointmentMcKey));
                if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlan1.getType()) {
                    int resultInt = WcsScblockDaoImpl.getInstance().updateReserved2ByName(blockName, BlockConstant.SC_PRIORITY_OFF_CAR_RESERVED2);
                    Log4j2Util.getAssigningTaskLogger().info(String.format("穿梭车 %s 优先卸车标识修改，结果： %d", blockName, resultInt));
                }
            }
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        } else {
            Log4j2Util.getAssigningTaskLogger().info(String.format("穿梭车 %s 预约任务：%s", blockName, scBlockAppointmentMcKey));
        }
    }

    /**
     * 充电开始
     *
     * @author CalmLake
     * @date 2019/6/6 15:14
     */
    @Override
    public void chargingStarted() throws InterruptedException {
        int workPlanId = workPlan.getId();
        WcsScblockDaoImpl.getInstance().updateChargeStartByPrimaryKey(blockName, new Date());
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        //  修改工作计划状态
        WorkPlanService.finishWorkPlan(workPlanId, msgMcKey);
        //  自动切换备车逻辑
        StandbyCarService standbyCarService = new StandbyCarService();
        standbyCarService.changeStandbyCar(blockName);
    }

    /**
     * 充电结束
     *
     * @author CalmLake
     * @date 2019/6/6 15:14
     */
    @Override
    public void chargeEnd() throws InterruptedException {
        WcsScblockDaoImpl.getInstance().updateChargeFinishByPrimaryKey(blockName, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        BlockServiceImplFactory.blockServiceDoKey(blockName);
    }

    /**
     * 盘点（数数）
     *
     * @author CalmLake
     * @date 2019/6/6 15:17
     */
    @Override
    public void takeStock() throws InterruptedException {
        WcsScblockDaoImpl.getInstance().updateTakeStockFinishByPrimaryKey(blockName, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        //  任务完成操作
        FinishWorkPlanInterface finishWorkPlan = new FinishWorkPlanImpl();
        finishWorkPlan.finishOutStorage(workPlan);
        //  继续干活
        taskingAppointmentMcKey();
    }

    /**
     * 理货（同一巷道内货物移动）
     *
     * @author CalmLake
     * @date 2019/6/6 15:19
     */
    @Override
    public void tallying() throws InterruptedException {
        WcsScblockDaoImpl.getInstance().updateTallyFinishByPrimaryKey(blockName, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        //  任务完成操作
        FinishWorkPlanInterface finishWorkPlan = new FinishWorkPlanImpl();
        finishWorkPlan.finishOutStorage(workPlan);
        //  继续干活
        taskingAppointmentMcKey();
    }

    private void goClA() throws InterruptedException {
        WcsScblockDaoImpl.getInstance().updateOffCarToClAEmptyFinishByPrimaryKey(blockName, "", new Date(), row, line, tier, BlockConstant.BERTH_BLOCK_NAME_CLA);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);

        WcsScblockDaoImpl.getInstance().updateTwoScBlock(msgMcKey, "AL01", blockName);
        BlockServiceImplFactory.blockServiceDoKey(blockName);
    }

    private void goClB() throws InterruptedException {
        WcsScblockDaoImpl.getInstance().updateOffCarToClAEmptyFinishByPrimaryKey(blockName, "", new Date(), row, line, tier, BlockConstant.BERTH_BLOCK_NAME_CLB);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);

        String toStation = workPlan.getToStation();
        WcsScblockDaoImpl.getInstance().updateTwoScBlock(msgMcKey, toStation, blockName);
        WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(msgMcKey, blockName, toStation);
        BlockServiceImplFactory.blockServiceDoKey(blockName);
        BlockServiceImplFactory.blockServiceDoKey(toStation);
    }

    private void goAl() throws InterruptedException {
        String withWorkBlockName = scBlock.getWithWorkBlockName();
        boolean loadStorage = scBlock.getIsLoad();
        WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, msgMcKey, withWorkBlockName, loadStorage, new Date(), row, line, tier);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
    }

    private void goMc() throws InterruptedException {
        String withWorkBlockName = scBlock.getWithWorkBlockName();
        boolean loadStorage = scBlock.getIsLoad();
        String toStation = workPlan.getToStation();
        if (withWorkBlockName.equals(toStation)) {
            WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            if (blockName.equals(mcBlock.getBingScBlockName())) {
                WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, "", withWorkBlockName, loadStorage, new Date(), row, line, tier);
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            } else {
                WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, msgMcKey, withWorkBlockName, loadStorage, new Date(), row, line, tier);
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        } else {
            WcsScblockDaoImpl.getInstance().updateGoOnCarEmptyFinishByPrimaryKey(blockName, msgMcKey, withWorkBlockName, loadStorage, new Date(), row, line, tier);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        }
    }

    /**
     * 查找是否存在预约任务，存在就分配执行
     *
     * @author CalmLake
     * @date 2019/6/6 11:25
     */
    private void taskingAppointmentMcKey() {
        WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String scBlockAppointmentMcKey = scBlock.getAppointmentMckey();
        if (StringUtils.isNotEmpty(scBlockAppointmentMcKey)) {
            String withWorkBlockName = scBlock.getReserved1();
            WcsScblockDaoImpl.getInstance().updateThreeScBlock(scBlockAppointmentMcKey, "", withWorkBlockName, blockName);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        }
    }

    /**
     * 充电时，穿梭车至货架两段，向任务分配列表添加任务（堆垛机和穿梭车交互）
     *
     * @param msgMcKey          任务标识
     * @param withWorkBlockName 交互工作数据block名称
     * @param blockName         数据block名称
     * @param workPlanType      工作计划类型
     * @author CalmLake
     * @date 2019/4/10 10:11
     */
    private void createTasking(String msgMcKey, String blockName, int workPlanType, String withWorkBlockName, int mlMcNum, int wareHouseNo) {
        //  终点设备分配任务    tasking toStation 为目标终点设备
        TaskingService taskingService = new TaskingService();
        taskingService.insertNewTasking(msgMcKey, withWorkBlockName, blockName, workPlanType, workPlanType, mlMcNum, wareHouseNo, "", blockName, TaskingConstant.MACHINE_TYPE_M_SC);
    }
}
