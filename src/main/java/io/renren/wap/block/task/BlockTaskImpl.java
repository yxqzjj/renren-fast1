package io.renren.wap.block.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsAlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMcblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.*;
import io.renren.wap.block.BlockTask;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.factory.BlockDaoFactory;
import io.renren.wap.factory.BlockDaoImpl;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 默认任务分配
 *
 * @Author: CalmLake
 * @date 2019/7/25  16:44
 * @Version: V1.0.0
 **/
public class BlockTaskImpl implements BlockTaskInterface {
    public WcsTaskingEntity tasking;

    public BlockTaskImpl(WcsTaskingEntity tasking) {
        this.tasking = tasking;
    }

    /**
     * 任务分配
     *
     * @return boolean
     * @author CalmLake
     * @date 2019/7/25 16:44
     */
    @Override
    public boolean task() {
        boolean result = true;
        try {
            BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
            Integer id = tasking.getId();
            String blockName = tasking.getBlockName();
            String nextBlockName = tasking.getNextBlockName();
            String mcKey = tasking.getMckey();
            int workPlanType = tasking.getWorkPlanType();
            WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
            Integer workPlanId = workPlan.getId();
            int workPlanStatus = workPlan.getStatus();
            WcsMachineEntity machine = MachineCache.getMachine(blockName);
            BlockTask blockTask = new BlockTask(machine);
            blockTask.getBlockStatus();
            blockTask.judgeMachineStatus();
            Block block = blockTask.getBlock();
            String keyType = blockTask.getType();
            if (CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU.equals(SystemCache.SYS_NAME_COMPANY)) {
                String alBlockName = "AL01";
                if (alBlockName.equals(blockName) && WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
                    if (BlockConstant.KEY_EMPTY_STRING.equals(keyType)) {
                        if ("0101".equals(nextBlockName)) {
                            nextBlockName = "0003";
                        } else if ("0102".equals(nextBlockName)) {
                            nextBlockName = "0004";
                        } else if ("0103".equals(nextBlockName)) {
                            nextBlockName = "0005";
                        } else {
                            nextBlockName = "0002";
                        }
                        //  设备空闲
                        blockDaoFactory.getBlockDao(machine).updateMcKeyByName(mcKey, nextBlockName, blockName);
                        WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                        wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                        wcsWorkplanEntity.setStartTime(new Date());
                        DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);
                        DbUtil.getTaskingDao().deleteById(id);
                        BlockServiceImplFactory.blockServiceDoKey(blockName);
                        return true;
                    }
                }
            }
            WcsMachineEntity nextMachine = MachineCache.getMachine(nextBlockName);
            BlockTask NextBlockTask = new BlockTask(nextMachine);
            NextBlockTask.getBlockStatus();
            Block nextBlock = NextBlockTask.getBlock();
            NextBlockTask.judgeMachineStatus();
            String nextKeyType = NextBlockTask.getType();
            if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
                String scBlockName = workPlan.getReserved2();
                WcsMcblockEntity mcBlock = (WcsMcblockEntity) block;
                String bingScBlockName = mcBlock.getBingScBlockName();
                String mcBlockScBlockName = mcBlock.getScBlockName();
                WcsMcblockEntity mcBlock1 = (WcsMcblockEntity) nextBlock;
                String bingScBlockName1 = mcBlock1.getBingScBlockName();
                String mcBlockScBlockName1 = mcBlock1.getScBlockName();
                WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(scBlockName);
                if (BlockConstant.STATUS_BAN.equals(scBlock.getStatus())) {
                    WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(mcKey, scBlockName, nextBlockName);
                    WcsScblockDaoImpl.getInstance().updateTwoScBlock(mcKey, blockName, scBlockName);
                    if (StringUtils.isNotEmpty(mcBlockScBlockName) && !mcBlockScBlockName.equals(scBlockName)) {
                        WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(mcKey, bingScBlockName, blockName);
                        WcsScblockDaoImpl.getInstance().updateTwoScBlock(mcKey, blockName, bingScBlockName);
                    } else {
                        WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(mcKey, scBlockName, blockName);
                    }
                    WcsAlblockDaoImpl.getInstance().updateTwoALBlock(mcKey, scBlockName, "AL01");
                } else {
                    WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(mcKey, scBlockName, blockName);
                    WcsScblockDaoImpl.getInstance().updateTwoScBlock(mcKey, blockName, scBlockName);
                    if (StringUtils.isNotEmpty(mcBlockScBlockName1) && !mcBlockScBlockName1.equals(scBlockName)) {
                        WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(mcKey, bingScBlockName1, nextBlockName);
                        WcsScblockDaoImpl.getInstance().updateTwoScBlock(mcKey, nextBlockName, bingScBlockName1);
                    } else {
                        WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(mcKey, scBlockName, nextBlockName);
                    }
                    WcsAlblockDaoImpl.getInstance().updateTwoALBlock(mcKey, scBlockName, "AL01");
                }
                WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                wcsWorkplanEntity.setStartTime(new Date());
                DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);
                DbUtil.getTaskingDao().deleteById(id);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
                BlockServiceImplFactory.blockServiceDoKey(nextBlockName);
                BlockServiceImplFactory.blockServiceDoKey(scBlockName);
                BlockServiceImplFactory.blockServiceDoKey("AL01");
                BlockServiceImplFactory.blockServiceDoKey(bingScBlockName);
                BlockServiceImplFactory.blockServiceDoKey(mcBlockScBlockName1);
                return true;
            }
            boolean resultMachineScStatus = isScStatusCharge(machine, block);
            boolean resultNextMachineScStatus = isScStatusCharge(nextMachine, nextBlock);
            if (resultMachineScStatus || resultNextMachineScStatus) {
                if (WorkPlanConstant.TYPE_CHARGE_COMPLETE!=workPlanType) {
                    Log4j2Util.getAssigningTaskLogger().info(String.format("mcKey：%s，设备状态不正确，设备处于充电状态！", mcKey));
                    return false;
                }
            }
            boolean resultMachineScStatusCharge = isScStatusChargeFinish(machine, block);
            boolean resultNextMachineScStatusCharge = isScStatusChargeFinish(nextMachine, nextBlock);
            if (resultMachineScStatusCharge || resultNextMachineScStatusCharge) {
                if (WorkPlanConstant.TYPE_CHARGE_COMPLETE!=workPlanType) {
                    Log4j2Util.getAssigningTaskLogger().info(String.format("mcKey：%s，设备状态不正确，设备有充电完成任务！", mcKey));
                    return false;
                }
            }
            boolean resultMachineScStatusStartCharge = isScStatusStartCharge(machine, block);
            boolean resultNextMachineScStatusStartCharge = isScStatusStartCharge(nextMachine, nextBlock);
            if (resultMachineScStatusStartCharge || resultNextMachineScStatusStartCharge) {
                if (WorkPlanConstant.TYPE_CHARGE_UP!=workPlanType) {
                    Log4j2Util.getAssigningTaskLogger().info(String.format("mcKey：%s，设备状态不正确，设备有充电开始任务！", mcKey));
                    return false;
                }
            }
            if (BlockConstant.KEY_EMPTY_STRING.equals(keyType)) {
                if (BlockConstant.KEY_EMPTY_STRING.equals(nextKeyType) || BlockConstant.MCKEY_NOT_EMPTY_STRING.equals(nextKeyType)) {
                    //  两个设备空闲
                    blockDaoFactory.getBlockDao(machine).updateMcKeyByName(mcKey, nextBlockName, blockName);
                    updateBlockTable(mcKey, nextBlockName, blockName, nextBlock, nextMachine, blockDaoFactory);
                } else {
                    result = false;
                    Log4j2Util.getAssigningTaskLogger().info(String.format("下一设备有任务，任务标识类型：%s", nextKeyType));
                }
            } else {
                if (MachineConstant.BYTE_TYPE_CL.equals(machine.getType())) {
                    if (MachineConstant.BYTE_TYPE_ML.equals(nextMachine.getType()) || MachineConstant.BYTE_TYPE_MC.equals(nextMachine.getType()) || MachineConstant.BYTE_TYPE_AL.equals(nextMachine.getType())) {
                        if (BlockConstant.KEY_EMPTY_STRING.equals(nextKeyType)) {
                            blockDaoFactory.getBlockDao(nextMachine).updateMcKeyByName(mcKey, blockName, nextBlockName);
                        } else {
                            result = false;
                            Log4j2Util.getAssigningTaskLogger().info(String.format("设备有任务，任务标识类型：%s", nextKeyType));
                        }
                    } else {
                        result = false;
                        Log4j2Util.getAssigningTaskLogger().info(String.format("未解析的设备类型，设备类型：%d", nextMachine.getType()));
                    }
                } else {
                    if (SystemCache.OUT_STORAGE_OFF_CAR && WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
                        if (MachineConstant.BYTE_TYPE_ML.equals(machine.getType()) || MachineConstant.BYTE_TYPE_MC.equals(machine.getType())) {
                            if (MachineConstant.BYTE_TYPE_SC.equals(nextMachine.getType())) {
                                if (BlockConstant.MCKEY_NOT_EMPTY_STRING.equals(keyType) || BlockConstant.KEY_EMPTY_STRING.equals(nextKeyType)) {
                                    if (BlockConstant.KEY_EMPTY_STRING.equals(nextKeyType) || BlockConstant.MCKEY_NOT_EMPTY_STRING.equals(nextKeyType)) {
                                        String beforeMcKey = block.getMckey();
                                        WcsWorkplanEntity beforeWorkPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",beforeMcKey));
                                        if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==beforeWorkPlan.getType()) {
                                            String statusSc = nextBlock.getStatus();
                                            if (BlockConstant.STATUS_RUNNING.equals(statusSc)) {
                                                WcsScblockEntity scBlock = (WcsScblockEntity) nextBlock;
                                                Integer kwh = Integer.parseInt(scBlock.getKwh());
                                                if (kwh > SystemCache.SC_WORK_MIN_KWH) {
                                                    String mcKeyNow = block.getMckey();
                                                    WcsWorkplanEntity workPlanNow = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKeyNow));
                                                    BlockService blockService = new BlockService();
                                                    if (blockService.isSameArea(workPlan.getFromLocation(), workPlanNow.getFromLocation())) {
                                                        //  两个设备正在执行任务且预约任务皆为空
                                                        updateBlockTable(mcKey, blockName, nextBlockName, block, machine, blockDaoFactory);
                                                        updateBlockTable(mcKey, nextBlockName, blockName, nextBlock, nextMachine, blockDaoFactory);
                                                    } else {
                                                        result = false;
                                                        Log4j2Util.getAssigningTaskLogger().info(String.format("源货架位校验不符合规则，当前任务：%s ，下一任务：%s", workPlanNow.getFromLocation(), workPlan.getFromLocation()));
                                                    }
                                                } else {
                                                    result = false;
                                                    Log4j2Util.getAssigningTaskLogger().info(String.format("穿梭车电量不符合条件，穿梭车电量：%d ", kwh));
                                                }
                                            } else {
                                                result = false;
                                                Log4j2Util.getAssigningTaskLogger().info(String.format("穿梭车状态检验失败，穿梭车状态：%s ", statusSc));
                                            }
                                        } else {
                                            result = false;
                                            Log4j2Util.getAssigningTaskLogger().info(String.format("设备有任务，workType：%d", beforeWorkPlan.getType()));
                                        }
                                    } else {
                                        result = false;
                                        Log4j2Util.getAssigningTaskLogger().info(String.format("设备有任务，任务标识类型：%s，任务标识类型：%s", keyType, nextKeyType));
                                    }
                                } else {
                                    result = false;
                                    Log4j2Util.getAssigningTaskLogger().info(String.format("设备有任务，任务标识类型：%s，任务标识类型：%s", keyType, nextKeyType));
                                }
                            } else {
                                result = false;
                                Log4j2Util.getAssigningTaskLogger().info(String.format("未解析的设备类型，设备类型：%d", nextMachine.getType()));
                            }
                        } else {
                            result = false;
                            Log4j2Util.getAssigningTaskLogger().info(String.format("未解析的设备类型，设备类型：%d，设备类型：%d", machine.getType(), nextMachine.getType()));
                        }
                    } else {
                        result = false;
                        Log4j2Util.getAssigningTaskLogger().info(String.format("优先出库开关：%b，工作计划类型：%d", SystemCache.OUT_STORAGE_OFF_CAR, workPlanType));
                    }
                }
            }
            if (result) {
                if (WorkPlanConstant.STATUS_WAIT==workPlanStatus) {
                    WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                    wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                    wcsWorkplanEntity.setStartTime(new Date());
                    DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);
                }
                DbUtil.getTaskingDao().deleteById(id);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
                BlockServiceImplFactory.blockServiceDoKey(nextBlockName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 修改DB操作
     *
     * @param mcKey           任务标识
     * @param blockName       设备block名称
     * @param nextBlockName   下一设备block名称
     * @param block           block信息对象
     * @param machine         设备信息对象
     * @param blockDaoFactory blockDao工厂
     * @author CalmLake
     * @date 2019/7/29 9:25
     */
    void updateBlockTable(String mcKey, String blockName, String nextBlockName, Block block, WcsMachineEntity machine, BlockDaoFactory blockDaoFactory) {
        if (mcKey.equals(block.getMckey())) {
            blockDaoFactory.getBlockDao(machine).updateMcKeyByName(mcKey, nextBlockName, blockName);
        } else {
            blockDaoFactory.getBlockDao(machine).updateAppointmentMcKeyReserved1ByName(blockName, mcKey, nextBlockName);
        }
    }

    private boolean isScStatusCharge(WcsMachineEntity machine, Block block) {
        int machineType = machine.getType();
        return isStatusSame(machineType, block, BlockConstant.STATUS_CHARGE);
    }

    private boolean isScStatusRunning(WcsMachineEntity machine, Block block) {
        int machineType = machine.getType();
        return isStatusSame(machineType, block, BlockConstant.STATUS_RUNNING);
    }

    private boolean isScStatusChargeFinish(WcsMachineEntity machine, Block block) {
        int machineType = machine.getType();
        return isStatusSame(machineType, block, BlockConstant.STATUS_CREATE_CHARGE_FINISH);
    }

    private boolean isScStatusStartCharge(WcsMachineEntity machine, Block block) {
        int machineType = machine.getType();
        return isStatusSame(machineType, block, BlockConstant.STATUS_CREATE_CHARGE_START);
    }

    private boolean isStatusSame(int machineType, Block block, String scStatus) {
        String scBlockName = null;
        boolean result = false;
        if (MachineConstant.BYTE_TYPE_MC.equals(machineType)) {
            scBlockName = ((WcsMcblockEntity) block).getBingScBlockName();
        } else if (MachineConstant.BYTE_TYPE_ML.equals(machineType)) {
            scBlockName = ((WcsMlblockEntity) block).getBingScBlockName();
        }
        if (StringUtils.isNotEmpty(scBlockName)) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(scBlockName);
            if (scStatus.equals(scBlock.getStatus())) {
                result = true;
            }
        }
        return result;
    }
}