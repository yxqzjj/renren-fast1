package io.renren.modules.generator.dao.provider;


import io.renren.modules.generator.entity.WcsWorkplanlogEntity;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.util.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * sql操作
 *
 * @author CalmLake
 * @date 2019/3/15 16:31
 */
public class WorkPlanLogSqlProvider {

    public String insertSelective(WcsWorkplanlogEntity workPlan) {
        SQL sql = new SQL();
        sql.INSERT_INTO("WCS_WorkPlanLog");
        if (workPlan.getId() != null && workPlan.getId() > 0) {
            sql.VALUES("ID", Integer.toString(workPlan.getId()));
        }
        if (StringUtils.isNotEmpty(workPlan.getMckey())) {
            sql.VALUES("Work_Plan_Id", "'" + workPlan.getWorkPlanId() + "'");
        }
        if (StringUtils.isNotEmpty(workPlan.getMckey())) {
            sql.VALUES("McKey", "'" + workPlan.getMckey() + "'");
        }
        if (workPlan.getType() != null) {
            sql.VALUES("Type", Integer.toString(workPlan.getType()));
        }
        if (StringUtils.isNotEmpty(workPlan.getBarcode())) {
            sql.VALUES("Barcode", "'" + workPlan.getBarcode() + "'");
        }
        if (workPlan.getCreateTime() != null) {
            sql.VALUES("Create_Time", DateFormatUtil.dateToString(workPlan.getCreateTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
        }
        if (workPlan.getStartTime() != null) {
            sql.VALUES("Start_Time",   DateFormatUtil.dateToString(workPlan.getStartTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
        }
        if (workPlan.getFinishTime() != null) {
            sql.VALUES("Finish_Time",  DateFormatUtil.dateToString(workPlan.getFinishTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS));
        }
        if (workPlan.getStatus() != null) {
            sql.VALUES("Status", Integer.toString(workPlan.getStatus()));
        }
        if (StringUtils.isNotEmpty(workPlan.getFromStation())) {
            sql.VALUES("From_Station", "'" + workPlan.getFromStation() + "'");
        }
        if (StringUtils.isNotEmpty(workPlan.getToStation())) {
            sql.VALUES("To_Station", "'" + workPlan.getToStation() + "'");
        }
        if (StringUtils.isNotEmpty(workPlan.getFromLocation())) {
            sql.VALUES("From_Location", "'" + workPlan.getFromLocation() + "'");
        }
        if (StringUtils.isNotEmpty(workPlan.getToLocation())) {
            sql.VALUES("To_Location", "'" + workPlan.getToLocation() + "'");
        }
        if (StringUtils.isNotEmpty(workPlan.getWmsFlag())) {
            sql.VALUES("WMS_Flag", "'" + workPlan.getWmsFlag() + "'");
        }
        if (workPlan.getPriorityConfigPriority() != null) {
            sql.VALUES("Priority_Config_Priority", Integer.toString(workPlan.getPriorityConfigPriority()));
        }
        if (StringUtils.isNotEmpty(workPlan.getReserved1())) {
            sql.VALUES("Reserved1", "'" + workPlan.getReserved1() + "'");
        }
        if (StringUtils.isNotEmpty(workPlan.getReserved2())) {
            sql.VALUES("Reserved2", "'" + workPlan.getReserved2() + "'");
        }
        return sql.toString();
    }

}