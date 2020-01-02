package io.renren.wap.cache;


import io.renren.wap.util.DbUtil;

/**
 * 系统变量缓存
 *
 * @Author: CalmLake
 * @Date: 2019/1/28  15:36
 * @Version: V1.0.0
 **/
public class SystemCache {
    /**
     * 系统工作模式
     */
    public static String SYS_MODE = DbUtil.getYmlReadUtil().getSystemConfigInfo().getMode();
    /**
     * 当前项目适用于那个项目
     */
    public static String SYS_NAME_COMPANY = DbUtil.getYmlReadUtil().getSystemConfigInfo().getCompany();
    /**
     * 自动回原点功能开关
     */
    public static boolean AUTO_BACK_LOCATION = DbUtil.getYmlReadUtil().getSystemConfigInfo().isAutoBackLocation();
    /**
     * 自动上架LoadUnitAtId消息发送开关
     */
    public static boolean AUTO_LOADUNITATID = DbUtil.getYmlReadUtil().getSystemConfigInfo().isAutoLoadUnitAtId();
    /**
     * 堆垛机卸车卸货之后执行接车任务
     */
    public static boolean AUTO_GET_CAR = DbUtil.getYmlReadUtil().getSystemConfigInfo().isAutoGetCar();
    /**
     * 出库优先卸车
     */
    public static boolean OUT_STORAGE_OFF_CAR = DbUtil.getYmlReadUtil().getSystemConfigInfo().isOutStorageOffCar();
    /**
     * 堆垛机二次移动功能开关
     */
    public static boolean ML_REMOVE = DbUtil.getYmlReadUtil().getSystemConfigInfo().isMlRemove();
    /**
     * 消息重发时间间隔 秒
     */
    public static long RESEND_TIME_INTERVAL = DbUtil.getYmlReadUtil().getSystemConfigInfo().getResendTimeInterval();
    /**
     * 消息重发最大次数
     */
    public static int RESEND_MMAX = DbUtil.getYmlReadUtil().getSystemConfigInfo().getResendMmax();
    /**
     * 超时未工作时间（分钟）
     */
    public static int WAIT_WORK_OVERTIME = DbUtil.getYmlReadUtil().getSystemConfigInfo().getWaitWorkOvertime();
    /**
     * 任务分配查询时间间隔 秒
     */
    public static int SELECT_TASKING_TIME = DbUtil.getYmlReadUtil().getSystemConfigInfo().getSelectTaskingTime();
    /**
     * 子车充电完成电量
     */
    public static int SC_CHARGE_FINISH_KWH = DbUtil.getYmlReadUtil().getSystemConfigInfo().getScChargeFinishKwh();
    /**
     * 子车可工作电量
     */
    public static int SC_WORK_MIN_KWH = DbUtil.getYmlReadUtil().getSystemConfigInfo().getScWorkMinKwh();
    /**
     * 子车最小充电电量
     */
    public static int SC_CHARGE_MIN_KWH = DbUtil.getYmlReadUtil().getSystemConfigInfo().getScChargeMinKwh();
    /**
     * 子车充电在直行巷道中的位置A
     */
    public static String SYS_CHARGE_LOCATION_A = DbUtil.getYmlReadUtil().getSystemConfigInfo().getChargeLocationA();
    /**
     * 子车充电在直行巷道中的位置B
     */
    public static String SYS_CHARGE_LOCATION_B = DbUtil.getYmlReadUtil().getSystemConfigInfo().getChargeLocationB();
    /**
     * 子车临时放置货架位置点
     */
    public static String TEMPORARY_LOCATION = DbUtil.getYmlReadUtil().getSystemConfigInfo().getTemporaryLocation();
    /**
     * 备用子车放置货架位置点
     */
    public static String STANDBY_CAR_LOCATION = DbUtil.getYmlReadUtil().getSystemConfigInfo().getStandbyCarLocation();
    /**
     * 不用主车放置货架位置点
     */
    public static String NONUSE_CAR_LOCATION = DbUtil.getYmlReadUtil().getSystemConfigInfo().getNonuseCarLocation();
    /**
     * 穿梭车2换层目标货位
     */
    public static String Car_Location_2 = DbUtil.getYmlReadUtil().getSystemConfigInfo().getCarLocation2();
    /**
     * 穿梭车3换层目标货位
     */
    public static String Car_Location_3 = DbUtil.getYmlReadUtil().getSystemConfigInfo().getCarLocation3();
    /**
     * 自动切换备车功能开关
     */
    public static boolean STANDBY_CAR_SWITCH = DbUtil.getYmlReadUtil().getSystemConfigInfo().isStandbyCarSwitch();
    /**
     * 穿梭车电量预警区间逻辑开关
     */
    public static boolean KWH_LOWER_SWITCH = DbUtil.getYmlReadUtil().getSystemConfigInfo().isKwhLowerSwitch();
    /**
     * 23指令修改设备载荷状态开关
     */
    public static boolean MSG_CYCLE_ORDER_ACK_UPDATE_LOAD = DbUtil.getYmlReadUtil().getSystemConfigInfo().isMsgCycleOrderAckDtoUpdateLoad();
    /**
     * 心跳周期 秒
     */
    public static int HEART_BEAT_ASK_TIME = DbUtil.getYmlReadUtil().getSystemConfigInfo().getHeartBeatAsk();
    /**
     * 心跳超时关闭标识
     */
    public static boolean HEART_BEAT_ASK_OUT_CLOSE_FLAG = DbUtil.getYmlReadUtil().getSystemConfigInfo().isHeartBeatAskOutCloseFlag();
    /**
     * 心跳超时 秒
     */
    public static int HEART_BEAT_ASK_OUT_TIME = DbUtil.getYmlReadUtil().getSystemConfigInfo().getHeartBeatAskOutTime();
    /**
     * 状态周期 秒
     */
    public static int MACHINE_STATUS_ASK_TIME = DbUtil.getYmlReadUtil().getSystemConfigInfo().getMachineStatusAsk();
    /**
     * 心跳询问开关
     */
    public static boolean HEART_BEAT_ASK_SWITCH = DbUtil.getYmlReadUtil().getSystemConfigInfo().isHeartBeatAskSwitch();
    /**
     * 状态询问开关
     */
    public static boolean MACHINE_STATUS_ASK_SWITCH = DbUtil.getYmlReadUtil().getSystemConfigInfo().isMachineStatusAskSwitch();
    /**
     * log日志记录路径
     */
    public static String LOGS_PATH = DbUtil.getYmlReadUtil().getLogConfigInfo().getLogsPath();
    /**
     * 自动删除过期日志开关
     */
    public static boolean DELETE_LOG_SWITCH = DbUtil.getYmlReadUtil().getLogConfigInfo().isDeleteLogSwitch();
    /**
     * 日志保留天数
     */
    public static int LOG_RESERVE_TIME = DbUtil.getYmlReadUtil().getLogConfigInfo().getLogReserveTime();
    /**
     * LoadUnitAtID消息重发时间间隔 秒
     */
    public static int RESEND_LOADUNITATID_TIME_INTERVAL = DbUtil.getYmlReadUtil().getWmsInfo().getResendLoadUnitAtIDTimeInterval();
    /**
     * LoadUnitAtID消息重发最大次数
     */
    public static int RESEND_LOADUNITATID_MAX = DbUtil.getYmlReadUtil().getWmsInfo().getResendLoadUnitAtIDMax();
}
