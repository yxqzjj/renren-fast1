package io.renren.wap.service;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsRouteStationStartEndDaoImpl;
import io.renren.modules.generator.entity.WcsAutocreateworkplanEntity;
import io.renren.modules.generator.entity.WcsMachineEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.wap.cache.BlockQueueCache;
import io.renren.wap.cache.PriorityCache;
import io.renren.wap.client.singleton.CreateSequenceNumberSingleton;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.server.xml.constant.TransportOrderConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.server.xml.dto.node.TransportOrderDTO;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import io.renren.wap.util.McKeyUtil;
import io.renren.wap.util.SleepUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 工作计划
 *
 * @Author: CalmLake
 * @Date: 2019/1/7  15:08
 * @Version: V1.0.0
 **/

public class WorkPlanService {

    public WorkPlanService() {
    }

    /**
     * 完成工作计划DB操作（修改完成时间，状态和删除该任务的命令消息）
     *
     * @param workPlanId 工作计划id
     * @param mcKey      任务标识
     * @author CalmLake
     * @date 2019/3/6 14:30
     */
    public static void finishWorkPlan(int workPlanId, String mcKey) {
        WcsWorkplanEntity wcsWorkplanEntity= DbUtil.getWorkPlanDao().selectById(workPlanId);
        wcsWorkplanEntity.setFinishTime(new Date());
        wcsWorkplanEntity.setStatus(WorkPlanConstant.STATUS_FINISH);
        int i = 0;
        boolean result = false;
        SleepUtil.sleep(5);
        while (i < WorkPlanConstant.FINISH_WORK_PLAN_OPERATION_MAX) {
            if (createWorkPlanLog(workPlanId) > 0) {
                result = true;
                break;
            }
            SleepUtil.sleep(1);
            i++;
        }
        if (result) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mcKey", mcKey);
                jsonObject.put("workPlanId", workPlanId);
                BlockQueueCache.addWorkPlanFishString(jsonObject.toJSONString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log4j2Util.getWorkPlanLogger().info(String.format("%d,mcKey : %s ,任务删除队列异常", workPlanId, mcKey));
            }
        } else {
            Log4j2Util.getWorkPlanLogger().info(String.format("%d,mcKey : %s ,任务完成操作DB失败", workPlanId, mcKey));
        }
    }

    /**
     * 新建完成工作计划记录
     *
     * @param workPlanId 工作计划序号
     * @return int
     * @author CalmLake
     * @date 2019/3/15 16:02
     */
    public static int createWorkPlanLog(int workPlanId) {
        WcsWorkplanEntity workPlan = DbUtil.getWorkPlanDao().selectById(workPlanId);
        WcsWorkplanlogEntity workPlanLog = new WcsWorkplanlogEntity();
        workPlanLog.setBarcode(workPlan.getBarcode());
        workPlanLog.setCreateTime(workPlan.getCreateTime());
        workPlanLog.setFinishTime(new Date());
        workPlanLog.setFromLocation(workPlan.getFromLocation());
        workPlanLog.setFromStation(workPlan.getFromStation());
        workPlanLog.setMckey(workPlan.getMckey());
        workPlanLog.setPriorityConfigPriority(workPlan.getPriorityConfigPriority());
        workPlanLog.setReserved1(StringUtils.isEmpty(workPlan.getReserved1()) ? "" : workPlan.getReserved1());
        workPlanLog.setReserved2(StringUtils.isEmpty(workPlan.getReserved2()) ? "" : workPlan.getReserved2());
        workPlanLog.setStartTime(workPlan.getStartTime());
        workPlanLog.setStatus(workPlan.getStatus());
        workPlanLog.setToLocation(workPlan.getToLocation());
        workPlanLog.setToStation(workPlan.getToStation());
        workPlanLog.setType(workPlan.getType());
        workPlanLog.setWmsFlag(workPlan.getWmsFlag());
        workPlanLog.setWorkPlanId(workPlanId);
        return DbUtil.getWorkPlanLogDao().InsertProvider(workPlanLog);
    }

    /**
     * 根据wms任务id查找
     *
     * @param wmsFlag wms任务唯一标识
     * @return int
     * @author CalmLake
     * @date 2019/1/9 10:18
     */
    public int selectByWmsFlag(String wmsFlag) {
        return DbUtil.getWorkPlanDao().selectCount(new QueryWrapper<WcsWorkplanEntity>().eq("WMS_Flag",wmsFlag));
    }

    /**
     * 创建演示工作计划（移库）
     *
     * @param autoCreateWorkPlan 自动演示信息
     * @author CalmLake
     * @date 2019/9/3 11:59
     */
    public static void createAutoWorkPlan(WcsAutocreateworkplanEntity autoCreateWorkPlan) {
        String num = CreateSequenceNumberSingleton.getInstance().getMessageNumber();
        String stationA = autoCreateWorkPlan.getStationA();
        String stationB = autoCreateWorkPlan.getStationB();
        String fromStation;
        String endStation;
        String fromLocation;
        String endLocation;
        if (autoCreateWorkPlan.getType() == 1) {
            fromLocation = autoCreateWorkPlan.getLocationA();
            endLocation = autoCreateWorkPlan.getLocationB();
            fromStation = stationA;
            endStation = stationB;
        } else {
            fromLocation = autoCreateWorkPlan.getLocationB();
            endLocation = autoCreateWorkPlan.getLocationA();
            fromStation = stationB;
            endStation = stationA;
        }
        int cargoNum = autoCreateWorkPlan.getCargoNum();
        for (int i = 0; i < cargoNum; i++) {
            WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlanShowTime("", num, endLocation, endStation, num, WorkPlanConstant.TYPE_MOVEMENT, fromStation, fromLocation, "showTime");
            AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
            assigningTaskService.assigningTasks();
            autoCreateWorkPlan.setStatus(0);
        }
    }

    /**
     * 创建一条新的工作计划
     *
     * @return WorkPlan 工作计划
     * @Param envelopeDTO transportOrder 消息
     * @author CalmLake
     * @date 2019/1/7 15:10
     */
    public WcsWorkplanEntity createWorkPlan(EnvelopeDTO envelopeDTO) {
        try {
            TransportOrderDTO transportOrderDTO = envelopeDTO.getTransportOrderDTO();
            String wmsID = transportOrderDTO.getControlAreaDTO().getRefIdDTO().getRefId();
            String transportType = transportOrderDTO.getTransportOrderDataDTO().getTransportType();
            String barcode = transportOrderDTO.getTransportOrderDataDTO().getStUnitId();
            String fromStation = transportOrderDTO.getTransportOrderDataDTO().getFromLocation().getMha();
            List<String> fromLocationList = transportOrderDTO.getTransportOrderDataDTO().getFromLocation().getRack();
            String toStation = transportOrderDTO.getTransportOrderDataDTO().getToLocation().getMha();
            List<String> toLocationList = transportOrderDTO.getTransportOrderDataDTO().getToLocation().getRack();
            String fromLocation = getLocation(fromLocationList);
            String toLocation = getLocation(toLocationList);
            byte workPlanType = getWorkPlanType(transportType);
            return createWorkPlan("", wmsID, toLocation, toStation, barcode, workPlanType, fromStation, fromLocation);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 创建工作计划
     *
     * @param mcKey         任务唯一标识
     * @param wmsId         wms任务唯一标识
     * @param endLocation   目标货位
     * @param endStation    目标站台
     * @param barcode       托盘号
     * @param type          工作计划类型
     * @param startStation  起始站台
     * @param startLocation 起始货位
     * @param reserved1     演示任务标识
     * @return com.wap.entity.WorkPlan
     * @author CalmLake
     * @date 2019/4/9 9:53
     */
    public static WcsWorkplanEntity createWorkPlanShowTime(String mcKey, String wmsId, String endLocation, String endStation, String barcode, int type, String startStation, String startLocation, String reserved1) {
        WcsWorkplanEntity workPlan = new WcsWorkplanEntity();
        WcsMachineEntity machine = DbUtil.getMachineDao().selectOne(new QueryWrapper<WcsMachineEntity>().eq("stationName",startStation));
        workPlan.setStatus(WorkPlanConstant.STATUS_WAIT);
        workPlan.setCreateTime(new Date());
        workPlan.setMckey(StringUtils.isEmpty(mcKey) ? McKeyUtil.getMcKey() : mcKey);
        workPlan.setWmsFlag(wmsId);
        workPlan.setToLocation(endLocation);
        workPlan.setToStation(endStation);
        workPlan.setBarcode(barcode);
        workPlan.setType(type);
        workPlan.setPriorityConfigPriority(type);
        workPlan.setFromStation(startStation);
        workPlan.setFromLocation(startLocation);
        workPlan.setWarehouseNo(machine.getWarehouseNo());
        workPlan.setReserved1(reserved1);
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==type || WorkPlanConstant.TYPE_OUT_PUT_STORAGE==type) {
            int num = WcsRouteStationStartEndDaoImpl.getRouteStationStartEndDao().countNumByFromStationAndEndStation(startStation, endStation);
            if (num > 0) {
                DbUtil.getWorkPlanDao().insertProvider(workPlan);
                Log4j2Util.getWorkPlanLogger().info(String.format("工作计划创建成功，id： %d ,mcKey：%s,type：%d", workPlan.getId(), workPlan.getMckey(), type));
                return workPlan;
            } else {
                Log4j2Util.getWorkPlanLogger().info(String.format("工作计划创建失败，startStation : %s，endStation : %s ,无符合路径起止信息！", startStation, endStation));
                return null;
            }
        } else {
            DbUtil.getWorkPlanDao().insertProvider(workPlan);
            Log4j2Util.getWorkPlanLogger().info(String.format("工作计划创建成功，id： %d ,mcKey：%s,type：%d", workPlan.getId(), workPlan.getMckey(), type));
            return workPlan;
        }
    }

    /**
     * 创建工作计划
     *
     * @param mcKey         任务唯一标识
     * @param wmsId         wms任务唯一标识
     * @param endLocation   目标货位
     * @param endStation    目标站台
     * @param barcode       托盘号
     * @param type          工作计划类型
     * @param startStation  起始站台
     * @param startLocation 起始货位
     * @return com.wap.entity.WorkPlan
     * @author CalmLake
     * @date 2019/4/9 9:53
     */
    public static WcsWorkplanEntity createWorkPlan(String mcKey, String wmsId, String endLocation, String endStation, String barcode, int type, String startStation, String startLocation) {
        WcsWorkplanEntity workPlan = new WcsWorkplanEntity();
        WcsMachineEntity machine = DbUtil.getMachineDao().selectOne(new QueryWrapper<WcsMachineEntity>().eq("stationName",startStation));
        workPlan.setStatus(WorkPlanConstant.STATUS_WAIT);
        workPlan.setCreateTime(new Date());
        workPlan.setMckey(StringUtils.isEmpty(mcKey) ? McKeyUtil.getMcKey() : mcKey);
        workPlan.setWmsFlag(wmsId);
        workPlan.setToLocation(endLocation);
        workPlan.setToStation(endStation);
        workPlan.setBarcode(barcode);
        workPlan.setType(type);
        workPlan.setPriorityConfigPriority(type);
        workPlan.setFromStation(startStation);
        workPlan.setFromLocation(startLocation);
        workPlan.setWarehouseNo(machine.getWarehouseNo());
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==type || WorkPlanConstant.TYPE_OUT_PUT_STORAGE==type) {
            int num = WcsRouteStationStartEndDaoImpl.getRouteStationStartEndDao().countNumByFromStationAndEndStation(startStation, endStation);
            if (num > 0) {
                DbUtil.getWorkPlanDao().insertProvider(workPlan);
                Log4j2Util.getWorkPlanLogger().info(String.format("工作计划创建成功，id： %d ,mcKey：%s,type：%d", workPlan.getId(), workPlan.getMckey(), type));
                return workPlan;
            } else {
                Log4j2Util.getWorkPlanLogger().info(String.format("工作计划创建失败，startStation : %s，endStation : %s ,无符合路径起止信息！", startStation, endStation));
                return null;
            }
        } else {
            DbUtil.getWorkPlanDao().insertProvider(workPlan);
            Log4j2Util.getWorkPlanLogger().info(String.format("工作计划创建成功，id： %d ,mcKey：%s,type：%d", workPlan.getId(), workPlan.getMckey(), type));
            return workPlan;
        }
    }

    /**
     * 创建工作计划
     *
     * @param mcKey         任务唯一标识
     * @param wmsId         wms任务唯一标识
     * @param endLocation   目标货位
     * @param endStation    目标站台
     * @param barcode       托盘号
     * @param type          工作计划类型
     * @param startStation  起始站台
     * @param startLocation 起始货位
     * @param scBlockName   穿梭车名称
     * @return com.wap.entity.WorkPlan
     * @author CalmLake
     * @date 2019/4/9 9:53
     */
    public static WcsWorkplanEntity createWorkPlan(String mcKey, String wmsId, String endLocation, String endStation, String barcode, int type, String startStation, String startLocation, String scBlockName) {
        WcsWorkplanEntity workPlan = new WcsWorkplanEntity();
        WcsMachineEntity machine = DbUtil.getMachineDao().selectOne(new QueryWrapper<WcsMachineEntity>().eq("stationName",startStation));
        workPlan.setStatus(WorkPlanConstant.STATUS_WAIT);
        workPlan.setCreateTime(new Date());
        workPlan.setMckey(StringUtils.isEmpty(mcKey) ? McKeyUtil.getMcKey() : mcKey);
        workPlan.setWmsFlag(wmsId);
        workPlan.setToLocation(endLocation);
        workPlan.setToStation(endStation);
        workPlan.setBarcode(barcode);
        workPlan.setType(type);
        workPlan.setPriorityConfigPriority(type);
        workPlan.setFromStation(startStation);
        workPlan.setFromLocation(startLocation);
        workPlan.setWarehouseNo(machine.getWarehouseNo());
        workPlan.setReserved2(scBlockName);
        DbUtil.getWorkPlanDao().insertProvider(workPlan);
        Log4j2Util.getWorkPlanLogger().info(String.format("工作计划创建成功，id： %d ,mcKey：%s,type：%d", workPlan.getId(), workPlan.getMckey(), type));
        return workPlan;
    }

    /**
     * 工作类型转换
     *
     * @param transportType 消息中的工作计划类型
     * @return java.lang.Byte
     * @author CalmLake
     * @date 2019/1/7 17:37
     */
    private Byte getWorkPlanType(String transportType) {
        Byte workPlanTypeByte = null;
        if (TransportOrderConstant.TYPE_PUT_IN_STORAGE.equals(transportType)) {
            workPlanTypeByte = WorkPlanConstant.TYPE_PUT_IN_STORAGE;
        } else if (TransportOrderConstant.TYPE_ADD_STORAGE.equals(transportType)) {
            workPlanTypeByte = WorkPlanConstant.TYPE_OUT_PUT_STORAGE;
        } else if (TransportOrderConstant.TYPE_MOVEMENT.equals(transportType)) {
            workPlanTypeByte = WorkPlanConstant.TYPE_MOVEMENT;
        } else if (TransportOrderConstant.TYPE_OUT_PUT_STORAGE_ALL.equals(transportType)) {
            workPlanTypeByte = WorkPlanConstant.TYPE_OUT_PUT_STORAGE;
        } else if (TransportOrderConstant.TYPE_PICK_UP_STORAGE.equals(transportType)) {
            workPlanTypeByte = WorkPlanConstant.TYPE_OUT_PUT_STORAGE;
        } else if (TransportOrderConstant.TYPE_TALLY.equals(transportType)) {
            workPlanTypeByte = WorkPlanConstant.TYPE_TALLY;
        } else {
            Log4j2Util.getWorkPlanLogger().info(String.format("接收WMS消息中，%s,未解析的任务类型", transportType));
        }
        return workPlanTypeByte;
    }

    /**
     * 拼接货位信息
     *
     * @param stringList 依次为排列层信息
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/7 16:26
     */
    private static String getLocation(List<String> stringList) {
        StringBuilder location = new StringBuilder();
        for (String string : stringList) {
            string = StringUtils.leftPad(string, 3, "0");
            location.append(string);
        }
        return location.toString();
    }

    /**
     * 解析货位信息
     *
     * @param location   workPlan中货位信息
     * @param stringList xml通讯中货位信息
     * @author CalmLake
     * @date 2019/1/22 14:48
     */
    public static void getListRackString(String location, List<String> stringList) {
        String row = StringUtils.substring(location, 0, 3);
        String line = StringUtils.substring(location, 3, 6);
        String tier = StringUtils.substring(location, 6, 9);
        stringList.add(row);
        stringList.add(line);
        stringList.add(tier);
    }
}