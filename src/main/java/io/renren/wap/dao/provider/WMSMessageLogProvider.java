package io.renren.wap.dao.provider;


import io.renren.modules.generator.entity.WcsWmsmessagelogEntity;
import io.renren.wap.constant.DatePatternConstant;
import io.renren.wap.util.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * wms消息记录sql工厂
 *
 * @Author: CalmLake
 * @Date: 2019/1/8  11:32
 * @Version: V1.0.0
 **/
public class WMSMessageLogProvider {
    /**
     * 插入
     *
     * @param wmsMessageLog wms消息记录数据
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/1/8 11:42
     */
    public String insertProvider(WcsWmsmessagelogEntity wmsMessageLog) {
        SQL sql = new SQL();
        sql.INSERT_INTO("WCS_WMSMessageLog");
        if (StringUtils.isNotEmpty(wmsMessageLog.getWmsId())) {
            sql.VALUES("WMS_ID", "'" + wmsMessageLog.getWmsId() + "'");
        }
        if (wmsMessageLog.getWorkPlanId() != null) {
            sql.VALUES("Work_Plan_ID", Integer.toString(wmsMessageLog.getWorkPlanId()));
        }
        if (StringUtils.isNotEmpty(wmsMessageLog.getType())) {
            sql.VALUES("Type", "'" + wmsMessageLog.getType() + "'");
        }
        if (wmsMessageLog.getCreateTime() != null) {
            sql.VALUES("Create_Time", "to_date('" + DateFormatUtil.dateToString(wmsMessageLog.getCreateTime(), DatePatternConstant.YYYY_MM_DD_HH_MM_SS) + "', 'yyyy-mm-dd hh24:mi:ss')");
        }
        if (StringUtils.isNotEmpty(wmsMessageLog.getBarcode())) {
            sql.VALUES("Barcode", "'" + wmsMessageLog.getBarcode() + "'");
        }
        if (wmsMessageLog.getStatus() != null) {
            sql.VALUES("Status", Integer.toString(wmsMessageLog.getStatus()));
        }
        if (StringUtils.isNotEmpty(wmsMessageLog.getMessage())) {
            sql.VALUES("Message", "'" + wmsMessageLog.getMessage() + "'");
        }
        if (StringUtils.isNotEmpty(wmsMessageLog.getUuid())) {
            sql.VALUES("UUID", "'" + wmsMessageLog.getUuid() + "'");
        }
        if (StringUtils.isNotEmpty(wmsMessageLog.getReserved1())) {
            sql.VALUES("reserved1", "'" + wmsMessageLog.getReserved1() + "'");
        }
        if (StringUtils.isNotEmpty(wmsMessageLog.getReserved2())) {
            sql.VALUES("Reserved2", "'" + wmsMessageLog.getReserved2() + "'");
        }
        return sql.toString();
    }
}
