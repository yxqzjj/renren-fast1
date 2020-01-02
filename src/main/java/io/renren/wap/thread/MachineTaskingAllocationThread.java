package io.renren.wap.thread;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.*;
import io.renren.modules.generator.entity.*;
import io.renren.wap.cache.MachineCache;
import io.renren.wap.cache.SystemCache;
import io.renren.wap.constant.company.WuHanYouJiConstant;
import io.renren.wap.entity.constant.BlockConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import io.renren.wap.entity.constant.MachineConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.service.AssigningTaskService;
import io.renren.wap.service.StandbyCarService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.service.block.impl.BlockService;
import io.renren.wap.service.block.impl.BlockServiceImplFactory;
import io.renren.wap.util.DateFormatUtil;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.SleepUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * rgv/母车/堆垛机任务分配（含预约任务分配）
 *
 * @Author: CalmLake
 * @Date: 2019/1/28  10:10
 * @Version: V1.0.0
 **/
public class MachineTaskingAllocationThread implements Runnable {
    @Override
    public void run() {
        LogManager.getLogger().info("任务分配启动！");
        while (true) {
            try {
                clearBlockNameTable();
                //  查找待分配任务
                List<WcsTaskingEntity> taskingList;
                if (CompanyConstant.SYS_NAME_COMPANY_YONG_XIANG.equals(SystemCache.SYS_NAME_COMPANY)) {
                    taskingList = DbUtil.getTaskingDao().getOneWarehouseListByProc();
                } else if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                    taskingList = DbUtil.getTaskingDao().getWarehousesListByProc();
                } else {
                    taskingList = new ArrayList<>();
                }
                if (taskingList.size() > 0) {
                    tasking(taskingList);
                }
                if (SystemCache.AUTO_BACK_LOCATION) {
                    goBackDefaultLocation();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log4j2Util.getAssigningTaskLogger().error("任务分配出错,任务分配线程继续工作！", e);
            } finally {
                SleepUtil.sleep(SystemCache.SELECT_TASKING_TIME);
            }
        }
    }

    /**
     * 当没有任务时创建返回默认位置任务
     *
     * @author CalmLake
     * @date 2019/3/8 16:58
     */
    private void goBackDefaultLocation() {
        //  默认  载车回原点（按项目设计的某一固定位置）
        List<WcsMachineEntity> machineList = MachineCache.getHaveDefaultLocationMachine();
        for (WcsMachineEntity machine : machineList) {
            WcsScblockEntity scBlock;
            String scBlockName;
            String bingScBlockName;
            String berthBlockName;
            String mcKey;
            String appointmentMcKey;
            String defaultLocation = machine.getDefaultLocation();
            String blockName = machine.getBlockName();
            String chargeMachineBlockName;
            if (MachineConstant.BYTE_TYPE_ML.equals(machine.getType())) {
                WcsMlblockEntity mlBlock = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                scBlockName = mlBlock.getScBlockName();
                bingScBlockName = StandbyCarService.getScBlockName(mlBlock);
                berthBlockName = mlBlock.getBerthBlockName();
                chargeMachineBlockName = mlBlock.getReserved2();
                mcKey = mlBlock.getMckey();
                appointmentMcKey = mlBlock.getAppointmentMckey();
                scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(bingScBlockName);
            } else if (MachineConstant.BYTE_TYPE_MC.equals(machine.getType())) {
                WcsMcblockEntity mcBlock = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockName);
                scBlockName = mcBlock.getScBlockName();
                bingScBlockName = mcBlock.getBingScBlockName();
                berthBlockName = mcBlock.getBerthBlockName();
                chargeMachineBlockName = mcBlock.getReserved2();
                mcKey = mcBlock.getMckey();
                appointmentMcKey = mcBlock.getAppointmentMckey();
                scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(bingScBlockName);
            } else {
                Log4j2Util.getAssigningTaskLogger().info(String.format("%s 该设备类型没有解析回原点", blockName));
                break;
            }
            boolean result = false;
            int workPlanNumGoBack = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                    .eq("To_Station",defaultLocation)
                    .eq("From_Station",blockName)
                    .eq("Status", WorkPlanConstant.STATUS_WAIT).or()
                    .eq("Status",WorkPlanConstant.STATUS_WORKING)
            );
            if (StringUtils.isEmpty(scBlockName) || StringUtils.isEmpty(berthBlockName) || !defaultLocation.equals(berthBlockName)) {
                result = true;
            }
            int resultNum = 1;
            if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
                if (StringUtils.isEmpty(mcKey) && StringUtils.isEmpty(appointmentMcKey)) {
                    if (StringUtils.isEmpty(scBlock.getMckey()) && StringUtils.isEmpty(scBlock.getAppointmentMckey())) {
                        resultNum = DbUtil.getProcedureDao().spSelectTaskingMlMcScOut(blockName, bingScBlockName);
                    }
                }
            } else {
                resultNum = DbUtil.getProcedureDao().spSelectCanCreateGoBackDefaultLocationInOut(blockName, chargeMachineBlockName, bingScBlockName, defaultLocation);
            }
            if (result && resultNum < 1 && workPlanNumGoBack < 1) {
                WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan("", DateFormatUtil.getStringHHmmss(), "000000000", defaultLocation, "", WorkPlanConstant.TYPE_GO_BACK_DEFAULT_LOCATION, blockName, "000000000");
                AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                assigningTaskService.assigningTasks();
            }
        }
    }

    /**
     * 任务分配
     *
     * @param taskingList 未分配任务数据
     * @author CalmLake
     * @date 2019/1/28 13:38
     */
    private void tasking(List<WcsTaskingEntity> taskingList) {
        for (WcsTaskingEntity tasking : taskingList) {
            Integer id = tasking.getId();
            String mcKey = tasking.getMckey();
            String blockName = tasking.getBlockName();
            String nextBlockName = tasking.getNextBlockName();
            Object objectBlockName = DbUtil.getBlockDao(blockName);
            Object objectNextBlockName = DbUtil.getBlockDao(nextBlockName);
            String blockJson = getBlockInfo(objectBlockName, blockName);
            String nextBlockJson = getBlockInfo(objectNextBlockName, nextBlockName);
            if (isCanTasking(blockName) && isCanTasking(nextBlockName)) {
                judgeBlockAndTasking(blockJson, nextBlockJson, mcKey, id);
            } else {
                if (blockName.contains(MachineConstant.TYPE_ML) && nextBlockName.contains(MachineConstant.TYPE_SC)) {
                    if (SystemCache.OUT_STORAGE_OFF_CAR) {
                        int numWorkPlanMovement = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                                .eq("From_Station",blockName)
                                .eq("Status", WorkPlanConstant.TYPE_MOVEMENT).or()
                                .eq("Status",WorkPlanConstant.TYPE_TALLY)
                        );
                        if (numWorkPlanMovement < 1) {
                            //  该堆垛机是否有移库任务如果有移库任务则不进行出库优先卸车
                            WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
                            Integer workPlanType = workPlan.getType();
                            //  出库优先卸车
                            if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
                                judgeBlockAndTasking(blockJson, nextBlockJson, mcKey, id);
                            } else {
                                //  添加不可分配任务设备
                                insertBlockName(blockName);
                            }
                        }
                    } else {
                        //  添加不可分配任务设备
                        insertBlockName(blockName);
                    }
                } else {
                    //  添加不可分配任务设备
                    insertBlockName(blockName);
                }
            }
        }
    }

    /**
     * 功能描述  获取block运行信息
     *
     * @param object    block表操作对象
     * @param blockName block名称
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/29 8:59
     */
    private String getBlockInfo(Object object, String blockName) {
        JSONObject jsonObject = new JSONObject();
        String command = null;
        String mcKey = null;
        String appointmentMcKey = null;
        String error = null;
        String status = null;
        boolean result = true;
        boolean car = false;
        if (object instanceof WcsMlblockDaoImpl) {
            WcsMlblockEntity mlBlock = ((WcsMlblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = mlBlock.getCommand();
            mcKey = mlBlock.getMckey();
            appointmentMcKey = mlBlock.getAppointmentMckey();
            error = mlBlock.getErrorCode();
            status = mlBlock.getStatus();
            if (StringUtils.isEmpty(mlBlock.getScBlockName())) {
                car = true;
            }
        } else if (object instanceof WcsScblockDaoImpl) {
            WcsScblockEntity scBlock = ((WcsScblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = scBlock.getCommand();
            mcKey = scBlock.getMckey();
            appointmentMcKey = scBlock.getAppointmentMckey();
            error = scBlock.getErrorCode();
            status = scBlock.getStatus();
            if (BlockConstant.STATUS_CHARGE.equals(status)) {
                result = false;
            }
        } else if (object instanceof WcsMcblockDaoImpl) {
            WcsMcblockEntity mcBlock = ((WcsMcblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = mcBlock.getCommand();
            mcKey = mcBlock.getMckey();
            appointmentMcKey = mcBlock.getAppointmentMckey();
            error = mcBlock.getErrorCode();
            status = mcBlock.getStatus();
            if (StringUtils.isEmpty(mcBlock.getScBlockName())) {
                car = true;
            }
        } else if (object instanceof WcsRgvblockDaoImpl) {
            WcsRgvblockEntity rgvBlock = ((WcsRgvblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = rgvBlock.getCommand();
            mcKey = rgvBlock.getMckey();
            appointmentMcKey = rgvBlock.getAppointmentMckey();
            error = rgvBlock.getErrorCode();
            status = rgvBlock.getStatus();
        } else if (object instanceof WcsAlblockDaoImpl) {
            WcsAlblockEntity alBlock = ((WcsAlblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = alBlock.getCommand();
            mcKey = alBlock.getMckey();
            appointmentMcKey = alBlock.getAppointmentMckey();
            error = alBlock.getErrorCode();
            status = alBlock.getStatus();
        } else if (object instanceof WcsClblockDaoImpl) {
            WcsClblockEntity clBlock = ((WcsClblockDaoImpl) object).selectByPrimaryKey(blockName);
            command = clBlock.getCommand();
            mcKey = clBlock.getMckey();
            appointmentMcKey = clBlock.getAppointmentMckey();
            error = clBlock.getErrorCode();
            status = clBlock.getStatus();
        } else {
            Log4j2Util.getAssigningTaskLogger().info(String.format("%s,任务分配解析操作表对象失败，未解析的对象类型", blockName));
        }
        jsonObject.put("blockName", blockName);
        jsonObject.put("command", command);
        jsonObject.put("mcKey", mcKey);
        jsonObject.put("appointmentMcKey", appointmentMcKey);
        jsonObject.put("error", error);
        jsonObject.put("status", status);
        jsonObject.put("result", result);
        jsonObject.put("car", car);
        return jsonObject.toJSONString();
    }

    /**
     * 判断设备状态分配任务
     *
     * @param jsonBlock     block运行信息
     * @param jsonNextBlock 交互block运行信息
     * @param mcKey         任务标识
     * @param taskingID     当前分配任务id
     * @author CalmLake
     * @date 2019/1/29 9:03
     */
    private void judgeBlockAndTasking(String jsonBlock, String jsonNextBlock, String mcKey, int taskingID) {
        JSONObject jsonObjectBlock = JSONObject.parseObject(jsonBlock);
        JSONObject jsonObjectNextBlock = JSONObject.parseObject(jsonNextBlock);
        boolean result1 = jsonObjectBlock.getBooleanValue("result");
        boolean result2 = jsonObjectNextBlock.getBooleanValue("result");
        String blockName = jsonObjectBlock.getString("blockName");
        String blockMcKey = jsonObjectBlock.getString("mcKey");
        String blockAppointmentMcKey = jsonObjectBlock.getString("appointmentMcKey");
        String blockError = jsonObjectBlock.getString("error");
        String blockNextName = jsonObjectNextBlock.getString("blockName");
        String blockNextMcKey = jsonObjectNextBlock.getString("mcKey");
        String blockNextAppointmentMcKey = jsonObjectNextBlock.getString("appointmentMcKey");
        String blockNextError = jsonObjectNextBlock.getString("error");
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectOne(new QueryWrapper<WcsWorkplanEntity>().eq("McKey",mcKey));
        Integer workPlanId = workPlan.getId();
        String fromStation = workPlan.getFromStation();
        String toStation = workPlan.getToStation();
        Integer workPlanStatus = workPlan.getStatus();
        Integer workPlanType = workPlan.getType();
        if (WorkPlanConstant.TYPE_OFF_CAR==workPlanType) {
            //  穿梭车在充电中卸车任务直接完成
            WcsScblockEntity scBlock = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(fromStation);
            if (BlockConstant.STATUS_CHARGE.equals(scBlock.getStatus())) {
                DbUtil.getTaskingDao().deleteById(taskingID);
                WorkPlanService.finishWorkPlan(workPlanId, mcKey);
            }
        }
        if (!result1 || !result2) {
            if (WorkPlanConstant.TYPE_CHARGE_COMPLETE!=workPlanType) {
                Log4j2Util.getAssigningTaskLogger().info(String.format("mcKey：%s，设备状态不正确，设备处于充电状态！", mcKey));
                return;
            }
        }
        if (BlockService.isNotError(blockError) && BlockService.isNotError(blockNextError)) {
            int workPlanSize;
            if (!blockName.contains(MachineConstant.TYPE_CL) && !blockNextName.contains(MachineConstant.TYPE_CL)) {
                if (!isWorkPlanCanTasking(workPlanType, fromStation, toStation)) {
                    Log4j2Util.getAssigningTaskLogger().info(String.format("mcKey：%s，workPlanType: %d，该工作计划类型与其它该设备工作计划类型冲突，其它类型优先执行", mcKey, workPlanType));
                    return;
                }
            }
            if (WorkPlanConstant.STATUS_WAIT==workPlanStatus) {
                //  检查路径是否冲突
                workPlanSize = routeCanUse(blockName, toStation, fromStation, workPlanType);
                if (workPlanSize < 1) {
                    taskWorkPlan(blockMcKey, blockAppointmentMcKey, blockNextMcKey, blockNextAppointmentMcKey, blockName, blockNextName, mcKey, taskingID, workPlan);
                }
            } else if (WorkPlanConstant.STATUS_WORKING==workPlanStatus) {
                if (WorkPlanConstant.TYPE_CHARGE_UP==workPlanType || WorkPlanConstant.TYPE_CHARGE_COMPLETE==workPlanType) {
                    runningWorkTasking(blockMcKey, blockAppointmentMcKey, blockName, blockNextName, mcKey, blockNextMcKey, blockNextAppointmentMcKey, taskingID, null);
                } else {
                    WcsMachineEntity machine = MachineCache.getMachine(blockNextName);
                    String bingScBlockName = "";
                    if (MachineConstant.BYTE_TYPE_MC.equals(machine.getType())) {
                        bingScBlockName = WcsMcblockDaoImpl.getInstance().selectByPrimaryKey(blockNextName).getBingScBlockName();
                    } else if (MachineConstant.BYTE_TYPE_ML.equals(machine.getType())) {
                        bingScBlockName = WcsMlblockDaoImpl.getInstance().selectByPrimaryKey(blockNextName).getBingScBlockName();
                    }
                    if (StringUtils.isNotEmpty(bingScBlockName)) {
                        String scStatus = WcsScblockDaoImpl.getInstance().selectByPrimaryKey(bingScBlockName).getStatus();
                        if (BlockConstant.STATUS_CHARGE.equals(scStatus)) {
                            if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType) {
                                return;
                            }
                        }
                    }
                    runningWorkTasking(blockMcKey, blockAppointmentMcKey, blockName, blockNextName, mcKey, blockNextMcKey, blockNextAppointmentMcKey, taskingID, null);
                }
            } else {
                Log4j2Util.getAssigningTaskLogger().info(String.format("设备状态未解析，workPlanStatus：%s", Integer.toString(workPlanStatus)));
            }
        }
    }

    /**
     * 运行任务分配及制作消息
     *
     * @param blockMcKey                设备当前任务标识
     * @param blockAppointmentMcKey     设备预约任务标识
     * @param blockNextMcKey            下一设备当前任务标识
     * @param blockNextAppointmentMcKey 下一设备预约任务标识
     * @param blockName                 设备block名称
     * @param blockNextName             下一设备block名称
     * @param mcKey                     任务标识
     * @param taskingID                 任务序号
     * @param crossRoute                交叉路径信息
     * @author CalmLake
     * @date 2019/3/12 9:57
     */
    private void runningWorkTasking(String blockMcKey, String blockAppointmentMcKey, String blockName, String blockNextName, String mcKey, String blockNextMcKey, String blockNextAppointmentMcKey, int taskingID, WcsCrossrouteEntity crossRoute) {
        int result_1 = 0;
        int result_2 = 0;
        if (StringUtils.isEmpty(blockMcKey) && StringUtils.isEmpty(blockAppointmentMcKey)) {
            result_1 = taskingUpdate(blockName, blockNextName, mcKey);
        }
        if (StringUtils.isEmpty(blockNextMcKey) && StringUtils.isEmpty(blockNextAppointmentMcKey)) {
            result_2 = taskingUpdate(blockNextName, blockName, mcKey);
        }
        if (result_2 > 0 || result_1 > 0) {
            taskingFinishOperation(taskingID, blockName, blockNextName);
            if (crossRoute != null) {
                DbUtil.getCrossRouteDao().update(crossRoute,(Wrapper<WcsCrossrouteEntity>) new Object());
            }
        }
    }

    /**
     * 新任务分配及制作消息
     *
     * @param blockMcKey                设备当前任务标识
     * @param blockAppointmentMcKey     设备预约任务标识
     * @param blockNextMcKey            下一设备当前任务标识
     * @param blockNextAppointmentMcKey 下一设备预约任务标识
     * @param blockName                 设备block名称
     * @param blockNextName             下一设备block名称
     * @param mcKey                     任务标识
     * @param taskingID                 任务序号
     * @param workPlan                  工作计划
     * @author CalmLake
     * @date 2019/3/12 9:57
     */
    private void taskWorkPlan(String blockMcKey, String blockAppointmentMcKey, String blockNextMcKey, String blockNextAppointmentMcKey, String blockName, String blockNextName, String mcKey, int taskingID, WcsWorkplanEntity workPlan) {
        int workPlanId = workPlan.getId();
        String fromStation = workPlan.getFromStation();
        Integer workPlanType = workPlan.getType();
        if (StringUtils.isEmpty(blockMcKey) && StringUtils.isEmpty(blockAppointmentMcKey) && StringUtils.isEmpty(blockNextMcKey) && StringUtils.isEmpty(blockNextAppointmentMcKey)) {
            //  分配任务 两个设备mcKey
            //  制作两个设备消息
            taskingUpdateBlock(taskingID, blockName, blockNextName, mcKey, workPlanId);
        } else if (StringUtils.isNotEmpty(blockMcKey) && StringUtils.isEmpty(blockAppointmentMcKey)) {
            int numWorkPlanMovement = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                    .eq("From_Station",fromStation)
                    .eq("Status", WorkPlanConstant.TYPE_MOVEMENT).or()
                    .eq("Status",WorkPlanConstant.TYPE_TALLY)
            );
            if (numWorkPlanMovement < 1) {
                if (SystemCache.OUT_STORAGE_OFF_CAR) {
                    //  该堆垛机是否有移库任务如果有移库任务则不进行出库优先卸车
                    //  出库优先卸车
                    if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
                        int result_1 = 0;
                        int result_2 = 0;
                        if (StringUtils.isEmpty(blockNextMcKey) && StringUtils.isEmpty(blockNextAppointmentMcKey)) {
                            result_1 = taskingAppointmentMcKeyUpdate(blockName, blockNextName, mcKey);
                            result_2 = taskingUpdate(blockNextName, blockName, mcKey);
                            //  给穿梭车优先卸车标识
                            WcsScblockDaoImpl.getInstance().updateReserved2ByName(blockNextName, BlockConstant.SC_PRIORITY_OFF_CAR_RESERVED2);
                        } else if (StringUtils.isNotEmpty(blockNextMcKey) && StringUtils.isEmpty(blockNextAppointmentMcKey)) {
                            result_1 = taskingAppointmentMcKeyUpdate(blockName, blockNextName, mcKey);
                            result_2 = taskingAppointmentMcKeyUpdate(blockNextName, blockName, mcKey);
                            WcsScblockDaoImpl.getInstance().updateReserved2ByName(blockNextName, BlockConstant.SC_PRIORITY_OFF_CAR_RESERVED2);
                        }
                        if (result_1 > 0 && result_2 > 0) {
                            WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
                            wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
                            wcsWorkplanEntity.setStartTime(new Date());
                            DbUtil.getTaskingDao().deleteById(taskingID);
                            insertBlockName(blockName);
                        }
                    }
                }
            }
        } else {
            insertBlockName(blockName);
        }
    }

    /**
     * 交叉路径数量判断
     *
     * @param blockName    数据block名称
     * @param toStation    目标站台号
     * @param fromStation  源站台号
     * @param workPlanType 工作计划类型
     * @return int
     * @author CalmLake
     * @date 2019/1/28 17:49
     */
    private int routeCanUse(String blockName, String toStation, String fromStation, Integer workPlanType) {
        int workPlanSize;
        if (CompanyConstant.SYS_NAME_COMPANY_YOU_JI.equals(SystemCache.SYS_NAME_COMPANY)) {
            if (blockName.equals(WuHanYouJiConstant.STATION_ML02)) {
                if (toStation.equals(WuHanYouJiConstant.STATION_1202) || toStation.equals(WuHanYouJiConstant.STATION_1201)) {
                    if (WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType) {
                        workPlanSize = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                                .eq("To_Station",blockName)
                                .eq("Status",WorkPlanConstant.STATUS_WORKING)
                                .eq("From_Station",WuHanYouJiConstant.STATION_1101)
                                .or().eq("From_Station",WuHanYouJiConstant.STATION_1103));
                    } else {
                        workPlanSize = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                                .eq("To_Station",toStation)
                                .eq("Status",WorkPlanConstant.STATUS_WORKING)
                                .eq("From_Station",fromStation));
                    }
                } else {
                    workPlanSize = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                            .eq("To_Station",toStation)
                            .eq("Status",WorkPlanConstant.STATUS_WORKING)
                            .eq("From_Station",fromStation));                }
            } else {
                workPlanSize = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                        .eq("To_Station",toStation)
                        .eq("Status",WorkPlanConstant.STATUS_WORKING)
                        .eq("From_Station",fromStation));            }
        } else {
            //  1.路径直来直去 设备只有输送线-母车类设备-穿梭车 或加个升降机
            workPlanSize = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                    .eq("To_Station",toStation)
                    .eq("Status",WorkPlanConstant.STATUS_WORKING)
                    .eq("From_Station",fromStation));
        }
        return workPlanSize;
    }

    /**
     * 堆垛机，rgv，母车，穿梭车预约任务分配
     *
     * @param blockName     数据block名称
     * @param blockNameNext 一起交互工作数据block名称
     * @param mcKey         任务标识
     * @author CalmLake
     * @date 2019/1/28 17:45
     */
    private int taskingAppointmentMcKeyUpdate(String blockName, String blockNameNext, String mcKey) {
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        if (MachineConstant.BYTE_TYPE_ML.equals(machine.getType())) {
            return WcsMlblockDaoImpl.getInstance().updateAppointmentMcKeyReserved1ByName(blockName, mcKey, blockNameNext);
        } else if (MachineConstant.BYTE_TYPE_MC.equals(machine.getType())) {
            return WcsMcblockDaoImpl.getInstance().updateAppointmentMcKeyReserved1ByName(blockName, mcKey, blockNameNext);
        } else if (MachineConstant.BYTE_TYPE_SC.equals(machine.getType())) {
            return WcsScblockDaoImpl.getInstance().updateAppointmentMcKeyReserved1ByName(blockName, mcKey, blockNameNext);
        } else {
            Log4j2Util.getAssigningTaskLogger().info(String.format("未识别的数据block类型，blockName：%s", blockName));
            return -1;
        }
    }

    /**
     * 堆垛机，rgv，母车，穿梭车任务分配
     *
     * @param blockName     数据block名称
     * @param blockNameNext 一起交互工作数据block名称
     * @param mcKey         任务标识
     * @author CalmLake
     * @date 2019/1/28 17:45
     */
    private int taskingUpdate(String blockName, String blockNameNext, String mcKey) {
        WcsMachineEntity machine = MachineCache.getMachine(blockName);
        if (MachineConstant.BYTE_TYPE_RGV.equals(machine.getType())) {
            return WcsRgvblockDaoImpl.getInstance().updateTwoRgvBlock(mcKey, blockNameNext, blockName);
        } else if (MachineConstant.BYTE_TYPE_ML.equals(machine.getType())) {
            return WcsMlblockDaoImpl.getInstance().updateTwoMlBlock(mcKey, blockNameNext, blockName);
        } else if (MachineConstant.BYTE_TYPE_MC.equals(machine.getType())) {
            return WcsMcblockDaoImpl.getInstance().updateTwoMcBlock(mcKey, blockNameNext, blockName);
        } else if (MachineConstant.BYTE_TYPE_SC.equals(machine.getType())) {
            return WcsScblockDaoImpl.getInstance().updateTwoScBlock(mcKey, blockNameNext, blockName);
        } else if (MachineConstant.BYTE_TYPE_AL.equals(machine.getType())) {
            return WcsAlblockDaoImpl.getInstance().updateTwoALBlock(mcKey, blockNameNext, blockName);
        } else if (MachineConstant.BYTE_TYPE_CL.equals(machine.getType())) {
            return WcsClblockDaoImpl.getInstance().updateTwoCLBlock(mcKey, blockNameNext, blockName);
        } else {
            Log4j2Util.getAssigningTaskLogger().info(String.format("未识别的数据block类型，blockName：%s", blockName));
            return -1;
        }
    }

    /**
     * 任务分配修改DB
     *
     * @param taskingID     任务分配表id
     * @param blockName     数据block名称
     * @param blockNextName 交互数据block名称
     * @param mcKey         工作计划标识
     * @param workPlanId    工作计划id
     * @author CalmLake
     * @date 2019/3/27 15:01
     */
    private void taskingUpdateBlock(int taskingID, String blockName, String blockNextName, String mcKey, int workPlanId) {
        int result_1 = taskingUpdate(blockName, blockNextName, mcKey);
        int result_2 = taskingUpdate(blockNextName, blockName, mcKey);
        if (result_1 > 0 && result_2 > 0) {
            WcsWorkplanEntity wcsWorkplanEntity=DbUtil.getWorkPlanDao().selectById(workPlanId);
            wcsWorkplanEntity.setStartTime(new Date());
            wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_WORKING);
            DbUtil.getWorkPlanDao().updateById(wcsWorkplanEntity);
            taskingFinishOperation(taskingID, blockName, blockNextName);
        }
    }

    /**
     * 任务分配完成操作 修改DB及制作消息 添加不可分配任务设备信息
     *
     * @param taskingID     任务分配表id
     * @param blockName     数据block名称
     * @param blockNextName 交互数据block名称
     * @author CalmLake
     * @date 2019/3/27 14:39
     */
    private void taskingFinishOperation(int taskingID, String blockName, String blockNextName) {
        DbUtil.getTaskingDao().deleteById(taskingID);
        BlockServiceImplFactory.blockServiceDoKey(blockName);
        BlockServiceImplFactory.blockServiceDoKey(blockNextName);
        insertBlockName(blockName);
        insertBlockName(blockNextName);
    }

    /**
     * 插入一条新的不可分配任务设备记录
     *
     * @param blockName 数据block名称
     * @author CalmLake
     * @date 2019/4/2 16:45
     */
    private void insertBlockName(String blockName) {
        WcsBlocknameEntity blockNameObject = new WcsBlocknameEntity();
        blockNameObject.setBlockName(blockName);
        DbUtil.getBlockNameDao().insert(blockNameObject);
    }

    /**
     * 清除不可分配任务设备记录
     *
     * @author CalmLake
     * @date 2019/4/2 16:48
     */
    private void clearBlockNameTable() {
        DbUtil.getBlockNameDao().delete(new QueryWrapper<>());
    }

    /**
     * 该设备是否可以分配任务
     *
     * @param blockName 数据block名称
     * @return int
     * @author CalmLake
     * @date 2019/4/2 16:49
     */
    private boolean isCanTasking(String blockName) {
        int num = DbUtil.getBlockNameDao().selectCount(new QueryWrapper<WcsBlocknameEntity>().eq("Block_Name",blockName));
        return num < 1;
    }

    /**
     * 当前计划使用设备是否有其余工作计划类型（如：换层，充电开始，充电完成，回原点，卸车，下车）
     *
     * @param workPlanType 工作计划类型
     * @param fromStation  源站台
     * @param toStation    目标站台
     * @return boolean
     * @author CalmLake
     * @date 2019/4/12 15:20
     */
    private boolean isWorkPlanCanTasking(Integer workPlanType, String fromStation, String toStation) {
        boolean result = false;
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType || WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType || WorkPlanConstant.TYPE_MOVEMENT==workPlanType || WorkPlanConstant.TYPE_TALLY==workPlanType || WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType) {
            int num = DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>()
                    .in("Status",1,2)
                    .in("Type",6,7,8,9,10,11)
                    .eq("From_Station",fromStation)
                    .or().eq("To_Station",toStation)
                    .or().eq("To_Station",fromStation)
                    .eq("From_Station",toStation)
            );
            if (num < 1) {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }
}
