package io.renren.wap.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.dao.impl.WcsRouteStationStartEndDaoImpl;
import io.renren.modules.generator.entity.WcsCommandlogEntity;
import io.renren.modules.generator.entity.WcsRoutestationstartendEntity;
import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.dao.WorkPlanDao;
import io.renren.wap.dao.WorkPlanLogDao;
import io.renren.wap.entity.constant.WMSMessageLogConstant;
import io.renren.wap.entity.constant.WorkPlanConstant;
import io.renren.wap.server.cache.ServerConfigCache;
import io.renren.wap.server.cache.XmlQueueCache;
import io.renren.wap.server.constant.XmlQueueConstant;
import io.renren.wap.server.service.CreateXmlService;
import io.renren.wap.server.xml.constant.XmlInfoConstant;
import io.renren.wap.server.xml.dto.EnvelopeDTO;
import io.renren.wap.service.AssigningTaskService;
import io.renren.wap.service.WmsMessageLogService;
import io.renren.wap.service.WorkPlanService;
import io.renren.wap.util.DbUtil;
import io.renren.wap.util.Log4j2Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工作计划
 *
 * @Author: CalmLake
 * @Date: 2019/1/7  16:16
 * @Version: V1.0.0
 **/
@Controller
@RequestMapping("workPlanControl")
public class WorkPlanControl {
    @Resource(name = "WorkPlanDao")
    private WorkPlanDao workPlanDao;
    @Resource(name = "WorkPlanLogDao")
    private WorkPlanLogDao workPlanLogDao;

    @RequestMapping("createWorkPlanList")
    @ResponseBody
    public JSONObject createWorkPlanList(HttpServletRequest request) {
        boolean result = false;
        String msg;
        JSONObject jsonObject = new JSONObject();
        String msgString = request.getParameter("msg");
        JSONObject jsonObjectMsg = JSONObject.parseObject(msgString);
        String station = jsonObjectMsg.getString("station");
        String workPlanType = jsonObjectMsg.getString("workPlanType");
        String fromRow = jsonObjectMsg.getString("fromRow");
        String fromLine = jsonObjectMsg.getString("fromLine");
        String fromTier = jsonObjectMsg.getString("fromTier");
        String toRow = jsonObjectMsg.getString("toRow");
        String toLine = jsonObjectMsg.getString("toLine");
        String toTier = jsonObjectMsg.getString("toTier");
        Integer fromRowInt = Integer.parseInt(fromRow);
        Integer fromLineInt = Integer.parseInt(fromLine);
        Integer fromTierInt = Integer.parseInt(fromTier);
        Integer toRowInt = Integer.parseInt(toRow);
        Integer toLineInt = Integer.parseInt(toLine);
        Integer toTierInt = Integer.parseInt(toTier);
        String endLocation;
        String startLocation;
        List<String> startLocationList = new ArrayList<>();
        List<String> endLocationList = new ArrayList<>();
        for (int i = fromRowInt; i <= toRowInt; i++) {
            for (int ii = fromTierInt; ii <= toTierInt; ii++) {
                String row_1;
                String line_1;
                String tier_1;
                String row_2;
                String line_2;
                String tier_2;
                row_1 = i + "";
                line_1 = fromLineInt + "";
                line_2 = fromLineInt + "";
                tier_1 = ii + "";
                if ((ii + 1) > toTierInt) {
                    row_2 = ++fromRowInt + "";
                    tier_2 = fromTierInt + "";
                } else {
                    row_2 = i + "";
                    tier_2 = ii + 1 + "";
                }
                row_1 = StringUtils.leftPad(row_1, 3, "0");
                line_1 = StringUtils.leftPad(line_1, 3, "0");
                tier_1 = StringUtils.leftPad(tier_1, 3, "0");
                row_2 = StringUtils.leftPad(row_2, 3, "0");
                line_2 = StringUtils.leftPad(line_2, 3, "0");
                tier_2 = StringUtils.leftPad(tier_2, 3, "0");
                startLocation = row_1 + line_1 + tier_1;
                endLocation = row_2 + line_2 + tier_2;
                startLocationList.add(startLocation);
                endLocationList.add(endLocation);
            }
        }
        int listLength = startLocationList.size();
        for (int i = 0; i < listLength; i++) {
            endLocation = endLocationList.get(i);
            startLocation = startLocationList.get(i);
            WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan("", "9999", endLocation, station, "9999", Byte.parseByte(workPlanType), station, startLocation);
            AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
            assigningTaskService.assigningTasks();
        }
        result = true;
        msg = "创建工作计划成功！噜噜噜";
        jsonObject.put("result", result);
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    @RequestMapping("createWorkPlan")
    @ResponseBody
    public JSONObject createWorkPlan(HttpServletRequest request) {
        boolean result = false;
        String msg;
        JSONObject jsonObject = new JSONObject();
        String msgString = request.getParameter("msg");
        JSONObject jsonObjectMsg = JSONObject.parseObject(msgString);
        String mcKey = "";
        String wmsId = jsonObjectMsg.getString("wmsId");
        String barcode = jsonObjectMsg.getString("barcode");
        String type = jsonObjectMsg.getString("type");
        String startStation = jsonObjectMsg.getString("startStation");
        String startLocation = jsonObjectMsg.getString("startLocation");
        String endStation = jsonObjectMsg.getString("endStation");
        String endLocation = jsonObjectMsg.getString("endLocation");
        if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==Byte.parseByte(type) || WorkPlanConstant.TYPE_OUT_PUT_STORAGE==Byte.parseByte(type)) {
            int num = WcsRouteStationStartEndDaoImpl.getRouteStationStartEndDao().countNumByFromStationAndEndStation(startStation, endStation);
            if (num > 0) {
                WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan(mcKey, wmsId, endLocation, endStation, barcode, Byte.parseByte(type), startStation, startLocation);
                AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
                assigningTaskService.assigningTasks();
                result = true;
                msg = "创建工作计划成功！噜噜噜";
            } else {
                msg = "创建工作计划失败，不是有效路径！怕不是遇到了..";
            }
        } else {
            WcsWorkplanEntity workPlan = WorkPlanService.createWorkPlan(mcKey, wmsId, endLocation, endStation, barcode, Byte.parseByte(type), startStation, startLocation);
            AssigningTaskService assigningTaskService = new AssigningTaskService(workPlan);
            assigningTaskService.assigningTasks();
            result = true;
            msg = "创建工作计划成功！噜噜噜";
        }
        jsonObject.put("result", result);
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    @RequestMapping("initIndex")
    @ResponseBody
    public JSONObject initIndex() {
        JSONObject jsonObject = new JSONObject();
        int finishNum = workPlanLogDao.countFinishWorkPlanNum(new Date());
        int workingNum = workPlanDao.countWorkingNum(new Date());
        jsonObject.put("finishNum", finishNum);
        jsonObject.put("workingNum", workingNum);
        return jsonObject;
    }


    @RequestMapping("deleteWorkPlan")
    @ResponseBody
    public JSONObject deleteWorkPlan(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String idString = request.getParameter("msg");
        int idInt = Integer.parseInt(idString);
        WcsWorkplanEntity workPlan = workPlanDao.selectByPrimaryKey(idInt);
        String mcKey = workPlan.getMckey();
        workPlan.setStatus(WorkPlanConstant.STATUS_CANCEL);
        workPlanDao.updateByPrimaryKey(workPlan);
        DbUtil.getCommandLogDao().delete(new QueryWrapper<WcsCommandlogEntity>().eq("McKey",mcKey));
        DbUtil.getTaskingDao().delete(new QueryWrapper<WcsTaskingEntity>().eq("McKey",mcKey));
        WorkPlanService.createWorkPlanLog(idInt);
        int i = workPlanDao.deleteByPrimaryKey(idInt);
        if (i > 0) {
            jsonObject.put("result", true);
            jsonObject.put("msg", "删除工作计划成功");
        } else {
            jsonObject.put("result", false);
            jsonObject.put("msg", "失败");
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工删除工作计划，结果：%s", idString, jsonObject.toJSONString()));
        return jsonObject;
    }
    @RequestMapping("finishWorkPlan")
    @ResponseBody
    public JSONObject finishWorkPlan(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String idString = request.getParameter("msg");
        int idInt = Integer.parseInt(idString);
        WcsWorkplanEntity workPlan = workPlanDao.selectByPrimaryKey(idInt);
        String mcKey = workPlan.getMckey();
        workPlan.setFinishTime(new Date());
        workPlan.setStatus(WorkPlanConstant.STATUS_FINISH);
        int workPlanType = workPlan.getType();
        int i = workPlanDao.updateByPrimaryKey(workPlan);
        if (i > 0) {
            DbUtil.getCommandLogDao().delete(new QueryWrapper<WcsCommandlogEntity>().eq("Mckey",mcKey));
            DbUtil.getTaskingDao().delete(new QueryWrapper<WcsTaskingEntity>().eq("McKey",mcKey));
            WorkPlanService.createWorkPlanLog(idInt);
            DbUtil.getWorkPlanDao().deleteById(idInt);
            jsonObject.put("result", true);
            jsonObject.put("msg", "成功");
            if (WorkPlanConstant.TYPE_PUT_IN_STORAGE==workPlanType || WorkPlanConstant.TYPE_OUT_PUT_STORAGE==workPlanType || WorkPlanConstant.TYPE_MOVEMENT==workPlanType || WorkPlanConstant.TYPE_TALLY==workPlanType || WorkPlanConstant.TYPE_TAKE_STOCK==workPlanType) {
                EnvelopeDTO envelopeDTO = CreateXmlService.createMovementReport(workPlan.getBarcode(), workPlan.getWmsFlag(), workPlan.getFromStation(), workPlan.getFromLocation(), workPlan.getToStation(), workPlan.getToLocation(), XmlInfoConstant.XML_DEFAULT_00, XmlInfoConstant.XML_DEFAULT_00);
                try {
                    WmsMessageLogService wmsMessageLogService = new WmsMessageLogService();
                    wmsMessageLogService.insertWMSMessageLog(workPlan.getWmsFlag(), workPlan.getBarcode(), workPlan.getId(), envelopeDTO, WMSMessageLogConstant.TYPE_MOVEMENTREPORT, WMSMessageLogConstant.STATUS_SEND, "");
                    XmlQueueCache.addMsg(XmlQueueConstant.QUEUE_TYPE_SEND, ServerConfigCache.DEFAULT_LIVING_CLIENT_PORT, envelopeDTO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    jsonObject.put("msg", "队列出错");
                    jsonObject.put("result", false);
                }
            }
        } else {
            jsonObject.put("result", false);
            jsonObject.put("msg", "修改失败");
        }
        Log4j2Util.getOperationLog().info(String.format("%s,人工完成工作计划，结果：%s", idString, jsonObject.toJSONString()));
        return jsonObject;
    }
    @RequestMapping("getListWorkPlan")
    @ResponseBody
    public JSONArray getListWorkPlan() {
        JSONArray jsonArray = new JSONArray();
        List<WcsWorkplanEntity> workPlanList =DbUtil.getWorkPlanDao().selectList(new QueryWrapper<WcsWorkplanEntity>());
        for (WcsWorkplanEntity workPlan : workPlanList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", workPlan.getId());
            jsonObject.put("orderKey", workPlan.getMckey());
            jsonObject.put("barCode", workPlan.getBarcode());
            jsonObject.put("wmsMcKey", workPlan.getWmsFlag());
            jsonObject.put("createTime", workPlan.getCreateTime() != null ? DateFormatUtils.format(workPlan.getCreateTime(), "yyyy-MM-dd HH:mm:ss") : "");
            jsonObject.put("startTime", workPlan.getStartTime() != null ? DateFormatUtils.format(workPlan.getStartTime(), "yyyy-MM-dd HH:mm:ss") : "");
            jsonObject.put("endTime", workPlan.getFinishTime() != null ? DateFormatUtils.format(workPlan.getFinishTime(), "yyyy-MM-dd HH:mm:ss") : "");
            jsonObject.put("orderType", typeByteToString(workPlan.getType()));
            jsonObject.put("fromStation", workPlan.getFromStation());
            jsonObject.put("toStation", workPlan.getToStation());
            jsonObject.put("fromLocation", workPlan.getFromLocation());
            jsonObject.put("toLocation", workPlan.getToLocation());
            jsonObject.put("status", statusByteToString(workPlan.getStatus()));
            jsonObject.put("statusByte", workPlan.getStatus());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 工作计划类型转换显示
     *
     * @param type 工作计划类型
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/2/21 11:56
     */
    private String typeByteToString(int type) {
        String result;
        switch (type) {
            case 1:
                result = "入库";
                break;
            case 2:
                result = "出库";
                break;
            case 3:
                result = "移位";
                break;
            case 4:
                result = "理货";
                break;
            case 5:
                result = "盘点";
                break;
            case 6:
                result = "充电开始";
                break;
            case 7:
                result = "充电完成";
                break;
            case 8:
                result = "换层";
                break;
            case 9:
                result = "回原点";
                break;
            case 10:
                result = "卸车";
                break;
            case 11:
                result = "接车";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    /**
     * 工作状态转换显示
     *
     * @param status 工作状态
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/2/21 11:57
     */
    private String statusByteToString(int status) {
        String result;
        switch (status) {
            case 1:
                result = "等待";
                break;
            case 2:
                result = "进行中";
                break;
            case 3:
                result = "完成";
                break;
            case 4:
                result = "取消";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
}
