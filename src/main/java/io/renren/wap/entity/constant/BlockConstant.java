package io.renren.wap.entity.constant;

/**
 * 数据block常量
 *
 * @Author: CalmLake
 * @Date: 2019/1/9  13:14
 * @Version: V1.0.0
 **/
public class BlockConstant {
    /**
     * key都不为空
     */
    public static final String KEY_NOT_EMPTY_STRING = "0";
    /**
     * mcKey不为空
     */
    public static final String MCKEY_NOT_EMPTY_STRING = "1";
    /**
     * appointmentMcKey不为空
     */
    public static final String APPOINTMENT_MCKEY_NOT_EMPTY_STRING = "2";
    /**
     * key为空
     */
    public static final String KEY_EMPTY_STRING = "3";
    /**
     * key都不为空
     */
    public static final int KEY_NOT_EMPTY = 0;
    /**
     * mcKey不为空
     */
    public static final int MCKEY_NOT_EMPTY = 1;
    /**
     * appointmentMcKey不为空
     */
    public static final int APPOINTMENT_MCKEY_NOT_EMPTY = 2;
    /**
     * key为空
     */
    public static final int KEY_EMPTY = 3;
    /**
     * 状态：禁用
     */
    public static final String STATUS_BAN = "0";
    /**
     * 状态：运行
     */
    public static final String STATUS_RUNNING = "1";
    /**
     * 状态：充电开始任务创建
     */
    public static final String STATUS_CREATE_CHARGE_START = "2";
    /**
     * 状态：充电完成任务创建
     */
    public static final String STATUS_CREATE_CHARGE_FINISH = "3";
    /**
     * 状态：正在充电
     */
    public static final String STATUS_CHARGE = "4";
    /**
     * 载荷状态：有
     */
    public static final byte BLOCK_LOAD_TRUE = 1;
    /**
     * 载荷状态：无
     */
    public static final byte BLOCK_LOAD_FALSE = 0;
    /**
     * 错误代码 默认 00-正常
     */
    public static final String ERROR_CODE_DEFAULT = "00";
    /**
     * 优先卸车
     */
    public static final String SC_PRIORITY_OFF_CAR_RESERVED2 = "1";
    /**
     * 默认值
     */
    public static final String DEFAULT_RESERVED2 = "0";
    /**
     * 设备停泊位置-充电位
     */
    public static final String BERTH_BLOCK_NAME_CHARGE = "charge";
    /**
     * 设备停泊位置-货架原点
     */
    public static final String BERTH_BLOCK_NAME_LOCATION = "location";
    /**
     * 设备停泊位置-输送线A段
     */
    public static final String BERTH_BLOCK_NAME_CLA = "clA";
    /**
     * 设备停泊位置-输送线B段
     */
    public static final String BERTH_BLOCK_NAME_CLB = "clB";
}
