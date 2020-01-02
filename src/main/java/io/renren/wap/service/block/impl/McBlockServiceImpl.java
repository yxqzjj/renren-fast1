package io.renren.wap.service.block.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsMcblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsMcblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.BlockMsgSendService;
import io.renren.wap.service.RouteService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.service.block.AppointmentMcKeyService;
import io.renren.wap.service.block.OperationKeyService;
import io.renren.wap.service.msg.McMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 母车处理逻辑
 *
 * @Author: CalmLake
 * @Date: 2019/1/16  11:16
 * @Version: V1.0.0
 **/
public class McBlockServiceImpl extends BlockService implements OperationKeyService, AppointmentMcKeyService {

    @Override
    public void operationKey(String blockName) throws InterruptedException {
        WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (isCanSendMsg(mcBlock)) {
            int keyType = getKeyType(mcBlock);
            boolean resultSend;
            MsgDTO msgDTO;
            BlockMsgSendService blockMsgSendService = new BlockMsgSendService();
            if (BlockConstant.KEY_NOT_EMPTY == keyType) {
                msgDTO = appointmentMcKey(mcBlock);
            } else {
                msgDTO = doWork(mcBlock.getMckey(), mcBlock);
            }
            resultSend = blockMsgSendService.sendMsg(msgDTO, blockName);
            if (resultSend) {
                int resultUpdateDb = updateCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, WcsMcblockDaoImpl.getInstance(), blockName);
                Log4j2Util.getBlockBrickLogger().info(String.format("blockName:%s,修改指令 %s 结果 %d", blockName, MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, resultUpdateDb));
                blockMsgSendService.resendMsg(msgDTO, blockName);
            }
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s,当前设备不满足消息制作条件！", blockName));
        }
    }

    @Override
    public MsgDTO appointmentMcKey(Object object) {
        MsgDTO msgDTO;
        McMsgService mcMsgService = new McMsgService();
        WcsMcblockEntity block = (WcsMcblockEntity) object;
        boolean isLoad = block.getIsLoad();
        String berthBlockName = block.getBerthBlockName();
        String withWorkBlockName = block.getWithWorkBlockName();
        String scBlockName = block.getScBlockName();
        String appointmentWithWorkBlockName = block.getReserved1();
        String mcKey = block.getMckey();
        String appointmentMcKey = block.getAppointmentMckey();
        WcsWorkplanEntity workPlanMcKey = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanMcKeyType = workPlanMcKey.getType();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",appointmentMcKey));
        String fromLocation = workPlan.getFromLocation();
        Integer workPlanType = workPlan.getType();
        if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanMcKeyType && WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(appointmentWithWorkBlockName);
            String rowSc = block.getRow();
            String lineSc = block.getLine();
            String tierSc = block.getTier();
            String hostBlockName = scBlock.getHostBlockName();
            if (isSameSite(rowSc, lineSc, tierSc, fromLocation) && StringUtils.isEmpty(hostBlockName) && StringUtils.isEmpty(scBlockName)) {
                //  子车已到达目标巷道
                msgDTO = doWork(mcKey, block);
            } else {
                //  堆垛机载车载货
                if (isLoad && StringUtils.isNotEmpty(scBlockName) && appointmentWithWorkBlockName.equals(scBlockName)) {
                    WcsMachineEntity machineWithWork = MachineCache.getMachine(withWorkBlockName);
                    if (berthBlockName.equals(machineWithWork.getDockName())) {
                        msgDTO = doWork(mcKey, block);
                    } else {
                        String row = block.getRow();
                        String line = block.getLine();
                        String tier = block.getTier();
                        int rowInt2 = Integer.parseInt(fromLocation.substring(0, 3));
                        int lineInt2 = Integer.parseInt(fromLocation.substring(3, 6));
                        int tierInt2 = Integer.parseInt(fromLocation.substring(6, 9));
                        String rowString = StringUtils.leftPad(Integer.toString(rowInt2), 2, "0");
                        String lineString = StringUtils.leftPad(Integer.toString(lineInt2), 2, "0");
                        String tierString = StringUtils.leftPad(Integer.toString(tierInt2), 2, "0");
                        //  子车是否到达目标巷道
                        if (isSameSite(row, line, tier, fromLocation)) {
                            if (SystemCache.ML_REMOVE) {
                                if (block.getIsMove()) {
                                    // 卸车
                                    msgDTO = mcMsgService.offCar(workPlanType, appointmentMcKey, block.getName(), appointmentWithWorkBlockName, rowString, lineString, tierString);
                                } else {
                                    msgDTO = mcMsgService.move(appointmentMcKey, block.getName(), rowString, lineString, tierString);
                                }
                            } else {
                                msgDTO = mcMsgService.offCar(workPlanType, appointmentMcKey, block.getName(), appointmentWithWorkBlockName, rowString, lineString, tierString);
                            }
                        } else {
                            msgDTO = mcMsgService.move(appointmentMcKey, block.getName(), rowString, lineString, tierString);
                        }
                    }
                } else {
                    msgDTO = doWork(mcKey, block);
                }
            }
        } else {
            msgDTO = doWork(mcKey, block);
        }
        return msgDTO;
    }

    /**
     * 根据任务类型进行不同的逻辑处理
     *
     * @param mcKey   mcKey
     * @param mcBlock 母车运行状态
     * @return MsgDTO 消息
     * @author CalmLake
     * @date 2019/1/10 15:10
     */
    private MsgDTO doWork(String mcKey, WcsMcblockEntity mcBlock) {
        MsgDTO msgDTO = null;
        McMsgService mcMsgService = new McMsgService();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanType = workPlan.getType();
        String blockName = mcBlock.getName();
        String row = mcBlock.getRow();
        String line = mcBlock.getLine();
        String tier = mcBlock.getTier();
        String withWorkBlockName = mcBlock.getWithWorkBlockName();
        String toLocation = workPlan.getToLocation();
        String fromLocation = workPlan.getFromLocation();
        if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
            String scStatus = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName).getStatus();
            if (!isScStatusCanWork(scStatus, workPlanType)) {
                Log4j2Util.getBlockBrickLogger().info(String.format("blockName: %s,当前设备状态及工作计划类型匹配，block：%s，status：%s，workPlanType：%d", blockName, withWorkBlockName, scStatus, workPlanType));
                return null;
            }
        }
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
            if (mcBlock.getIsLoad()) {
                msgDTO = getCarAndPutCarToInStorage(mcMsgService, workPlanType, toLocation, mcBlock, mcKey);
            } else {
                WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
                if (StringUtils.isNotEmpty(mcBlock.getBerthBlockName()) && machine.getDockName().equals(mcBlock.getBerthBlockName())) {
                    // 取货
                    msgDTO = mcMsgService.transplantingPickUp(workPlan, blockName, withWorkBlockName);
                } else {
                    // 移动向该设备（输送线）
                    msgDTO = mcMsgService.move(workPlan, blockName, machine.getBlockName(), machine.getDockName());
                }
            }
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            if (mcBlock.getIsLoad()) {
                 WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
                if (StringUtils.isNotEmpty(mcBlock.getBerthBlockName()) && machine.getDockName().equals(mcBlock.getBerthBlockName())) {
                    // 卸货
                    msgDTO = mcMsgService.transplantingTheUnloading(workPlan, blockName, withWorkBlockName);
                } else {
                    // 移动向该设备（输送线）
                    msgDTO = mcMsgService.move(workPlan, blockName, machine.getBlockName(), machine.getDockName());
                }
            } else {
                msgDTO = getCarAndPutCarToInStorage(mcMsgService, workPlanType, fromLocation, mcBlock, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_MOVEMENT==workPlanType) {
            if (mcBlock.getIsLoad()) {
                if (withWorkBlockName.contains(MachineConstant.TYPE_CL)) {
                     WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
                    if (StringUtils.isNotEmpty(mcBlock.getBerthBlockName()) && machine.getDockName().equals(mcBlock.getBerthBlockName())) {
                        // 卸货
                        msgDTO = mcMsgService.transplantingTheUnloading(workPlan, blockName, withWorkBlockName);
                    } else {
                        // 移动向该设备（输送线）
                        msgDTO = mcMsgService.move(workPlan, blockName, machine.getBlockName(), machine.getDockName());
                    }
                } else {
                    msgDTO = getCarAndPutCarToInStorage(mcMsgService, workPlanType, toLocation, mcBlock, mcKey);
                }
            } else {
                if (withWorkBlockName.contains(MachineConstant.TYPE_CL)) {
                     WcsMachineEntity machine = MachineCache.getMachine(withWorkBlockName);
                    if (StringUtils.isNotEmpty(mcBlock.getBerthBlockName()) && machine.getDockName().equals(mcBlock.getBerthBlockName())) {
                        // 取货
                        msgDTO = mcMsgService.transplantingPickUp(workPlan, blockName, withWorkBlockName);
                    } else {
                        // 移动向该设备（输送线）
                        msgDTO = mcMsgService.move(workPlan, blockName, machine.getBlockName(), machine.getDockName());
                    }
                } else {
                    msgDTO = getCarAndPutCarToInStorage(mcMsgService, workPlanType, fromLocation, mcBlock, mcKey);
                }
            }
        } else if (WorkPlanConstant.TYPE_TALLY==workPlanType) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            if (!isSameArea(scBlock.getRow(), scBlock.getLine(), scBlock.getTier(), toLocation)) {
                // 子车不在目标巷道
                msgDTO = getCarAndPutCarToInStorage(mcMsgService, workPlanType, toLocation, mcBlock, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
            String scBlockNameChange = workPlan.getReserved2();
            String workPlanFromStation = workPlan.getFromStation();
            if (StringUtils.isEmpty(mcBlock.getScBlockName())) {
                WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
                if (BlockConstant.STATUS_BAN.equals(scBlock.getStatus())){
                    String bingScBlockName = mcBlock.getBingScBlockName();
                    if (!bingScBlockName.equals(withWorkBlockName)){
                        String location = "001001001";
                        if (withWorkBlockName.equals("SC02")){
                            location=SystemCache.Car_Location_2;
                        }
                        if (withWorkBlockName.equals("SC03")){
                            location=SystemCache.Car_Location_3;
                        }
                        int rowInt2 = Integer.parseInt(location.substring(0, 3));
                        int lineInt2 = Integer.parseInt(location.substring(3, 6));
                        int tierInt2 = Integer.parseInt(location.substring(6, 9));
                        String rowString = StringUtils.leftPad(Integer.toString(rowInt2), 2, "0");
                        String lineString = StringUtils.leftPad(Integer.toString(lineInt2), 2, "0");
                        String tierString = StringUtils.leftPad(Integer.toString(tierInt2), 2, "0");
                        if (isSameSite(row, line, tier, location)) {
                            // 接车
                            msgDTO = mcMsgService.getCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), rowString, lineString, tierString);
                        } else {
                            // 移动向货位
                            msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), rowString, lineString, tierString);
                        }
                    }else {
                        if (scBlockNameChange.equals(withWorkBlockName)) {
                            String berthBlockName = mcBlock.getBerthBlockName();
                             WcsMachineEntity machine = MachineCache.getMachine(blockName);
                            if (!berthBlockName.equals(machine.getDefaultLocation())) {
                                String toStation = machine.getDefaultLocation();
                                String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                                msgDTO = mcMsgService.move(workPlan, blockName, nextBlockName, toStation);
                            } else {
                                msgDTO = mcMsgService.getCar(workPlanType, mcKey, blockName, withWorkBlockName, MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER, MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER, MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
                            }
                        }
                    }
                }else {
                    if (scBlockNameChange.equals(withWorkBlockName)) {
                        String berthBlockName = mcBlock.getBerthBlockName();
                         WcsMachineEntity machine = MachineCache.getMachine(blockName);
                        if (!machine.getDefaultLocation().equals(berthBlockName)) {
                            String toStation = machine.getDefaultLocation();
                            String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                            msgDTO = mcMsgService.move(workPlan, blockName, nextBlockName, toStation);
                        } else {
                            msgDTO = mcMsgService.getCar(workPlanType, mcKey, blockName, withWorkBlockName, scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                        }
                    }
                }
            } else {
                if (scBlockNameChange.equals(withWorkBlockName)) {
                    String berthBlockName = mcBlock.getBerthBlockName();
                     WcsMachineEntity machine = MachineCache.getMachine(blockName);
                    String bingScBlockName = mcBlock.getBingScBlockName();
                    if (blockName.equals(workPlanFromStation)) {
                        if (!machine.getDefaultLocation().equals(berthBlockName)) {
                            String toStation = machine.getDefaultLocation();
                            String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                            msgDTO = mcMsgService.move(workPlan, blockName, nextBlockName, toStation);
                        } else {
                            msgDTO = mcMsgService.offCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER, MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER, MsgCycleOrderConstant.DEFAULT_ROW_LINE_TIER);
                        }
                    } else {
                        if (!bingScBlockName.equals(withWorkBlockName)){
                            String location = "001001001";
                            if (withWorkBlockName.equals("SC02")){
                                location=SystemCache.Car_Location_2;
                            }
                            if (withWorkBlockName.equals("SC03")){
                                location=SystemCache.Car_Location_3;
                            }
                            int rowInt2 = Integer.parseInt(location.substring(0, 3));
                            int lineInt2 = Integer.parseInt(location.substring(3, 6));
                            int tierInt2 = Integer.parseInt(location.substring(6, 9));
                            String rowString = StringUtils.leftPad(Integer.toString(rowInt2), 2, "0");
                            String lineString = StringUtils.leftPad(Integer.toString(lineInt2), 2, "0");
                            String tierString = StringUtils.leftPad(Integer.toString(tierInt2), 2, "0");
                            if (isSameSite(row, line, tier, location)) {
                                // 卸车
                                msgDTO = mcMsgService.offCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), rowString, lineString, tierString);
                            } else {
                                // 移动向货位
                                msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), rowString, lineString, tierString);
                            }
                        }
                    }
                } else {
                    String location = "001001001";
                    int rowInt2 = Integer.parseInt(location.substring(0, 3));
                    int lineInt2 = Integer.parseInt(location.substring(3, 6));
                    int tierInt2 = Integer.parseInt(location.substring(6, 9));
                    String rowString = StringUtils.leftPad(Integer.toString(rowInt2), 2, "0");
                    String lineString = StringUtils.leftPad(Integer.toString(lineInt2), 2, "0");
                    String tierString = StringUtils.leftPad(Integer.toString(tierInt2), 2, "0");
                    if (isSameSite(row, line, tier, location)) {
                        // 卸车
                        msgDTO = mcMsgService.offCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), rowString, lineString, tierString);
                    } else {
                        // 移动向货位
                        msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), rowString, lineString, tierString);
                    }
                }
            }
        } else if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            if (!isSameArea(scBlock.getRow(), scBlock.getLine(), scBlock.getTier(), toLocation)) {
                // 子车不在目标巷道
                msgDTO = getCarAndPutCarToInStorage(mcMsgService, workPlanType, toLocation, mcBlock, mcKey);
            } else {
                msgDTO = mcMsgService.offCar(workPlanType, mcKey, mcBlock.getName(), withWorkBlockName, row, line, tier);
            }
        } else if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
            if (StringUtils.isEmpty(mcBlock.getScBlockName())) {
                WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
                if (isSameSite(row, line, tier, scBlock.getRow(), scBlock.getLine(), scBlock.getTier())) {
                    // 接车
                    msgDTO = mcMsgService.getCar(workPlanType, mcKey, blockName, withWorkBlockName, scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                } else {
                    // 移动向子车
                    msgDTO = mcMsgService.move(mcKey, blockName, scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                }
            }
        } else if (WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            if (!isSameArea(scBlock.getRow(), scBlock.getLine(), scBlock.getTier(), toLocation)) {
                // 子车不在目标巷道
                msgDTO = getCarAndPutCarToInStorage(mcMsgService, workPlanType, toLocation, mcBlock, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            if (StringUtils.isEmpty(mcBlock.getScBlockName())) {
                msgDTO = scBlockNameIsEmptyWork(mcBlock, mcKey, blockName, workPlanType, row, line, tier);
            } else {
                String berthBlockName = mcBlock.getBerthBlockName();
                if (!workPlan.getToStation().equals(berthBlockName)) {
                    String toStation = workPlan.getToStation();
                    String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                     WcsMachineEntity machine = MachineCache.getMachine(nextBlockName);
                    msgDTO = mcMsgService.move(workPlan, blockName, machine.getBlockName(), machine.getDockName());
                }
            }
        } else if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
            if (StringUtils.isNotEmpty(mcBlock.getScBlockName())) {
                if (!isSameSite(row, line, tier, toLocation)) {
                    String toRow = toLocation.substring(0, 3);
                    String toLine = toLocation.substring(3, 6);
                    String toTier = toLocation.substring(6, 9);
                    // 移动向货位
                    msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), toRow, toLine, toTier);
                } else {
                    msgDTO = mcMsgService.offCar(workPlanType, mcKey, mcBlock.getName(), withWorkBlockName, row, line, tier);
                }
            } else {
                WcsMlblockDaoImpl.getInstance().updateMcKey("", blockName);
                Log4j2Util.getBlockBrickLogger().info(String.format("%s，该卸车任务已完成，mcKey：%s ，任务类型：%s", blockName, mcKey, Integer.toString(workPlanType)));
                WorkPlanService.finishWorkPlan(workPlan.getId(), mcKey);
            }
        } else if (WorkPlanConstant.TYPE_GET_CAR==workPlanType) {
            if (StringUtils.isEmpty(mcBlock.getScBlockName())) {
                msgDTO = scBlockNameIsEmptyWork(mcBlock, mcKey, blockName, workPlanType, row, line, tier);
            } else {
                Log4j2Util.getBlockBrickLogger().info(String.format("blockName： %s，mcKey：%s ，接车已完成", blockName, mcKey));
            }
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，未识别的任务类型，mcKey：%s ，任务类型：%s", blockName, mcKey, Integer.toString(workPlanType)));
        }
        return msgDTO;
    }

    protected MsgDTO scBlockNameIsEmptyWork(WcsMcblockEntity mcBlock, String mcKey, String blockName, Integer workPlanType, String row, String line, String tier) {
        MsgDTO msgDTO;
        McMsgService mcMsgService = new McMsgService();
        WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(mcBlock.getWithWorkBlockName());
        if (isSameSite(row, line, tier, scBlock.getRow(), scBlock.getLine(), scBlock.getTier())) {
            if (SystemCache.ML_REMOVE) {
                if (mcBlock.getIsMove()) {
                    // 接车
                    msgDTO = mcMsgService.getCar(workPlanType, mcKey, blockName, mcBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                } else {
                    // 移动向子车
                    msgDTO = mcMsgService.move(mcKey, blockName, scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                }
            } else {
                // 接车
                msgDTO = mcMsgService.getCar(workPlanType, mcKey, blockName, mcBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
            }
        } else {
            // 移动向子车
            msgDTO = mcMsgService.move(mcKey, blockName, scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
        }
        return msgDTO;
    }

    /**
     * 堆垛机消息制作公共处理逻辑
     *
     * @param mcMsgService 消息制作对象
     * @param workPlanType 工作计划类型
     * @param location     位置信息
     * @param mcBlock      母车运行状态
     * @param mcKey        mcKey
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/15 12:14
     */
    private MsgDTO getCarAndPutCarToInStorage(McMsgService mcMsgService, Integer workPlanType, String location, WcsMcblockEntity mcBlock, String mcKey) {
        MsgDTO msgDTO;
        String row = mcBlock.getRow();
        String line = mcBlock.getLine();
        String tier = mcBlock.getTier();
        int rowInt2 = Integer.parseInt(location.substring(0, 3));
        int lineInt2 = Integer.parseInt(location.substring(3, 6));
        int tierInt2 = Integer.parseInt(location.substring(6, 9));
        String rowString = StringUtils.leftPad(Integer.toString(rowInt2), 2, "0");
        String lineString = StringUtils.leftPad(Integer.toString(lineInt2), 2, "0");
        String tierString = StringUtils.leftPad(Integer.toString(tierInt2), 2, "0");
        if (StringUtils.isNotEmpty(mcBlock.getScBlockName())) {
            if (isSameSite(row, line, tier, location)) {
                if (SystemCache.ML_REMOVE) {
                    if (mcBlock.getIsMove()) {
                        // 卸车
                        msgDTO = mcMsgService.offCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), rowString, lineString, tierString);
                    } else {
                        // 移动向货位
                        msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), rowString, lineString, tierString);
                    }
                } else {
                    // 卸车
                    msgDTO = mcMsgService.offCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), rowString, lineString, tierString);
                }
            } else {
                // 移动向货位
                msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), rowString, lineString, tierString);
            }
        } else {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(mcBlock.getWithWorkBlockName());
            if (isSameSite(row, line, tier, scBlock.getRow(), scBlock.getLine(), scBlock.getTier())) {
                if (SystemCache.ML_REMOVE) {
                    if (mcBlock.getIsMove()) {
                        // 接车
                        msgDTO = mcMsgService.getCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                    } else {
                        // 移动向子车
                        msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                    }
                } else {
                    // 接车
                    msgDTO = mcMsgService.getCar(workPlanType, mcKey, mcBlock.getName(), mcBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                }
            } else {
                // 移动向子车
                msgDTO = mcMsgService.move(mcKey, mcBlock.getName(), scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
            }
        }
        return msgDTO;
    }
}
