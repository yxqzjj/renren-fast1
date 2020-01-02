package io.renren.wap.service.block.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsMlblockEntity;
import io.renren.modules.generator.entity.WcsScblockEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.BlockMsgSendService;
import io.renren.wap.service.RouteService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.service.block.AppointmentMcKeyService;
import io.renren.wap.service.block.OperationKeyService;
import io.renren.wap.service.block.charge.ChargeServiceInterface;
import io.renren.wap.service.block.charge.self.SelfMlBlockServiceImpl;
import io.renren.wap.service.block.charge.shelf.ShelfMlBlockServiceImpl;
import io.renren.wap.service.msg.MlMsgService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

/**
 * 堆垛机逻辑处理
 *
 * @Author: CalmLake
 * @Date: 2019/1/10  11:54
 * @Version: V1.0.0
 **/
public class MlBlockServiceImpl extends BlockService implements OperationKeyService, AppointmentMcKeyService {

    @Override
    public void operationKey(String blockName) throws InterruptedException {
        WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String mlBlockMcKey = mlBlock.getMckey();
        String mlBlockAppointmentMcKey = mlBlock.getAppointmentMckey();
        String appointmentBlockName = mlBlock.getReserved1();
        if (StringUtils.isEmpty(mlBlockMcKey) && StringUtils.isNotEmpty(mlBlockAppointmentMcKey)) {
            WcsMlblockDaoImpl.getInstance().updateThreeValueMLBlock(mlBlockAppointmentMcKey, "", appointmentBlockName, blockName);
        }
        mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        if (isCanSendMsg(mlBlock)) {
            int keyType = getKeyType(mlBlock);
            boolean resultSend;
            MsgDTO msgDTO;
            BlockMsgSendService blockMsgSendService = new BlockMsgSendService();
            if (BlockConstant.KEY_NOT_EMPTY == keyType) {
                msgDTO = appointmentMcKey(mlBlock);
            } else {
                msgDTO = doWork(mlBlock.getMckey(), mlBlock);
            }
            resultSend = blockMsgSendService.sendMsg(msgDTO, blockName);
            if (resultSend) {
                int resultUpdateDb = updateCommand(MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER, WcsMlblockDaoImpl.getInstance(), blockName);
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
        MlMsgService mlMsgService = new MlMsgService();
        WcsMlblockEntity mlBlock = (WcsMlblockEntity) object;
        boolean isLoad = mlBlock.getIsLoad();
        String scBlockName = mlBlock.getScBlockName();
        String appointmentWithWorkBlockName = mlBlock.getReserved1();
        String mcKey = mlBlock.getMckey();
        String appointmentMcKey = mlBlock.getAppointmentMckey();
        WcsWorkplanEntity workPlanMcKey = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanMcKeyType = workPlanMcKey.getType();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",appointmentMcKey));
        String fromLocation = workPlan.getFromLocation();
        Integer workPlanType = workPlan.getType();
        if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanMcKeyType && WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(appointmentWithWorkBlockName);
            String rowSc = mlBlock.getRow();
            String lineSc = mlBlock.getLine();
            String tierSc = mlBlock.getTier();
            String hostBlockName = scBlock.getHostBlockName();
            if (isSameSite(rowSc, lineSc, tierSc, fromLocation) && StringUtils.isEmpty(hostBlockName) && StringUtils.isEmpty(scBlockName)) {
                //  子车已到达目标巷道
                msgDTO = doWork(mcKey, mlBlock);
            } else {
                //  堆垛机载车载货
                if (isLoad && StringUtils.isNotEmpty(scBlockName) && appointmentWithWorkBlockName.equals(scBlockName)) {
                    String row = mlBlock.getRow();
                    String line = mlBlock.getLine();
                    String tier = mlBlock.getTier();
                    int rowInt2 = Integer.parseInt(fromLocation.substring(0, 3));
                    int lineInt2 = Integer.parseInt(fromLocation.substring(3, 6));
                    int tierInt2 = Integer.parseInt(fromLocation.substring(6, 9));
                    String rowString = StringUtils.leftPad(Integer.toString(rowInt2), 2, "0");
                    String lineString = StringUtils.leftPad(Integer.toString(lineInt2), 2, "0");
                    String tierString = StringUtils.leftPad(Integer.toString(tierInt2), 2, "0");
                    //  子车是否到达目标巷道
                    if (isSameSite(row, line, tier, fromLocation)) {
                        if (SystemCache.ML_REMOVE) {
                            if (mlBlock.getIsMove()) {
                                // 卸车
                                msgDTO = mlMsgService.offCar(workPlanType, appointmentMcKey, mlBlock.getName(), appointmentWithWorkBlockName, rowString, lineString, tierString);
                            } else {
                                msgDTO = mlMsgService.move(appointmentMcKey, mlBlock.getName(), rowString, lineString, tierString);
                            }
                        } else {
                            msgDTO = mlMsgService.offCar(workPlanType, appointmentMcKey, mlBlock.getName(), appointmentWithWorkBlockName, rowString, lineString, tierString);
                        }
                    } else {
                        msgDTO = mlMsgService.move(appointmentMcKey, mlBlock.getName(), rowString, lineString, tierString);
                    }
                } else {
                    msgDTO = doWork(mcKey, mlBlock);
                }
            }
        } else {
            msgDTO = doWork(mcKey, mlBlock);
        }
        return msgDTO;
    }

    /**
     * 根据任务类型进行不同的逻辑处理
     *
     * @param mcKey   mcKey
     * @param mlBlock 堆垛机运行状态
     * @return MsgDTO 消息
     * @author CalmLake
     * @date 2019/1/10 15:10
     */
    private MsgDTO doWork(String mcKey, WcsMlblockEntity mlBlock) {
        MsgDTO msgDTO = null;
        MlMsgService mlMsgService = new MlMsgService();
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanType = workPlan.getType();
        int workPlanId = workPlan.getId();
        String blockName = mlBlock.getName();
        String row = mlBlock.getRow();
        String line = mlBlock.getLine();
        String tier = mlBlock.getTier();
        boolean isLoad = mlBlock.getIsLoad();
        String withWorkBlockName = mlBlock.getWithWorkBlockName();
        if (withWorkBlockName.contains(MachineConstant.TYPE_SC)) {
            String scStatus = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName).getStatus();
            if (!isScStatusCanWork(scStatus, workPlanType)) {
                Log4j2Util.getBlockBrickLogger().info(String.format("blockName: %s,当前设备状态及工作计划类型匹配，block：%s，status：%s，workPlanType：%d", blockName, withWorkBlockName, scStatus, workPlanType));
                return null;
            }
        }
        String toLocation = workPlan.getToLocation();
        String fromLocation = workPlan.getFromLocation();
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
            if (isLoad) {
                msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, toLocation, mlBlock, mcKey);
            } else {
                WcsMachineEntity machine = MachineCache.getMachine(mlBlock.getWithWorkBlockName());
                if (MachineConstant.BYTE_TYPE_CL.equals(machine.getType())) {
                    if (StringUtils.isNotEmpty(mlBlock.getBerthBlockName()) && machine.getDockName().equals(mlBlock.getBerthBlockName())) {
                        // 取货
                        msgDTO = mlMsgService.transplantingPickUp(workPlan, blockName, mlBlock.getWithWorkBlockName());
                    } else {
                        // 移动向该设备（输送线）
                        msgDTO = mlMsgService.move(mcKey, workPlanType, blockName, machine.getBlockName(), machine.getDockName());
                    }
                } else {
                    Log4j2Util.getBlockBrickLogger().info(String.format("%s，未载荷，mcKey：%s ，交互设备为：%s", blockName, mcKey, machine.getBlockName()));
                }
            }
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            if (isLoad) {
                WcsMachineEntity machine = MachineCache.getMachine(mlBlock.getWithWorkBlockName());
                if (StringUtils.isNotEmpty(mlBlock.getBerthBlockName()) && machine.getDockName().equals(mlBlock.getBerthBlockName())) {
                    // 移载卸货
                    msgDTO = mlMsgService.transplantingTheUnloading(workPlan, blockName, mlBlock.getWithWorkBlockName());
                } else {
                    // 移动向该设备（输送线）
                    msgDTO = mlMsgService.move(mcKey, workPlanType, blockName, machine.getBlockName(), machine.getDockName());
                }
            } else {
                msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, fromLocation, mlBlock, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_MOVEMENT==workPlanType) {
            if (isLoad) {
                msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, toLocation, mlBlock, mcKey);
            } else {
                msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, fromLocation, mlBlock, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_TALLY==workPlanType) {
            msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, toLocation, mlBlock, mcKey);
        } else if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
            // todo 暂无
        } else if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                ChargeServiceInterface chargeServiceInterface = new SelfMlBlockServiceImpl();
                msgDTO = chargeServiceInterface.chargeUp(workPlan, mlBlock);
            } else if (CompanyConstant.SYS_NAME_COMPANY_YONG_XIANG.equals(SystemCache.SYS_NAME_COMPANY)) {
                ChargeServiceInterface shelfMlBlockService = new ShelfMlBlockServiceImpl();
                msgDTO = shelfMlBlockService.chargeUp(workPlan, mlBlock);
            } else {
                msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, toLocation, mlBlock, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                ChargeServiceInterface chargeServiceInterface = new SelfMlBlockServiceImpl();
                msgDTO = chargeServiceInterface.chargeFinish(workPlan, mlBlock);
            } else if (CompanyConstant.SYS_NAME_COMPANY_YONG_XIANG.equals(SystemCache.SYS_NAME_COMPANY)) {
                ChargeServiceInterface shelfMlBlockService = new ShelfMlBlockServiceImpl();
                msgDTO = shelfMlBlockService.chargeFinish(workPlan, mlBlock);
            } else {
                msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, toLocation, mlBlock, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType) {
            msgDTO = getCarAndPutCarToInStorage(mlMsgService, workPlanType, toLocation, mlBlock, mcKey);
        } else if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            if (StringUtils.isEmpty(mlBlock.getScBlockName())) {
                msgDTO = scBlockNameIsEmptyWork(mlBlock, mcKey, blockName, workPlanType, row, line, tier);
            } else {
                String berthBlockName = mlBlock.getBerthBlockName();
                if (!workPlan.getToStation().equals(berthBlockName)) {
                    String toStation = workPlan.getToStation();
                    String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                    msgDTO = mlMsgService.move(mcKey, workPlanType, blockName, nextBlockName, toStation);
                }
            }
        } else if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
            if (StringUtils.isNotEmpty(mlBlock.getScBlockName())) {
                if (!isSameSite(row, line, tier, toLocation)) {
                    String toRow = toLocation.substring(0, 3);
                    String toLine = toLocation.substring(3, 6);
                    String toTier = toLocation.substring(6, 9);
                    msgDTO = mlMsgService.move(mcKey, blockName, toRow, toLine, toTier);
                } else {
                    msgDTO = mlMsgService.offCar(workPlanType, mcKey, blockName, withWorkBlockName, row, line, tier);
                }
            } else {
                WcsMlblockDaoImpl.getInstance().updateMcKey("", blockName);
                Log4j2Util.getBlockBrickLogger().info(String.format("%s，该卸车任务已完成，mcKey：%s ，任务类型：%s", blockName, mcKey, Integer.toString(workPlanType)));
                WorkPlanService.finishWorkPlan(workPlanId, mcKey);
            }
        } else if (WorkPlanConstant.TYPE_GET_CAR==workPlanType) {
            if (StringUtils.isEmpty(mlBlock.getScBlockName())) {
                msgDTO = scBlockNameIsEmptyWork(mlBlock, mcKey, blockName, workPlanType, row, line, tier);
            } else {
                Log4j2Util.getBlockBrickLogger().info(String.format("blockName： %s，mcKey：%s ，接车已完成", blockName, mcKey));
            }
        } else {
            Log4j2Util.getBlockBrickLogger().info(String.format("%s，未识别的任务类型，mcKey：%s ，任务类型：%s", blockName, mcKey, Integer.toString(workPlanType)));
        }
        return msgDTO;
    }

    /**
     * 堆垛机无子车处理逻辑
     *
     * @param mlBlock      堆垛机block对象
     * @param mcKey        任务标识
     * @param blockName    block名称
     * @param workPlanType 工作计划类型
     * @param row          排
     * @param line         列
     * @param tier         层
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/3/25 11:37
     */
    protected MsgDTO scBlockNameIsEmptyWork(WcsMlblockEntity mlBlock, String mcKey, String blockName, Integer workPlanType, String row, String line, String tier) {
        MsgDTO msgDTO;
        MlMsgService mlMsgService = new MlMsgService();
        WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(mlBlock.getWithWorkBlockName());
        if (isSameSite(row, line, tier, scBlock.getRow(), scBlock.getLine(), scBlock.getTier())) {
            if (SystemCache.ML_REMOVE) {
                if (mlBlock.getIsMove()) {
                    // 接车
                    msgDTO = mlMsgService.getCar(workPlanType, mcKey, blockName, mlBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                } else {
                    // 移动向子车
                    msgDTO = mlMsgService.move(mcKey, blockName, scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                }
            } else {
                // 接车
                msgDTO = mlMsgService.getCar(workPlanType, mcKey, blockName, mlBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
            }
        } else {
            // 移动向子车
            msgDTO = mlMsgService.move(mcKey, blockName, scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
        }
        return msgDTO;
    }

    /**
     * 堆垛机消息制作公共处理逻辑  移动 接车  卸车
     *
     * @param mlMsgService 消息制作对象
     * @param workPlanType 工作计划类型
     * @param location     位置信息
     * @param mlBlock      堆垛机运行状态
     * @param mcKey        mcKey
     * @return com.wap.client.dto.MsgDTO
     * @author CalmLake
     * @date 2019/1/15 12:14
     */
    protected MsgDTO getCarAndPutCarToInStorage(MlMsgService mlMsgService, Integer workPlanType, String location, WcsMlblockEntity mlBlock, String mcKey) {
        MsgDTO msgDTO;
        String row = mlBlock.getRow();
        String line = mlBlock.getLine();
        String tier = mlBlock.getTier();
        int rowInt2 = Integer.parseInt(location.substring(0, 3));
        int lineInt2 = Integer.parseInt(location.substring(3, 6));
        int tierInt2 = Integer.parseInt(location.substring(6, 9));
        String rowString = StringUtils.leftPad(Integer.toString(rowInt2), 2, "0");
        String lineString = StringUtils.leftPad(Integer.toString(lineInt2), 2, "0");
        String tierString = StringUtils.leftPad(Integer.toString(tierInt2), 2, "0");
        if (StringUtils.isNotEmpty(mlBlock.getScBlockName())) {
            if (isSameSite(row, line, tier, location)) {
                if (SystemCache.ML_REMOVE) {
                    if (mlBlock.getIsMove()) {
                        // 卸车
                        msgDTO = mlMsgService.offCar(workPlanType, mcKey, mlBlock.getName(), mlBlock.getWithWorkBlockName(), rowString, lineString, tierString);
                    } else {
                        msgDTO = mlMsgService.move(mcKey, mlBlock.getName(), rowString, lineString, tierString);
                    }
                } else {
                    msgDTO = mlMsgService.offCar(workPlanType, mcKey, mlBlock.getName(), mlBlock.getWithWorkBlockName(), rowString, lineString, tierString);
                }
            } else {
                // 移动向货位
                msgDTO = mlMsgService.move(mcKey, mlBlock.getName(), rowString, lineString, tierString);
            }
        } else {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(mlBlock.getWithWorkBlockName());
            if (isSameSite(scBlock.getRow(), scBlock.getLine(), scBlock.getTier(), location)) {
                if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
                    return null;
                }
            }
            if (isSameSite(row, line, tier, scBlock.getRow(), scBlock.getLine(), scBlock.getTier())) {
                if (SystemCache.ML_REMOVE) {
                    if (mlBlock.getIsMove()) {
                        // 接车
                        msgDTO = mlMsgService.getCar(workPlanType, mcKey, mlBlock.getName(), mlBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                    } else {
                        msgDTO = mlMsgService.move(mcKey, mlBlock.getName(), scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                    }
                } else {
                    // 接车
                    msgDTO = mlMsgService.getCar(workPlanType, mcKey, mlBlock.getName(), mlBlock.getWithWorkBlockName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
                }
            } else {
                // 移动向子车
                msgDTO = mlMsgService.move(mcKey, mlBlock.getName(), scBlock.getName(), scBlock.getRow(), scBlock.getLine(), scBlock.getTier());
            }
        }
        return msgDTO;
    }

}
