package io.renren.modules.generator.dao.provider;


import io.renren.modules.generator.entity.WcsTaskingEntity;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.util.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * 任务信息 动态sql
 *
 * @Author: CalmLake
 * @Date: 2019/1/9  14:34
 * @Version: V1.0.0
 **/
public class TaskingProvider {

    /**
     * 动态插入
     *
     * @param tasking 任务信息
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/9 14:43
     */
    public String insertProvider(WcsTaskingEntity tasking) {
        SQL sql = new SQL();
        sql.INSERT_INTO("WCS_Tasking");
        try {
            if (tasking.getId() != null && tasking.getId() > 0) {
                sql.VALUES("ID", Integer.toString(tasking.getId()));
            }
            if (tasking.getMckey() != null && StringUtils.isNotEmpty(tasking.getMckey())) {
                sql.VALUES("McKey", "'" + tasking.getMckey() + "'");
            }
            if (StringUtils.isNotEmpty(tasking.getBlockName())) {
                sql.VALUES("Block_Name", "'" + tasking.getBlockName() + "'");
            }
            if (StringUtils.isNotEmpty(tasking.getRunBlockName())) {
                sql.VALUES("Run_Block_Name", "'" + tasking.getRunBlockName() + "'");
            }
            if (tasking.getCreateTime() != null) {
                sql.VALUES("Create_Time", "to_date('" + DateFormatUtil.dateToString(tasking.getCreateTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS) + "', 'yyyy-mm-dd hh24:mi:ss')");
            }
            if (tasking.getPriorityConfigPriority() != null) {
                sql.VALUES("Priority_Config_Priority", Integer.toString(tasking.getPriorityConfigPriority()));
            }
            if (tasking.getMachineType() != null) {
                sql.VALUES("Machine_Type", Integer.toString(tasking.getMachineType()));
            }
            if (tasking.getWorkPlanType() != null) {
                sql.VALUES("Work_Plan_Type",Integer.toString(tasking.getWorkPlanType()));
            }
            if (StringUtils.isNotEmpty(tasking.getNextBlockName())) {
                sql.VALUES("Next_Block_Name", "'" + tasking.getNextBlockName() + "'");
            }
            if (StringUtils.isNotEmpty(tasking.getToStation())) {
                sql.VALUES("To_Station", "'" + tasking.getToStation() + "'");
            }
            if (StringUtils.isNotEmpty(tasking.getReserved1())) {
                sql.VALUES("reserved1", "'" + tasking.getReserved1() + "'");
            }
            if (StringUtils.isNotEmpty(tasking.getReserved2())) {
                sql.VALUES("Reserved2", "'" + tasking.getReserved2() + "'");
            }
            if (tasking.getWarehouseNo() > -1) {
                sql.VALUES("Warehouse_No", Integer.toString(tasking.getWarehouseNo()));
            }
            if (tasking.getMlMcNum() > -1) {
                sql.VALUES("Ml_Mc_Num", Integer.toString(tasking.getMlMcNum()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql.toString();
    }
}
