package io.renren.wap.util;


import io.renren.modules.generator.dao.*;
import io.renren.modules.generator.dao.ProcedureDao;
import io.renren.modules.generator.dao.ProcedureOrcaleDao;
import io.renren.wap.entity.constant.MachineConstant;

/**
 * DB工具类
 *
 * @Author: CalmLake
 * @Date: 2019/1/4  16:05
 * @Version: V1.0.0
 **/
public class DbUtil {
    /**
     * ProcedureOrcaleDao
     *
     * @return com.wap.dao.AutoCreateWorkPlanDao
     * @author CalmLake
     * @date 2019/6/14 14:52
     */
    public static ProcedureOrcaleDao getProcedureOrcaleDao() {
        return (ProcedureOrcaleDao) ApplicationContextProvider.getBean("procedureOrcaleDao");
    }
    /**
     * 存储过程操作对象
     *
     * @return com.wap.dao.RouteStationStartEndDao
     * @author CalmLake
     * @date 2019/5/19 14:21
     */
    public static ProcedureDao getProcedureDao() {
        return (ProcedureDao) ApplicationContextProvider.getBean("procedureDao");
    }
    /**
     * 演示操作对象
     *
     * @return com.wap.dao.AutoCreateWorkPlanDao
     * @author CalmLake
     * @date 2019/6/14 14:52
     */
    public static WcsAutocreateworkplanDao getAutoCreateWorkPlanDao() {
        return (WcsAutocreateworkplanDao) ApplicationContextProvider.getBean("wcsAutocreateworkplanDao");
    }

    /**
     * 库区操作对象
     *
     * @return com.wap.dao.WarehouseDao
     * @author CalmLake
     * @date 2019/6/14 14:52
     */
    public static WcsWarehouseDao getWarehouseDao() {
        return (WcsWarehouseDao) ApplicationContextProvider.getBean("WcsWarehouseDao");
    }



    /**
     * 路径起止信息表操作对象
     *
     * @return com.wap.dao.RouteStationStartEndDao
     * @author CalmLake
     * @date 2019/5/19 14:21
     */
    public static WcsRoutestationstartendDao getRouteStationStartEndDao() {
        return (WcsRoutestationstartendDao) ApplicationContextProvider.getBean("wcsRoutestationstartendDao");
    }

    /**
     * 充电位信息对象
     *
     * @return com.wap.util.YmlReadUtil
     * @author CalmLake
     * @date 2019/3/29 17:48
     */
    public static WcsChargesiteuseDao getChargeSiteUseDao() {
        return (WcsChargesiteuseDao) ApplicationContextProvider.getBean("wcsChargesiteuseDao");
    }

    /**
     * 获取读取配置文件工具对象（含数据）
     *
     * @return com.wap.util.YmlReadUtil
     * @author CalmLake
     * @date 2019/3/29 17:48
     */
    public static YmlReadUtil getYmlReadUtil() {
        return (YmlReadUtil) ApplicationContextProvider.getBean("YmlReadUtil");
    }

    /**
     * 获取操作BlockNameDao表的对象
     *
     * @return com.wap.dao.BlockNameDao
     * @author CalmLake
     * @date 2019/3/20 11:08
     */
    public static WcsBlocknameDao getBlockNameDao() {
        return (WcsBlocknameDao) ApplicationContextProvider.getBean("wcsBlocknameDao");
    }

    /**
     * 获取操作ChargeDao表的对象
     *
     * @return com.wap.dao.ChargeDao
     * @author CalmLake
     * @date 2019/3/20 11:08
     */
    public static WcsChargeDao getChargeDao() {
        return (WcsChargeDao) ApplicationContextProvider.getBean("wcsChargeDao");
    }

    /**
     * 获取操作WorkPlanLogDao表的对象
     *
     * @return com.wap.dao.WorkPlanLogDao
     * @author CalmLake
     * @date 2019/3/15 15:52
     */
    public static WcsWorkplanlogDao getWorkPlanLogDao() {
        return (WcsWorkplanlogDao) ApplicationContextProvider.getBean("WcsWorkplanlogDao");
    }

    /**
     * 获取操作CrossRouteDao表的对象
     *
     * @return com.wap.dao.CrossRouteDao
     * @author CalmLake
     * @date 2019/3/11 16:06
     */
    public static WcsCrossrouteDao getCrossRouteDao() {
        return (WcsCrossrouteDao) ApplicationContextProvider.getBean("wcsCrossrouteDao");
    }

    /**
     * 获取操作DefaultLocationDao表的对象
     *
     * @return com.wap.dao.DefaultLocationDao
     * @author CalmLake
     * @date 2019/3/8 15:39
     */
    public static WcsDefaultlocationDao getDefaultLocationDao() {
        return (WcsDefaultlocationDao) ApplicationContextProvider.getBean("wcsDefaultlocationDao");
    }

    /**
     * 获取操作PriorityConfig表的对象
     *
     * @return com.wap.dao.PriorityConfigDao
     * @author CalmLake
     * @date 2019/3/8 14:38
     */
    public static WcsPriorityconfigDao getPriorityConfigDao() {
        return (WcsPriorityconfigDao) ApplicationContextProvider.getBean("wcsPriorityconfigDao");
    }

    /**
     * 获取操作StationMode表的对象
     *
     * @return com.wap.dao.StationModeDao
     * @author CalmLake
     * @date 2019/2/28 15:52
     */
    public static WcsStationmodeDao getStationModeDao() {
        return (WcsStationmodeDao) ApplicationContextProvider.getBean("wcsStationmodeDao");
    }

    /**
     * 获取操作CommandLog表的对象
     *
     * @return com.wap.dao.CommandLogDao
     * @author CalmLake
     * @date 2019/2/19 10:54
     */
    public static WcsCommandlogDao getCommandLogDao() {
        return (WcsCommandlogDao) ApplicationContextProvider.getBean("wcsCommandlogDao");
    }

    /**
     * 获取操作WCSMessageLog表的对象
     *
     * @return com.wap.dao.WCSMessageLogDao
     * @author CalmLake
     * @date 2019/2/19 10:53
     */
    public static WcsWcsmessagelogDao getWCSMessageLogDao() {
        return (WcsWcsmessagelogDao) ApplicationContextProvider.getBean("wcsWcsmessagelogDao");
    }

    /**
     * 获取操作block表的对象
     *
     * @param blockName 数据block名称
     * @return java.lang.Object
     * @author CalmLake
     * @date 2019/1/23 15:05
     */
    public static Object getBlockDao(String blockName) {
        if (blockName.contains(MachineConstant.TYPE_SC)) {
            return getSCBlockDao();
        } else if (blockName.contains(MachineConstant.TYPE_MC)) {
            return getMCBlockDao();
        } else if (blockName.contains(MachineConstant.TYPE_ML)) {
            return getMLBlockDao();
        } else if (blockName.contains(MachineConstant.TYPE_RGV)) {
            return getRGVBlockDao();
        } else if (blockName.contains(MachineConstant.TYPE_AL)) {
            return getALBlockDao();
        } else {
            return getCLBlockDao();
        }
    }

    /**
     * 获取操作plc配置信息表的对象
     *
     * @return com.wap.dao.PlcConfigDao
     * @author CalmLake
     * @date 2019/1/16 14:49
     */
    public static WcsPlcconfigDao getPlcConfigDao() {
        return (WcsPlcconfigDao) ApplicationContextProvider.getBean("wcsPlcconfigDao");
    }

    /**
     * 获取操作任务分配表的对象
     *
     * @return com.wap.dao.TaskingDao
     * @author CalmLake
     * @date 2019/1/9 14:20
     */
    public static WcsTaskingDao getTaskingDao() {

        return (WcsTaskingDao) ApplicationContextProvider.getBean("wcsTaskingDao");
    }

    /**
     * 获取操作RGV数据block表的对象
     *
     * @return com.wap.dao.RGVBlockDao
     * @author CalmLake
     * @date 2019/1/9 14:08
     */
    public static WcsRgvblockDao getRGVBlockDao() {
        return (WcsRgvblockDao) ApplicationContextProvider.getBean("wcsRgvblockDao");
    }

    /**
     * 获取操作穿梭车数据block表的对象
     *
     * @return com.wap.dao.SCBlockDao
     * @author CalmLake
     * @date 2019/1/9 14:08
     */
    public static WcsScblockDao getSCBlockDao() {

        return (WcsScblockDao) ApplicationContextProvider.getBean("wcsScblockDao");
    }

    /**
     * 获取操作母车数据block表的对象
     *
     * @return com.wap.dao.MCBlockDao
     * @author CalmLake
     * @date 2019/1/9 14:07
     */
    public static WcsMcblockDao getMCBlockDao() {
        return (WcsMcblockDao) ApplicationContextProvider.getBean("wcsMcblockDao");
    }

    /**
     * 获取操作堆垛机数据block表的对象
     *
     * @return com.wap.dao.MLBlockDao
     * @author CalmLake
     * @date 2019/1/9 14:07
     */
    public static WcsMlblockDao getMLBlockDao() {

        return (WcsMlblockDao) ApplicationContextProvider.getBean("wcsMlblockDao");
    }

    /**
     * 获取操作升降机数据block表的对象
     *
     * @return com.wap.dao.ALBlockDao
     * @author CalmLake
     * @date 2019/1/9 14:06
     */
    public static WcsAlblockDao getALBlockDao() {

        return (WcsAlblockDao) ApplicationContextProvider.getBean("wcsAlblockDao");
    }

    /**
     * 获取操作输送线数据block表的对象
     *
     * @return com.wap.dao.CLBlockDao
     * @author CalmLake
     * @date 2019/1/9 11:58
     */
    public static WcsClblockDao getCLBlockDao() {

        return (WcsClblockDao) ApplicationContextProvider.getBean("wcsClblockDao");
    }

    /**
     * 获取操作路径信息表的对象
     *
     * @return com.wap.dao.RouteDao
     * @author CalmLake
     * @date 2019/1/9 11:32
     */
    public static WcsRouteDao getRouteDao() {

        return (WcsRouteDao) ApplicationContextProvider.getBean("wcsRouteDao");
    }

    /**
     * 获取操作工作计划表的对象
     *
     * @return com.wap.dao.WorkPlanDao
     * @author CalmLake
     * @date 2019/1/8 10:19
     */
    public static WcsWorkplanDao getWorkPlanDao() {
        return (WcsWorkplanDao) ApplicationContextProvider.getBean("wcsWorkplanDao");
    }

    /**
     * 获取操作设备表的对象
     *
     * @return com.wap.dao.MachineDao
     * @author CalmLake
     * @date 2019/1/7 15:04
     */
    public static WcsMachineDao getMachineDao() {
        return (WcsMachineDao) ApplicationContextProvider.getBean("wcsMachineDao");
    }

    /**
     * 获取操作wms消息记录表的对象
     *
     * @return com.wap.dao.WMSMessageLogDao
     * @author CalmLake
     * @date 2019/1/7 16:51
     */
    public static WcsWmsmessagelogDao getWMSMessageLogDao() {
        return (WcsWmsmessagelogDao) ApplicationContextProvider.getBean("wcsWmsmessagelogDao");
    }
    /**
     * 获取操作用户记录表的对象
     *
     * @return com.wap.dao.WMSMessageLogDao
     * @author CalmLake
     * @date 2019/1/7 16:51
     */
    public static WcsUserDao getWcsUserDao() {
        return (WcsUserDao) ApplicationContextProvider.getBean("wcsUserDao");
    }
    /**
     * 获取操作McKey表的对象
     *
     * @return com.wap.dao.McKeyDao
     * @author CalmLake
     * @date 2019/1/7 17:02
     */
    static WcsMckeyDao getMcKeyDao() {
        return (WcsMckeyDao) ApplicationContextProvider.getBean("wcsMckeyDao");
    }
}
