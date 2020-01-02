package io.renren.wap.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * log4j2 工具类
 *
 * @Author: CalmLake
 * @Date: 2019/1/7  11:03
 * @Version: V1.0.0
 **/
public class Log4j2Util {

    private static final Logger loggerXmlMsgQueue = LogManager.getLogger("xmlMsgQueue");
    private static final Logger loggerXmlMsgOperation = LogManager.getLogger("xmlMsgOperation");
    private static final Logger loggerAssigningTask = LogManager.getLogger("assigningTask");
    private static final Logger loggerBlockBrick = LogManager.getLogger("blockBrick");
    private static final Logger loggerMsgCustomer = LogManager.getLogger("msgCustomer");
    private static final Logger loggerMsgQueue = LogManager.getLogger("msgQueue");
    private static final Logger msgHeartMachineStatus = LogManager.getLogger("msgHeartMachineStatus");
    private static final Logger loggerWorkPlan = LogManager.getLogger("workPlan");
    private static final Logger loggerCharge = LogManager.getLogger("charge");
    private static final Logger standbyCar = LogManager.getLogger("standbyCar");
    private static final Logger operationLog = LogManager.getLogger("operationLog");

    /**
     * 心跳状态消息记录
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/4/18 11:11
     */
    public static Logger getOperationLog() {
        return operationLog;
    }

    /**
     * 心跳状态消息记录
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/4/18 11:11
     */
    public static Logger getMsgHeartMachineStatus() {
        return msgHeartMachineStatus;
    }

    /**
     * 备车切换
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/4/18 11:11
     */
    public static Logger getStandbyCar() {
        return standbyCar;
    }

    /**
     * wms-wcs xml消息队列
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/4/1 10:05
     */
    public static Logger getXmlMsgQueue() {
        return loggerXmlMsgQueue;
    }

    /**
     * 充电
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/3/19 17:11
     */
    public static Logger getChargeLogger() {
        return loggerCharge;
    }

    /**
     * 工作计划
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/3/5 10:03
     */
    public static Logger getWorkPlanLogger() {
        return loggerWorkPlan;
    }

    /**
     * 获取消息队列log
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/1/16 14:07
     */
    public static Logger getMsgQueueLogger() {
        return loggerMsgQueue;
    }

    /**
     * 获取消息消费处理log
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/1/16 14:07
     */
    public static Logger getMsgCustomerLogger() {
        return loggerMsgCustomer;
    }

    /**
     * 获取block消息制作log
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/1/9 15:48
     */
    public static Logger getBlockBrickLogger() {
        return loggerBlockBrick;
    }

    /**
     * 获取wms消息解析log
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/1/7 11:06
     */
    public static Logger getXmlMsgOperationLogger() {
        return loggerXmlMsgOperation;
    }

    /**
     * 获取工作计划任务分配log
     *
     * @return org.apache.logging.log4j.Logger
     * @author CalmLake
     * @date 2019/1/9 14:16
     */
    public static Logger getAssigningTaskLogger() {
        return loggerAssigningTask;
    }

    public static Logger getRoot() {
        return LogManager.getLogger();
    }
}
