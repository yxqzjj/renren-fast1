package io.renren.wcs.util;

import io.renren.modules.generator.dao.*;

/**
 * @author ：zzwy
 * @date ：Created in 2019/12/23 9:07
 * @description：操作数据库工具类
 * @modified By：
 * @version: 1.0
 */
public class DbUtil {

    /**
     * 输送线数据block
     * @return
     */
    public static WcsClblockDao getWcsClblockDao(){
         return (WcsClblockDao)SpringBootBeanUtil.getBean("WcsClblockDao");
    }

    /***
     * 接收命令记录
     * @return
     */
    public static WcsCommandlogDao getWcsCommandlogDao(){
         return (WcsCommandlogDao) SpringBootBeanUtil .getBean("WcsCommandlogDao");
    }

    /**
     * 设备信息
     * @return
     */
    public static WcsMachineDao getWcsMachineDao(){
        return (WcsMachineDao) SpringBootBeanUtil.getBean("WcsMachineDao");
    }

    /**
     * plc信息
     * @return
     */
    public  static WcsPlcconfigDao getWcsPlcconfigDao(){
         return (WcsPlcconfigDao) SpringBootBeanUtil.getBean("WcsPlcconfigDao");
    }

    /**
     * RGV信息表
     * @return
     */
    public static WcsRgvblockDao getWcsRgvblockDao(){
        return (WcsRgvblockDao) SpringBootBeanUtil.getBean("WcsRgvblockDao");
    }

    /**
     * WCS发送消息记录
     * @return
     */
    public static  WcsWcsmessagelogDao getWcsWcsmessagelogDao(){
        return (WcsWcsmessagelogDao) SpringBootBeanUtil.getBean("WcsWcsmessagelogDao");
    }

     /**
     *  WCS-WMS消息记录
     * @return
     */
    public static  WcsWmsmessagelogDao getWcsWmsmessagelogDao(){
        return (WcsWmsmessagelogDao) SpringBootBeanUtil.getBean("WcsWmsmessagelogDao");
    }

    /**
     * wcs工作计划
     * @return
     */
    public static  WcsWorkplanDao getWcsWorkplanDao(){
        return (WcsWorkplanDao) SpringBootBeanUtil.getBean("WcsWorkplanDao");
    }
}
