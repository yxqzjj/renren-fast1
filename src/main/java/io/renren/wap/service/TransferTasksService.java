package io.renren.wap.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.BlockDao;
import io.renren.modules.generator.dao.impl.WcsClblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMcblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsMlblockDaoImpl;
import io.renren.modules.generator.dao.impl.WcsScblockDaoImpl;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.LockCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgCycleOrderConstant;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.factory.BlockDaoFactory;
import io.renren.wap.factory.BlockDaoImpl;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.ChargeLocationUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 任务传递
 *
 * @Author: CalmLake
 * @Date: 2019/6/5  9:38
 * @Version: V1.0.0
 **/
public class TransferTasksService {
    /**
     * 堆垛机/母车/提升机/RGV移载取货完成后任务传递
     *
     * @param blockName   数据block名称
     * @param msgMcKey    消息中携带的mckey
     * @param machineType 设备类型
     * @author CalmLake
     * @date 2019/6/5 9:43
     */
    public void transplantingPickUpFinishedTransferTasks(String blockName, String msgMcKey, Integer machineType) {
        BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
        BlockDao blockDao = blockDaoFactory.getBlockDao(blockName);
        Block block = blockDao.selectByPrimaryKey(blockName);
        // 分配任务
        String withWorkBlockName = StandbyCarService.getScBlockName(block);
        if (MachineService.isScMachine(withWorkBlockName)) {
            WcsScblockEntity nextBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            String mcKey = nextBlock.getMckey();
            String appointmentMcKey = nextBlock.getAppointmentMckey();
            if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
                //  修改自身状态且下一设备设备无任务 无预约任务 分配任务
                DbUtil.getProcedureOrcaleDao().spUpdateBlockScBlockTransplantingPickUpFinishedTaskingIn(blockName, withWorkBlockName, msgMcKey, machineType);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else if (StringUtils.isNotEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
                if (!mcKey.equals(msgMcKey)) {
                    //  修改自身状态且当前下一设备有任务无预约任务 分配预约任务
                    DbUtil.getProcedureOrcaleDao().spUpdateBlockScBlockTransplantingPickUpFinishedTaskingAppointmentmckeyIn(blockName, withWorkBlockName, msgMcKey, machineType);
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                } else {
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，该设备的条件不满足，McKey：%s,AppointmentMcKey：%s", blockName, mcKey, appointmentMcKey));
                }
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，该设备的条件不满足，McKey：%s,AppointmentMcKey：%s", blockName, mcKey, appointmentMcKey));
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载取货变成，制作队列消息时未解析的交互设备类型，withWorkBlockName：%s", blockName, withWorkBlockName));
        }
    }

    /**
     * 堆垛机/母车/提升机/RGV移载卸货完成任务传递
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/6/5 10:12
     */
    public void transplantingTheUnloadingFinishedTransferTasks(String blockName) {
        BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
        BlockDao blockDao = blockDaoFactory.getBlockDao(blockName);
        Block block = blockDao.selectByPrimaryKey(blockName);
        String mlBlockAppointmentMcKey = block.getAppointmentMckey();
        if (StringUtils.isNotEmpty(mlBlockAppointmentMcKey)) {
            String appointmentBlockName = block.getReserved1();
            blockDao.updateThreeValueBlock(mlBlockAppointmentMcKey, "", appointmentBlockName, blockName);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载卸货完成，当前无预约任务", blockName));
        }
        LockCache.getValue(blockName).signal();
    }

    /**
     * 堆垛机/母车/提升机/RGV移动完成任务传递
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/6/5 10:11
     */
    public void moveFinishTransferTasks(String blockName) {
        BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
        BlockDao blockDao = blockDaoFactory.getBlockDao(blockName);
        //  任务分配或执行未完成任务
        Block block = blockDao.selectByPrimaryKey(blockName);
        String blockMcKey = block.getMckey();
        String blockAppointmentMcKey = block.getAppointmentMckey();
        String withWorkBlockName = block.getWithWorkBlockName();
        String AppointmentWithWorkBlockName = block.getReserved1();
        String scBlockName = "";
        if (block instanceof WcsMlblockEntity) {
            scBlockName = ((WcsMlblockEntity) block).getScBlockName();
        }
        if (block instanceof WcsMcblockEntity) {
            scBlockName = ((WcsMcblockEntity) block).getScBlockName();
        }
        if (StringUtils.isNotEmpty(blockMcKey)) {
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            //  优先卸车判断
            if (SystemCache.OUT_STORAGE_OFF_CAR) {
                if (StringUtils.isNotEmpty(blockAppointmentMcKey)) {
                    if (scBlockName.contains(MachineConstant.TYPE_SC) && StringUtils.isNotEmpty(scBlockName) && scBlockName.equals(AppointmentWithWorkBlockName)) {
                        BlockServiceImplFactory.blockServiceDoKey(AppointmentWithWorkBlockName);
                        return;
                    }
                }
            }
            //  执行未完成任务
            if (MachineService.isClMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            } else if (MachineService.isScMachine(withWorkBlockName)) {
                //  穿梭车任务处理
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
     * 堆垛机/母车接车完成后任务传递
     *
     * @param msgMcKey    消息中mckey
     * @param blockName   数据block名称
     * @param loadCar     载车
     * @param loadStatus  载荷
     * @param machineType 设备类型
     * @author CalmLake
     * @date 2019/6/5 10:09
     */
    public void getCarFinishTransferTasks(String msgMcKey, String blockName, String loadCar, String loadStatus, Integer machineType) {
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        Integer workPlanType = workPlan.getType();
        String toStation = workPlan.getToStation();
        while (true) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(loadCar);
            String hostBlockName = scBlock.getHostBlockName();
            if (StringUtils.isNotEmpty(hostBlockName) && blockName.equals(hostBlockName)) {
                break;
            }
            SleepUtil.sleep(0.5);
        }
        if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                //  任务结束 清除任务
                charge(blockName, loadCar, BlockConstant.STATUS_RUNNING, msgMcKey, workPlan.getId(), machineType);
                ChargeLocationUtil.getInstance().recycleLocation(loadCar);
                LockCache.getValue(blockName).signal();
            } else {
                if (blockName.equals(toStation)) {
                    charge(blockName, loadCar, BlockConstant.STATUS_RUNNING, msgMcKey, workPlan.getId(), machineType);
                    ChargeLocationUtil.getInstance().recycleLocation(loadCar);
                } else {
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            }
        } else if (WorkPlanConstant.TYPE_CHANGE_TIER==workPlanType) {
            if (blockName.equals(toStation)) {
                WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                String bingScBlockName = mcBlock.getBingScBlockName();
                if (loadCar.equals(bingScBlockName)) {
                    BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
                    BlockDao blockDao = blockDaoFactory.getBlockDao(blockName);
                    blockDao.updateMcKeyByName("", "", blockName);
                    WcsScblockDaoImpl.getInstance().updateStatus(bingScBlockName, BlockConstant.STATUS_RUNNING);
                    // 接车任务 子车上车后结束 工作计划完成
                    WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                    LockCache.getValue(blockName).signal();
                } else {
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        } else if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                //  任务结束 清除任务
                charge(blockName, loadCar, BlockConstant.STATUS_CHARGE, msgMcKey, workPlan.getId(), machineType);
                LockCache.getValue(blockName).signal();
            } else if (CompanyConstant.SYS_NAME_COMPANY_YONG_XIANG.equals(SystemCache.SYS_NAME_COMPANY) || CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU.equals(SystemCache.SYS_NAME_COMPANY)) {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        } else if (WorkPlanConstant.TYPE_GET_CAR==workPlanType) {
            BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
            BlockDao blockDao = blockDaoFactory.getBlockDao(blockName);
            blockDao.updateMcKeyByName("", "", blockName);
            // 接车任务 子车上车后结束 工作计划完成
            WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            LockCache.getValue(blockName).signal();
        } else if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
            if (MsgCycleOrderConstant.LOAD_STATUS_HAVE_CAR_LOAD.equals(loadStatus)) {
                //  接车接货完成 当前设备分配任务
                String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                if (nextBlockName.contains(MachineConstant.TYPE_CL)) {
                    WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(nextBlockName);
                    if (StringUtils.isNotEmpty(clBlock.getMckey()) && StringUtils.isEmpty(clBlock.getAppointmentMckey())) {
                        DbUtil.getProcedureOrcaleDao().spUpdateBlockClblockAppointmckeyIn(blockName, nextBlockName, msgMcKey, machineType);
                    } else if (StringUtils.isEmpty(clBlock.getMckey()) && StringUtils.isEmpty(clBlock.getAppointmentMckey())) {
                        DbUtil.getProcedureOrcaleDao().spUpdateBlockClblockMckeyIn(blockName, nextBlockName, msgMcKey, machineType);
                    }
                } else {
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：接车接货完成，任务分配时未解析的交互设备类型，nextBlockName：%s", blockName, nextBlockName));
                }
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        } else if (WorkPlanConstant.TYPE_MOVEMENT==workPlanType) {
            if (MsgCycleOrderConstant.LOAD_STATUS_HAVE_CAR_LOAD.equals(loadStatus)) {
                if (toStation.equals(blockName)) {
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                } else {
                    //  接车接货完成 当前设备分配任务
                    String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                    if (nextBlockName.contains(MachineConstant.TYPE_CL)) {
                        WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(nextBlockName);
                        if (StringUtils.isNotEmpty(clBlock.getMckey()) && StringUtils.isEmpty(clBlock.getAppointmentMckey())) {
                            DbUtil.getProcedureOrcaleDao().spUpdateBlockClblockAppointmckeyIn(blockName, nextBlockName, msgMcKey, machineType);
                        } else if (StringUtils.isEmpty(clBlock.getMckey()) && StringUtils.isEmpty(clBlock.getAppointmentMckey())) {
                            DbUtil.getProcedureOrcaleDao().spUpdateBlockClblockMckeyIn(blockName, nextBlockName, msgMcKey, machineType);
                        }
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：接车接货完成，任务分配时未解析的交互设备类型，nextBlockName：%s", blockName, nextBlockName));
                    }
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        } else if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
            BlockDaoFactory blockDaoFactory = new BlockDaoImpl();
            BlockDao blockDao = blockDaoFactory.getBlockDao(blockName);
            //  当且仅当载车且处于默认位置时完成任务
            Block block = blockDao.selectByPrimaryKey(blockName);
            String berthBlockName = block.getBerthBlockName();
            if (StringUtils.isNotEmpty(loadCar) && loadCar.equals(((WcsMcblockEntity) block).getBingScBlockName()) && StringUtils.isNotEmpty(berthBlockName) && berthBlockName.equals(toStation)) {
                DbUtil.getProcedureOrcaleDao().spUpdateBlockScblockClearMckeyIn(blockName, loadCar, machineType);
                WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
                LockCache.getValue(blockName).signal();
            } else {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        } else {
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        }
    }


    /**
     * 堆垛机移载取货完成后任务传递
     *
     * @param blockName 数据block名称
     * @param msgMcKey  消息中携带的mckey
     * @author CalmLake
     * @date 2019/6/5 9:43
     */
    public void mlTransplantingPickUpFinishedTransferTasks(String blockName, String msgMcKey) {
        // 分配任务
        WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String withWorkBlockName = StandbyCarService.getScBlockName(mlBlock);
        if (MachineService.isScMachine(withWorkBlockName)) {
            WcsScblockEntity nextBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(withWorkBlockName);
            String mcKey = nextBlock.getMckey();
            String appointmentMcKey = nextBlock.getAppointmentMckey();
            if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
                //  修改自身状态且下一设备设备无任务 无预约任务 分配任务
                DbUtil.getProcedureOrcaleDao().spUpdateBlockScBlockTransplantingPickUpFinishedTaskingIn(blockName, withWorkBlockName, msgMcKey, MachineConstant.BYTE_TYPE_ML);
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            } else if (StringUtils.isNotEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
                if (!mcKey.equals(msgMcKey)) {
                    //  修改自身状态且当前下一设备有任务无预约任务 分配预约任务
                    DbUtil.getProcedureOrcaleDao().spUpdateBlockScBlockTransplantingPickUpFinishedTaskingAppointmentmckeyIn(blockName, withWorkBlockName, msgMcKey, MachineConstant.BYTE_TYPE_ML);
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                } else {
                    Log4j2Util.getMsgCustomerLogger().info(String.format("%s，该设备的条件不满足，McKey：%s,AppointmentMcKey：%s", blockName, mcKey, appointmentMcKey));
                }
            } else {
                Log4j2Util.getMsgCustomerLogger().info(String.format("%s，该设备的条件不满足，McKey：%s,AppointmentMcKey：%s", blockName, mcKey, appointmentMcKey));
            }
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载取货变成，制作队列消息时未解析的交互设备类型，withWorkBlockName：%s", blockName, withWorkBlockName));
        }
    }

    /**
     * 堆垛机移载卸货完成任务传递
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/6/5 10:12
     */
    public void mlTransplantingTheUnloadingFinishedTransferTasks(String blockName) {
        WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String mlBlockAppointmentMcKey = mlBlock.getAppointmentMckey();
        if (StringUtils.isNotEmpty(mlBlockAppointmentMcKey)) {
            String appointmentBlockName = mlBlock.getReserved1();
            WcsMlblockDaoImpl.getInstance().updateThreeValueMLBlock(mlBlockAppointmentMcKey, "", appointmentBlockName, blockName);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：移载卸货完成，当前无预约任务", blockName));
        }
        LockCache.getValue(blockName).signal();
    }

    /**
     * 堆垛机移动完成任务传递
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/6/5 10:11
     */
    public void mlMoveFinishTransferTasks(String blockName) {
        //  任务分配或执行未完成任务
        WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
        String mlBlockMcKey = mlBlock.getMckey();
        String mlBlockAppointmentMcKey = mlBlock.getAppointmentMckey();
        String withWorkBlockName = mlBlock.getWithWorkBlockName();
        String AppointmentWithWorkBlockName = mlBlock.getReserved1();
        if (StringUtils.isNotEmpty(mlBlockMcKey)) {
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            //  优先卸车判断
            if (SystemCache.OUT_STORAGE_OFF_CAR) {
                if (StringUtils.isNotEmpty(mlBlockAppointmentMcKey)) {
                    if (StringUtils.isNotEmpty(mlBlock.getScBlockName()) && mlBlock.getScBlockName().equals(AppointmentWithWorkBlockName)) {
                        BlockServiceImplFactory.blockServiceDoKey(AppointmentWithWorkBlockName);
                        return;
                    }
                }
            }
            //  执行未完成任务
            if (MachineService.isClMachine(withWorkBlockName)) {
                BlockServiceImplFactory.blockServiceDoKey(withWorkBlockName);
            } else if (MachineService.isScMachine(withWorkBlockName)) {
                //  穿梭车任务处理
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
     * 堆垛机接车完成后任务传递
     *
     * @param msgMcKey   消息中mckey
     * @param blockName  数据block名称
     * @param loadCar    载车
     * @param loadStatus 载荷
     * @author CalmLake
     * @date 2019/6/5 10:09
     */
    public void mlGetCarFinishTransferTasks(String msgMcKey, String blockName, String loadCar, String loadStatus) {
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",msgMcKey));
        Integer workPlanType = workPlan.getType();
        String toStation = workPlan.getToStation();
        //  校验小车上车完成
        while (true) {
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(loadCar);
            if (StringUtils.isNotEmpty(scBlock.getHostBlockName()) && blockName.equals(scBlock.getHostBlockName())) {
                break;
            }
            SleepUtil.sleep(0.2);
        }
        if (WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                //  任务结束 清除任务
                charge(blockName, loadCar, BlockConstant.STATUS_RUNNING, msgMcKey, workPlan.getId());
                ChargeLocationUtil.getInstance().recycleLocation(loadCar);
                LockCache.getValue(blockName).signal();
            } else if (CompanyConstant.SYS_NAME_COMPANY_YONG_XIANG.equals(SystemCache.SYS_NAME_COMPANY) || CompanyConstant.SYS_NAME_COMPANY_JIA_TIAN.equals(SystemCache.SYS_NAME_COMPANY)) {
                if (blockName.equals(toStation)) {
                    charge(blockName, loadCar, BlockConstant.STATUS_RUNNING, msgMcKey, workPlan.getId());
                    ChargeLocationUtil.getInstance().recycleLocation(loadCar);
                } else {
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            }
        } else if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType) {
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                //  任务结束 清除任务
                charge(blockName, loadCar, BlockConstant.STATUS_CHARGE, msgMcKey, workPlan.getId());
                LockCache.getValue(blockName).signal();
            } else {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        } else if (WorkPlanConstant.TYPE_GET_CAR==workPlanType) {
            WcsMlblockDaoImpl.getInstance().updateMcKey("", blockName);
            // 接车任务 子车上车后结束 工作计划完成
            WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
            BlockServiceImplFactory.blockServiceDoKey(blockName);
            LockCache.getValue(blockName).signal();
        } else {
            if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
                if (MsgCycleOrderConstant.LOAD_STATUS_HAVE_CAR_LOAD.equals(loadStatus)) {
                    //  接车接货完成 当前设备分配任务
                    String nextBlockName = RouteService.getRouteNextBlockName(blockName, toStation);
                    if (nextBlockName.contains(MachineConstant.TYPE_CL)) {
                        WcsClblockEntity clBlock = WcsClblockDaoImpl.getInstance().selectByPrimaryKey(nextBlockName);
                        if (StringUtils.isNotEmpty(clBlock.getMckey()) && StringUtils.isEmpty(clBlock.getAppointmentMckey())) {
                            DbUtil.getProcedureOrcaleDao().spUpdateBlockClblockAppointmckeyIn(blockName, nextBlockName, msgMcKey, MachineConstant.BYTE_TYPE_ML);
                        } else if (StringUtils.isEmpty(clBlock.getMckey()) && StringUtils.isEmpty(clBlock.getAppointmentMckey())) {
                            DbUtil.getProcedureOrcaleDao().spUpdateBlockClblockMckeyIn(blockName, nextBlockName, msgMcKey, MachineConstant.BYTE_TYPE_ML);
                        } else {
                            SleepUtil.sleep(350);
                            mlGetCarFinishTransferTasks(msgMcKey, blockName, loadCar, loadStatus);
                        }
                    } else {
                        Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：接车接货完成，任务分配时未解析的交互设备类型，nextBlockName：%s", blockName, nextBlockName));
                    }
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                } else {
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else if (WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION==workPlanType) {
                //  当且仅当载车且处于默认位置时完成任务
                WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                String berthBlockName = mlBlock.getBerthBlockName();
                if (StringUtils.isNotEmpty(loadCar) && loadCar.equals(mlBlock.getBingScBlockName()) && StringUtils.isNotEmpty(berthBlockName) && berthBlockName.equals(toStation)) {
                    DbUtil.getProcedureOrcaleDao().spUpdateBlockScblockClearMckeyIn(blockName, loadCar, MachineConstant.BYTE_TYPE_ML);
                    WorkPlanService.finishWorkPlan(workPlan.getId(), msgMcKey);
                    LockCache.getValue(blockName).signal();
                } else {
                    BlockServiceImplFactory.blockServiceDoKey(blockName);
                }
            } else {
                BlockServiceImplFactory.blockServiceDoKey(blockName);
            }
        }
    }

    /**
     * 充电处理
     *
     * @param blockName  数据block名称
     * @param loadCar    穿梭车名称
     * @param status     设备状态
     * @param mcKey      工作计划标识
     * @param workPlanId 工作计划id
     * @author CalmLake
     * @date 2019/3/27 9:59
     */
    private void charge(String blockName, String loadCar, String status, String mcKey, Integer workPlanId) {
        Integer machineType = 0;
        if (blockName.contains(MachineConstant.TYPE_ML)) {
            machineType = MachineConstant.BYTE_TYPE_ML;
        } else if (blockName.contains(MachineConstant.TYPE_MC)) {
            machineType = MachineConstant.BYTE_TYPE_MC;
        } else {
            Log4j2Util.getMsgCustomerLogger().info(String.format("%s，动作：充电任务完成，未解析的设备类型", blockName));
            return;
        }
        DbUtil.getProcedureOrcaleDao().spUpdateBlockScblockClearMckeyIn(blockName, loadCar, machineType);
        WcsScblockDaoImpl.getInstance().updateStatus(loadCar, status);
        WorkPlanService.finishWorkPlan(workPlanId, mcKey);
        LockCache.getValue(blockName).signal();
    }

    /**
     * 充电处理
     *
     * @param blockName  数据block名称
     * @param loadCar    穿梭车名称
     * @param status     设备状态
     * @param mcKey      工作计划标识
     * @param workPlanId 工作计划id
     * @author CalmLake
     * @date 2019/3/27 9:59
     */
    private void charge(String blockName, String loadCar, String status, String mcKey, int workPlanId, Integer machineType) {
        DbUtil.getProcedureOrcaleDao().spUpdateBlockScblockClearMckeyIn(blockName, loadCar, machineType);
        WcsScblockDaoImpl.getInstance().updateStatus(loadCar, status);
        WorkPlanService.finishWorkPlan(workPlanId, mcKey);
        LockCache.getValue(blockName).signal();
    }
}
