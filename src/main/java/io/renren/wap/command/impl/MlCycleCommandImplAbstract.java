package io.renren.wap.command.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.MlMcAlCommandInterface;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.*;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.McKeyUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 堆垛机动作
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  16:51
 * @Version: V1.0.0
 **/
public class MlCycleCommandImplAbstract extends AbstractMachineCycleCommand implements MlMcAlCommandInterface {
    public WcsMlblockEntity mlBlock;
    public boolean isLoad;
    private boolean loadStorageBool;
    private boolean loadStorage;
    private String loadCar;
    /**
     * 任务传递消息发送使者
     */
    private TransferTasksService transferTasksService = new TransferTasksService();

    public MlCycleCommandImplAbstract(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
        super(msgCycleOrderFinishReportDTO);
    }

    @Override
    public void execute() throws InterruptedException {
        mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String command = mlBlock.getCommand();
        String mlBlockMcKey = mlBlock.getMckey();
        String mlBlockAppointmentMcKey = mlBlock.getAppointmentMckey();
        String withWorkBlockName = mlBlock.getWithWorkBlockName();
        isLoad = mlBlock.getIsLoad();
        loadCar = null;
        switch (loadStatus) {
            case MsgCycleOrderConstant.LOAD_STATUS_NONE:
                loadStorage = false;
                loadStorageBool=false;
                loadCar = "";
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVE:
                loadStorage = true;
                loadStorageBool=true;
                loadCar = "";
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVA_CAR:
                loadStorage = false;
                loadStorageBool=false;
                if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
                    loadCar = withWorkBlockName;
                } else {
                    loadCar = StandbyCarService.getScBlockName(mlBlock);
                }
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVE_CAR_LOAD:
                loadStorage = true;
                loadStorageBool=true;
                if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
                    loadCar = withWorkBlockName;
                } else {
                    loadCar = StandbyCarService.getScBlockName(mlBlock);
                }
                break;
            default:
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,未知状态信息: %s", blockName, loadStatus));
                throw new IllegalStateException("未知状态信息: " + loadStatus);
        }
        if (MsgCycleOrderFinishReportService.checkData(msgMcKey, mlBlockMcKey, mlBlockAppointmentMcKey, blockName)) {
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
     * @author CalmLake
     * @date 2019/6/6 14:54
     */
    @Override
    public void pickup() throws InterruptedException {
        // 移载取货
        WcsMlblockDaoImpl.getInstance().updateBlockTransplantingPickUpFinished(blockName, loadCar);
        if (StringUtils.isNotEmpty(loadCar)) {
            int result = WcsScblockDaoImpl.getInstance().updateLoad(loadCar, loadStorageBool);
            Log4j2Util.getMsgCustomerLogger().info(String.format("blockName：%s，%s 移栽取货,载荷更改结果：%d", loadCar, blockName, result));
        }
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        transferTasksService.mlTransplantingPickUpFinishedTransferTasks(blockName, msgMcKey);
    }

    /**
     * 卸货
     *
     * @author CalmLake
     * @date 2019/6/6 14:54
     */
    @Override
    public void unload() throws InterruptedException {
        WcsMlblockDaoImpl.getInstance().updateBlockTransplantingTheUnloadingFinished(blockName, loadCar);
        if (StringUtils.isNotEmpty(loadCar)) {
            // 载车
            int result = WcsScblockDaoImpl.getInstance().updateLoad(loadCar, loadStorageBool);
            Log4j2Util.getMsgCustomerLogger().info(String.format("blockName：%s，%s 移栽卸货,载荷更改结果：%d", loadCar, blockName, result));
        }
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        transferTasksService.mlTransplantingTheUnloadingFinishedTransferTasks(blockName);
    }

    /**
     * 移动
     *
     * @author CalmLake
     * @date 2019/6/6 14:55
     */
    @Override
    public void move() throws InterruptedException {
        String dock = msgCycleOrderFinishReportDTO.getDock();
        String row = msgCycleOrderFinishReportDTO.getRow();
        String line = msgCycleOrderFinishReportDTO.getLine();
        String tier = msgCycleOrderFinishReportDTO.getTier();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        int workPlanType = workPlan.getType();
        if (StringUtils.isNotEmpty(loadCar)) {
            //  载车
            WcsScblockDaoImpl.getInstance().updateLocationLoadByPrimaryKey(loadCar, row, line, tier, loadStorageBool);
        }
        WcsMlblockDaoImpl.getInstance().updateMoveFinishByPrimaryKey(blockName, dock, row, line, tier, loadStorage, loadCar);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        //  回原点任务
        if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            //  移动 当且仅当载车且处于默认位置时完成任务
            if (StringUtils.isNotEmpty(loadCar) && StringUtils.isNotEmpty(dock) && dock.equals(workPlan.getToStation())) {
                DbUtil.getProcedureOrcaleDao().spUpdateBlockScblockClearMckeyIn(blockName, loadCar,MachineConstant.BYTE_TYPE_ML);
                WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
                LockCache.getValue(blockName).signal();
            }
        }
        //  传递任务发送消息
        transferTasksService.mlMoveFinishTransferTasks(blockName);
    }

    /**
     * 接车
     *
     * @author CalmLake
     * @date 2019/6/6 14:57
     */
    @Override
    public void pickUpTheCar() throws InterruptedException {
        WcsMlblockDaoImpl.getInstance().updateGetCarFinishByPrimaryKey(blockName, loadCar, loadStorage);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        transferTasksService.mlGetCarFinishTransferTasks(msgMcKey, blockName, loadCar, loadStatus);
    }

    /**
     * 卸车
     *
     * @author CalmLake
     * @date 2019/6/6 15:03
     */
    @Override
    public void unloadTheCar() throws InterruptedException {
        if (MsgCycleOrderConstant.LOAD_STATUS_NONE.equals(loadStatus) && StringUtils.isEmpty(loadCar) && isLoad) {
            //  卸车卸货
            WcsMlblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, "", "", loadCar, loadStorage);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
            String mlBlockMcKey = mlBlock.getMckey();
            String mlBlockAppointmentMcKey = mlBlock.getAppointmentMckey();
            String appointmentBlockName = mlBlock.getReserved1();
            if (StringUtils.isNotEmpty(mlBlockMcKey)) {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else if (StringUtils.isNotEmpty(mlBlockAppointmentMcKey)) {
                WcsMlblockDaoImpl.getInstance().updateThreeValueMLBlock(mlBlockAppointmentMcKey, "", appointmentBlockName, blockName);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：卸车卸货，当前设备无任务", blockName));
                if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                    if (SystemCache.AUTO_GET_CAR) {
                        WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan(McKeyUtil.getMcKey(), "0000", "000000000", mlBlock.getBingScBlockName(), "0000", WorkPlanConstant.TYPE_GET_CAR, blockName, "000000000");
                        assert workPlan != null;
                        AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                        assigningTaskService.assigningTasks();
                    } else {
                        LockCache.getValue(blockName).signal();
                    }
                } else {
                    LockCache.getValue(blockName).signal();
                }
            }
        } else {
            WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
            int workPlanType = workPlan.getType();
            int workPlanId = workPlan.getId();
            //  卸车
            if (WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType || WorkPlanConstant.TYPE_CHARGE_UP==workPlanType || WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType || WorkPlanConstant.TYPE_TALLY==workPlanType || WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
                //  任务结束 清除任务
                WcsMlblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, "", "", "", loadStorage);
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
                    // 卸车任务 子车下车后结束 工作计划完成
                    WorkPlanService.finishWorkPlan(workPlanId, msgMcKey);
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else {
                WcsMlblockDaoImpl.getInstance().updateOffCarFinish2ByPrimaryKey(blockName, loadStorage);
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
            LockCache.getValue(blockName).signal();
        }
    }
}
