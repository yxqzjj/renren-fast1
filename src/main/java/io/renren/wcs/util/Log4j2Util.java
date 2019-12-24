package io.renren.wcs.util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * log4j2日志工具类
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
     */
    public static Logger getOperationLog() {
        return operationLog;
    }

    /**
     * 心跳状态消息记录
     */
    public static Logger getMsgHeartMachineStatus() {
        return msgHeartMachineStatus;
    }

    /**
     * 备车切换
     */
    public static Logger getStandbyCar() {
        return standbyCar;
    }

    /**
     * wms-wcs xml消息队列
     *
     */
    public static Logger getXmlMsgQueue() {
        return loggerXmlMsgQueue;
    }

    /**
     * 充电
     */
    public static Logger getChargeLogger() {
        return loggerCharge;
    }

    /**
     * 工作计划
     *
     */
    public static Logger getWorkPlanLogger() {
        return loggerWorkPlan;
    }

    /**
     * 获取消息队列log
     */
    public static Logger getMsgQueueLogger() {
        return loggerMsgQueue;
    }

    /**
     * 获取消息消费处理log
     */
    public static Logger getMsgCustomerLogger() {
        return loggerMsgCustomer;
    }

    /**
     * 获取block消息制作log
     */
    public static Logger getBlockBrickLogger() {
        return loggerBlockBrick;
    }

    /**
     * 获取wms消息解析log
     */
    public static Logger getXmlMsgOperationLogger() {
        return loggerXmlMsgOperation;
    }

    /**
     * 获取工作计划任务分配log
     *
     */
    public static Logger getAssigningTaskLogger() {
        return loggerAssigningTask;
    }

    /**
     * 控制台打印
     * @return
     */
    public static Logger getRoot() {
        return LogManager.getLogger();
    }
}
