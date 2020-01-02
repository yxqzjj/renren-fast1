package io.renren.wap.command.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsRgvblockDaoImpl;
import io.renren.modules.generator.entity.WcsClblockEntity;
import io.renren.modules.generator.entity.WcsRgvblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgCycleOrderFinishReportDTO;
import io.renren.wap.command.AbstractMachineCycleCommand;
import io.renren.wap.command.MlMcAlCommandInterface;
import io.renren.wap.service.MachineService;
import io.renren.wap.service.MsgCycleOrderFinishReportAckService;
import io.renren.wap.service.MsgCycleOrderFinishReportService;
import io.renren.wap.service.RouteService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * rgv动作消息处理
 *
 * @Author: CalmLake
 * @date 2019/8/15  11:11
 * @Version: V1.0.0
 **/
public class RgvCycleCommandImplAbstract extends AbstractMachineCycleCommand implements MlMcAlCommandInterface {
    public WcsRgvblockEntity rgvBlock;
    public boolean isLoad;
    private boolean loadStorage;

    public RgvCycleCommandImplAbstract(MsgCycleOrderFinishReportDTO msgCycleOrderFinishReportDTO) {
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
        String plcName = msgCycleOrderFinishReportDTO.getPlcName();
        String blockName = msgCycleOrderFinishReportDTO.getMachineName();
        String msgMcKey = msgCycleOrderFinishReportDTO.getMcKey();
        String cycleCommand = msgCycleOrderFinishReportDTO.getCycleCommand();
        String finishType = msgCycleOrderFinishReportDTO.getFinishType();
        String finishCode = msgCycleOrderFinishReportDTO.getFinishCode();
        String loadStatus = msgCycleOrderFinishReportDTO.getLoadStatus();
        String station = msgCycleOrderFinishReportDTO.getStation();
        WcsRgvblockEntity block = WcsRgvblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String command = block.getCommand();
        String blockMcKey = block.getMckey();
        String blockAppointmentMcKey = block.getAppointmentMckey();
        if (MsgCycleOrderFinishReportService.checkData(msgMcKey, blockMcKey, blockAppointmentMcKey, blockName)) {
            if (MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT.equals(command) || MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK.equals(command)) {
                // 回复05
                MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
            } else {
                if (MsgCycleOrderFinishReportService.isFinishedSuccess(finishCode, finishType, blockName)) {
                    WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
                    String toStation = workPlan.getToStation();
                    if (MsgCycleOrderConstant.CYCLE_TRANSPLANTING_PICKUP_07.equals(cycleCommand) && MsgCycleOrderConstant.LOAD_STATUS_HAVE.equals(loadStatus)) {
                        // 移载取货
                        WcsRgvblockDaoImpl.getInstance().updateBlockTransplantingPickUpFinished(blockName);
                        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                        // 继续新任务
                        String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                        if (MachineService.isClMachine(nextBlockName)) {
                            WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(nextBlockName);
                            String clMcKey = clBlock.getMckey();
                            String clAppointmentMcKey = clBlock.getAppointmentMckey();
                            //  当前设备分配任务  当前有任务  修改交互设备名称
                            WcsRgvblockDaoImpl.getInstance().updateTwoRgvBlock(msgMcKey, nextBlockName, blockName);
                            //  制作动作消息
                            BlockServiceImplFactory.blockServiceDoKey(blockName);
                            if (StringUtils.isEmpty(clMcKey) && StringUtils.isEmpty(clAppointmentMcKey)) {
                                //  下一设备设备无任务 无预约任务 分配任务
                                WcsClblockDaoImpl.getInstance().updateMcKeyByName(msgMcKey, blockName, nextBlockName);
                                //   输送线  制作动作消息
                                BlockServiceImplFactory.blockServiceDoKey(blockName);
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
                    } else if (MsgCycleOrderConstant.CYCLE_TRANSPLANTING_THE_UNLOADING_08.equals(cycleCommand) && MsgCycleOrderConstant.LOAD_STATUS_NONE.equals(loadStatus)) {
                        WcsRgvblockDaoImpl.getInstance().updateBlockTransplantingTheUnloadingFinished(blockName);
                        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                        block = WcsRgvblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                        blockAppointmentMcKey = block.getAppointmentMckey();
                        if (StringUtils.isNotEmpty(blockAppointmentMcKey)) {
                            String withWorkBlockName = block.getReserved1();
                            WcsRgvblockDaoImpl.getInstance().updateThreeRgvBlock(blockAppointmentMcKey, "", withWorkBlockName, blockName);
                            BlockServiceImplFactory.blockServiceDoKey(blockName);
                        } else {
                            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载卸货完成，当前无预约任务", blockName));
                        }
                    } else if (MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_NOLOAD_04.equals(cycleCommand)) {
                        // 移动
                        WcsRgvblockDaoImpl.getInstance().updateMoveFinishByPrimaryKey(blockName, station);
                        if (MachineService.isClMachine(station)) {
                            WcsClblockDaoImpl.getInstance().updateBerthBlockNameByPrimaryKey(blockName, station);
                        }
                        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                        BlockServiceImplFactory.blockServiceDoKey(blockName);
                        block = WcsRgvblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                        blockMcKey = block.getMckey();
                        String withWorkBlockName = block.getWithWorkBlockName();
                        if (StringUtils.isNotEmpty(blockMcKey)) {
                            BlockServiceImplFactory.blockServiceDoKey(blockName);
                            if (MachineService.isClMachine(withWorkBlockName)) {
                                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
                            } else {
                                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，制作队列消息时未解析的交互设备类型，withWorkBlockName：%s", blockName, withWorkBlockName));
                            }
                        } else {
                            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移动完成，当前设备无任务", blockName));
                        }
                    } else if (MsgCycleOrderConstant.CYCLE_COMMAND_MOVE_LOAD_11.equals(cycleCommand)) {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，载货移动暂不使用，cycleCommand：%s", blockName, cycleCommand));
                        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，未解析的指示命令，cycleCommand：%s", blockName, cycleCommand));
                        MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                    }
                } else {
                    WcsRgvblockDaoImpl.getInstance().updateBlockErrorCodeByPrimaryKey(blockName, finishCode);
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，异常完成，cycleCommand：%s,finishType：%s，finishCode：%s", blockName, cycleCommand, finishType, finishCode));
                    MsgCycleOrderFinishReportAckService.replay05AndUpdateBlock(msgCycleOrderFinishReportAckConditionDTO);
                }
            }
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

    }
}
