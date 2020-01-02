package io.renren.wap.command.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsAlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMcblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsAlblockEntity;
import io.renren.modules.generator.entity.WcsMcblockEntity;
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
 * 母车动作完成消息处理
 *
 * @Author: CalmLake
 * @date 2019/8/15  9:31
 * @Version: V1.0.0
 **/
public class McCycleCommandImplAbstract extends AbstractMachineCycleCommand implements MlMcAlCommandInterface {
    private boolean isLoad;
    private boolean loadStorage;
    private boolean loadStorageByte;
    private String loadCar;
    private TransferTasksService transferTasksService = new TransferTasksService();

    public McCycleCommandImplAbstract(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
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
        WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String command = mcBlock.getCommand();
        String lockMcKey = mcBlock.getMckey();
        String blockAppointmentMcKey = mcBlock.getAppointmentMckey();
        String withWorkBlockName = mcBlock.getWithWorkBlockName();
        isLoad = mcBlock.getIsLoad();
        loadCar = null;
        switch (loadStatus) {
            case MsgCycleOrderConstant.LOAD_STATUS_NONE:
                loadStorage = false;
                loadStorageByte=false;
                loadCar = "";
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVE:
                loadStorage = true;
                loadStorageByte=true;
                loadCar = "";
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVA_CAR:
                loadStorage = false;
                loadStorageByte=false;
                if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
                    loadCar = withWorkBlockName;
                } else {
                    loadCar = StandbyCarService.getScBlockName(mcBlock);
                }
                break;
            case MsgCycleOrderConstant.LOAD_STATUS_HAVE_CAR_LOAD:
                loadStorage = true;
                loadStorageByte=true;
                if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
                    loadCar = withWorkBlockName;
                } else {
                    loadCar = StandbyCarService.getScBlockName(mcBlock);
                }
                break;
            default:
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s,未知状态信息: %s", blockName, loadStatus));
                throw new IllegalStateException("未知状态信息: " + loadStatus);
        }
        if (MsgCycleOrderFinishReportService.checkData(msgMcKey, lockMcKey, blockAppointmentMcKey, blockName)) {
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
        // 移载取货
        WcsMcblockDaoImpl.getInstance().updateBlockTransplantingPickUpFinished(blockName, loadCar);
        if (StringUtils.isNotEmpty(loadCar)) {
            int result = WcsScblockDaoImpl.getInstance().updateLoad(loadCar, loadStorage);
            Log4j2Util.getMsgCustomerLogger().info(String.format("blockName：%s，%s 移栽取货,载荷更改结果：%d", loadCar, blockName, result));
        }
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        transferTasksService.transplantingPickUpFinishedTransferTasks(blockName, msgMcKey, MachineConstant.BYTE_TYPE_MC);
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
        WcsMcblockDaoImpl.getInstance().updateBlockTransplantingTheUnloadingFinished(blockName, loadCar);
        if (StringUtils.isNotEmpty(loadCar)) {
            WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
            if (WorkPlanConstant.TYPE_MOVEMENT==workPlan.getType()) {
                // 载车
                int result = WcsScblockDaoImpl.getInstance().updateLoadMcKey(loadCar, loadStorage, "");
                Log4j2Util.getMsgCustomerLogger().info(String.format("blockName：%s，%s 移栽卸货,移库任务，清除穿梭车任务标识，载荷更改结果：%d", loadCar, blockName, result));
            } else {
                // 载车
                int result = WcsScblockDaoImpl.getInstance().updateLoad(loadCar, loadStorage);
                Log4j2Util.getMsgCustomerLogger().info(String.format("blockName：%s，%s 移栽卸货,载荷更改结果：%d", loadCar, blockName, result));
            }
        }
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        transferTasksService.transplantingTheUnloadingFinishedTransferTasks(blockName);
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
        String dock1 = "0101";
        String dock2 = "0102";
        String dock3 = "0103";
        String dock = msgCycleOrderFinishReportDTO.getDock();
        String row = msgCycleOrderFinishReportDTO.getRow();
        String line = msgCycleOrderFinishReportDTO.getLine();
        String tier = msgCycleOrderFinishReportDTO.getTier();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        String fromStation = workPlan.getFromStation();
        String toStation = workPlan.getToStation();
        int workPlanType = workPlan.getType();
        if (StringUtils.isNotEmpty(loadCar)) {
            //  载车
            int result = WcsScblockDaoImpl.getInstance().updateLocationLoadByPrimaryKey(loadCar, row, line, tier, loadStorage);
            Log4j2Util.getMsgCustomerLogger().info(String.format("blockName：%s移动完成，子车 %s  信息修改结果：%d", blockName, loadCar, result));
        }
        WcsMcblockDaoImpl.getInstance().updateMoveFinishByPrimaryKey(blockName, dock, row, line, tier, loadStorage, loadCar);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        //  回原点任务
        if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            //  移动 当且仅当载车且处于默认位置时完成任务
            if (StringUtils.isNotEmpty(loadCar) && StringUtils.isNotEmpty(dock) && dock.equals(workPlan.getToStation())) {
                DbUtil.getProcedureOrcaleDao().spUpdateBlockScblockClearMckeyIn(blockName, loadCar, MachineConstant.BYTE_TYPE_MC);
                WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
                LockCache.getValue(blockName).signal();
            }
        }
        if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType || WorkPlanConstant.TYPE_MOVEMENT==workPlanType) {
            if (!fromStation.equals(toStation)) {
                //  创建提升机移动任务
                if (dock1.equals(dock) || dock2.equals(dock) || dock3.equals(dock)) {
                    WcsAlblockEntity alBlock = WcsAlblockDaoImpl.getInstance().selectByPrimaryKey("AL01");
                    String alBlockBerthBlockName = alBlock.getBerthBlockName();
                    if (!dock.equals(alBlockBerthBlockName)) {
                        int countNum = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                                .eq("To_Station","AL01")
                                .eq("From_Station","AL01")
                                .eq("Status",WorkPlanConstant.STATUS_WAIT).or().eq("Status",WorkPlanConstant.STATUS_WORKING)
                        );
                        if (countNum < 1) {
                            WcsWorkplanEntity workPlan1 = WorkPlanService.createWorkPlan("", "0001", "000000000", dock, "0001", WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION, "AL01", "000000000");
                            AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan1);
                            assigningTaskService.assigningTasks();
                        }
                    }
                }
            }
        }
        //  传递任务发送消息
        transferTasksService.moveFinishTransferTasks(blockName);
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
        WcsMcblockDaoImpl.getInstance().updateGetCarFinishByPrimaryKey(blockName, loadCar, loadStorage);
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        transferTasksService.getCarFinishTransferTasks(msgMcKey, blockName, loadCar, loadStatus, MachineConstant.BYTE_TYPE_MC);
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
        if (MsgCycleOrderConstant.LOAD_STATUS_NONE.equals(loadStatus) && StringUtils.isEmpty(loadCar) && isLoad) {
            //  卸车卸货
            WcsMcblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, "", "", loadCar, loadStorage);
            MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            WcsMcblockEntity mCBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
            String blockMcKey = mCBlock.getMckey();
            String blockAppointmentMcKey = mCBlock.getAppointmentMckey();
            String appointmentBlockName = mCBlock.getReserved1();
            if (StringUtils.isNotEmpty(blockMcKey)) {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else if (StringUtils.isNotEmpty(blockAppointmentMcKey)) {
                WcsMcblockDaoImpl.getInstance().updateThreeValueMLBlock(blockAppointmentMcKey, "", appointmentBlockName, blockName);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：卸车卸货，当前设备无任务", blockName));
                if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                    if (SystemCache.AUTO_GET_CAR) {
                        WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan(McKeyUtil.getMcKey(), "0000", "000000000", mCBlock.getBingScBlockName(), "0000", WorkPlanConstant.TYPE_GET_CAR, blockName, "000000000");
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
                int resultInt = WcsMcblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, "", "", "", loadStorage);
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：卸车完成，工作计划类型：%d，修改结果：%d", blockName, workPlanType, resultInt));
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
                    // 卸车任务 子车下车后结束 工作计划完成
                    WorkPlanService.finishWorkPlan(workPlanId, msgMcKey);
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
                String scBlockName = workPlan.getReserved2();
                WcsMcblockEntity mCBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                //  任务结束 清除任务
                if (scBlockName.equals(mCBlock.getWithWorkBlockName())) {
                    int resultInt = WcsMcblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, "", "", "", loadStorage);
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：卸车完成，工作计划类型：%d，修改结果：%d", blockName, workPlanType, resultInt));
                    MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                } else {
                    int resultInt = WcsMcblockDaoImpl.getInstance().updateOffCarFinishByPrimaryKey(blockName, msgMcKey, scBlockName, "", loadStorage);
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：卸车完成，工作计划类型：%d，修改结果：%d", blockName, workPlanType, resultInt));
                    MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else {
                WcsMcblockDaoImpl.getInstance().updateOffCarFinish2ByPrimaryKey(blockName, loadStorage);
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
            LockCache.getValue(blockName).signal();
        }
    }
}
