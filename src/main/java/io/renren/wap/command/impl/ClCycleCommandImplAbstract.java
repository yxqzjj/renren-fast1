package io.renren.wap.command.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.ClCommandInterface;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.TaskingConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.MsgCycleOrderFinishReportAckService;
import io.renren.wap.service.MsgCycleOrderFinishReportService;
import io.renren.wap.service.RouteService;
import io.renren.wap.service.TaskingService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 输送线动作
 *
 * @Author: CalmLake
 * @Date: 2019/6/6  16:04
 * @Version: V1.0.0
 **/
public class ClCycleCommandImplAbstract extends AbstractMachineCycleCommand implements ClCommandInterface {

    public WcsClblockEntity clBlock;

    public ClCycleCommandImplAbstract(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
        super(msgCycleOrderFinishReportDTO);
    }

    @Override
    public void execute() throws InterruptedException {
        clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(this.blockName);
        String command = clBlock.getCommand();
        String clBlockMcKey = clBlock.getMckey();
        String clBlockAppointmentMcKey = clBlock.getAppointmentMckey();
        if (MsgCycleOrderFinishReportService.checkData(msgMcKey, clBlockMcKey, clBlockAppointmentMcKey, blockName)) {
            if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT.equals(command) || MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK.equals(command)) {
                // 当前任务已经完成 回复05
                Log4j2Util.getMsgCustomerLogger().info(String.format("再次收到35，%s", msgCycleOrderFinishReportDTO.toString()));
                MsgCycleOrderFinishReportAckService.replay05(msgCycleOrderFinishReportAckConditionDTO);
            } else {
                if (MsgCycleOrderFinishReportService.isFinishedSuccess(finishCode, finishType, blockName)) {
                    if (MsgCycleOrderConstant.CYCLE_TRANSPLANTING_PICKUP_07.equals(cycleCommand)) {
                        pickup();
                    } else if (MsgCycleOrderConstant.CYCLE_TRANSPLANTING_THE_UNLOADING_08.equals(cycleCommand)) {
                        unload();
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，未解析的指示命令，cycleCommand：%s，loadStatus：%s", blockName, cycleCommand, loadStatus));
                        MsgCycleOrderFinishReportAckService.replay05(msgCycleOrderFinishReportAckConditionDTO);
                    }
                } else {
                    WcsClblockDaoImpl.getInstance().updateBlockErrorCodeByPrimaryKey(blockName, finishCode);
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，异常完成，cycleCommand：%s,finishType：%s，finishCode：%s", blockName, cycleCommand, finishType, finishCode));
                    MsgCycleOrderFinishReportAckService.replay05(msgCycleOrderFinishReportAckConditionDTO);
                }
            }
        }else {
            MsgCycleOrderFinishReportAckService.replay05(msgCycleOrderFinishReportAckConditionDTO);
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
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        int workPlanType = workPlan.getType();
        String toStation = workPlan.getToStation();
        int resultUpdateDb = WcsClblockDaoImpl.getInstance().updateCLBlockTransplantingPickUpFinished(blockName);
        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，mcKey：%s，移栽取货处理结果：%d", blockName, msgMcKey, resultUpdateDb));
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        String station = machine.getStationName();
        if (StringUtils.isNotEmpty(station) && StringUtils.isNotEmpty(toStation) && toStation.equals(station) && WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，mcKey：%s ,station：%s 出库完成", blockName, msgMcKey, station));
        } else {
            String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
            transplantingPickUpFinishedOperation(nextBlockName, msgMcKey, blockName, workPlan.getPriorityConfigPriority(), workPlanType, toStation);
        }
    }

    /**
     * 卸货
     *
     * @author CalmLake
     * @date 2019/6/6 14:54
     */
    @Override
    public void unload() throws InterruptedException {
        int resultUpdateDb = WcsClblockDaoImpl.getInstance().updateCLBlockTransplantingTheUnloadingFinished(blockName);
        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，mcKey：%s，移栽卸货处理结果：%d", blockName, msgMcKey, resultUpdateDb));
        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
        transplantingTheUnloadingFinished(blockName);
    }

    /**
     * 移栽卸货完成
     *
     * @param blockName 设备的数据block名称
     * @author CalmLake
     * @date 2019/3/11 17:15
     */
    protected void transplantingTheUnloadingFinished(String blockName) {
        WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String clBlockAppointmentMcKey = clBlock.getAppointmentMckey();
        if (StringUtils.isNotEmpty(clBlockAppointmentMcKey)) {
            String withWorkBlockName = clBlock.getReserved1();
            WcsClblockDaoImpl.getInstance().updateThreeCLBlock(clBlockAppointmentMcKey, "", withWorkBlockName, blockName);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载卸货完成，当前无预约任务", blockName));
        }
    }

    /**
     * 移栽卸货完成
     *
     * @param blockName    设备的数据block名称
     * @param runBlockName 运行block名称
     * @author CalmLake
     * @date 2019/8/4 20:11
     */
    protected void transplantingTheUnloadingFinished(String blockName, String runBlockName) {
        WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String clBlockAppointmentMcKey = clBlock.getAppointmentMckey();
        if (StringUtils.isNotEmpty(clBlockAppointmentMcKey)) {
            String withWorkBlockName = clBlock.getReserved1();
            WcsClblockDaoImpl.getInstance().updateThreeCLBlock(clBlockAppointmentMcKey, "", withWorkBlockName, blockName);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载卸货完成，当前无预约任务", blockName));
            LockCache.getValue(runBlockName).signal();
        }
    }

    /**
     * 移载取货完成后输送线对任务的操作
     *
     * @param nextBlockName 下一交互设备的数据block名称
     * @param msgMcKey      消息中的任务标识
     * @param blockName     数据block名称
     * @param priority      工作计划优先级
     * @param workPlanType  工作计划类型
     * @author CalmLake
     * @date 2019/3/11 16:32
     */
    protected void transplantingPickUpFinishedOperation(String nextBlockName, String msgMcKey, String blockName, int priority, int workPlanType, String toStation) {
        //  当前设备分配任务  有任务  修改交互设备名称
        WcsClblockDaoImpl.getInstance().updateMcKeyCLBlock(msgMcKey, nextBlockName, blockName);
        if (nextBlockName.contains(MachineConstant.TYPE_ML) || nextBlockName.contains(MachineConstant.TYPE_RGV) || nextBlockName.contains(MachineConstant.TYPE_MC)) {
            WcsMachineEntity machine = MachineCache.getMachine(nextBlockName);
            //  放入任务分配表
            TaskingService taskingService = new TaskingService();
            taskingService.insertNewTasking(msgMcKey, nextBlockName, blockName, priority, workPlanType, TaskingConstant.ML_MC_ONE, machine.getWarehouseNo(), "", toStation, TaskingConstant.MACHINE_TYPE_CL_M);
            LockCache.getValue(nextBlockName).signal();
        } else if (nextBlockName.contains(MachineConstant.TYPE_SC)) {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，请检查路径配置信息，输送线交互设备为穿梭车：%s", blockName, nextBlockName));
        } else if (nextBlockName.contains(MachineConstant.TYPE_AL)) {
            WcsMachineEntity machine = MachineCache.getMachine(nextBlockName);
            //  放入任务分配表
            TaskingService taskingService = new TaskingService();
            taskingService.insertNewTasking(msgMcKey, nextBlockName, blockName, priority, workPlanType, TaskingConstant.ML_MC_ONE, machine.getWarehouseNo(), "", toStation, TaskingConstant.MACHINE_TYPE_CL_M);
            LockCache.getValue(nextBlockName).signal();
        } else {
            // 下一设备为输送线
            WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(nextBlockName);
            String clMcKey = clBlock.getMckey();
            String clAppointmentMcKey = clBlock.getAppointmentMckey();
            if (StringUtils.isEmpty(clMcKey) && StringUtils.isEmpty(clAppointmentMcKey)) {
                //  下一设备设备无任务 无预约任务 分配任务
                WcsClblockDaoImpl.getInstance().updateMcKeyCLBlock(msgMcKey, blockName, nextBlockName);
                //   输送线  制作设备动作消息
                BlockServiceImplFactory.blockServiceDoKey(blockName);
                BlockServiceImplFactory.blockServiceDoKey(nextBlockName);
            } else if (StringUtils.isNotEmpty(clMcKey) && StringUtils.isEmpty(clAppointmentMcKey)) {
                if (!clMcKey.equals(msgMcKey)) {
                    //  1.当前下一设备有任务无预约任务 分配预约任务
                    WcsClblockDaoImpl.getInstance().updateAppointmentMcKeyCLBlock(msgMcKey, blockName, nextBlockName);
                    //  制作自己动作消息
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else if (StringUtils.isNotEmpty(clMcKey) && StringUtils.isNotEmpty(clAppointmentMcKey)) {
                SleepUtil.sleep(2);
                transplantingPickUpFinishedOperation(nextBlockName, msgMcKey, blockName, priority, workPlanType, toStation);
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，该设备的条件不满足，McKey：%s,AppointmentMcKey：%s", blockName, clMcKey, clAppointmentMcKey));
            }
        }
    }
}
