package io.renren.modules.generator.dao.provider;


import io.renren.modules.generator.entity.WcsWorkplanEntity;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.util.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * 工作计划 动态sql
 *
 * @Author: CalmLake
 * @Date: 2019/1/8  10:34
 * @Version: V1.0.0
 **/
public class WorkPlanProvider {

    /**
     * 动态插入
     *
     * @param workPlan 工作计划数据信息
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/8 10:39
     */
    public String insertProvider(WcsWorkplanEntity workPlan){
        SQL sql = new SQL();
        sql.INSERT_INTO("WCS_WorkPlan");
        try {
            if (workPlan.getId() != null && workPlan.getId() > 0) {
                sql.VALUES("ID", Integer.toString(workPlan.getId()));
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
                sql.VALUES("Create_Time", "to_date('" + DateFormatUtil.dateToString(workPlan.getCreateTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS) + "', 'yyyy-mm-dd hh24:mi:ss')");
            }
            if (workPlan.getStartTime() != null) {
                sql.VALUES("Start_Time", "to_date('" + DateFormatUtil.dateToString(workPlan.getStartTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS) + "', 'yyyy-mm-dd hh24:mi:ss')");
            }
            if (workPlan.getFinishTime() != null) {
                sql.VALUES("Finish_Time", "to_date('" + DateFormatUtil.dateToString(workPlan.getFinishTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS) + "', 'yyyy-mm-dd hh24:mi:ss')");
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
            if (workPlan.getWarehouseNo()>0) {
                sql.VALUES("Warehouse_No", "'" + workPlan.getWarehouseNo() + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql.toString();
    }
}
