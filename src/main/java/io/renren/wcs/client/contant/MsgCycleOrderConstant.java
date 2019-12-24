package io.renren.wcs.client.contant;

import java.io.Serializable;

/**
 * 消息cycle 命令 常量
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  13:22
 * @Version: V1.0.0
 **/
public class MsgCycleOrderConstant implements Serializable {
    /**
     * 0000 站台/码头/消息序号默认值
     */
    public static final String DEFAULT_ZERO = "0000";
    /**
     * 0 货形宽高默认值
     */
    public static final String DEFAULT_HEIGHT_WIDTH = "0";
    /**
     * 00 排列层默认值
     */
    public static final String DEFAULT_ROW_LINE_TIER = "00";
    /**
     * 返回原点
     */
    public static final String CYCLE_COMMAND_RETURN_TO_ORIGIN_01 = "01";
    /**
     * 取货
     */
    public static final String CYCLE_COMMAND_PICK_UP_02 = "02";
    /**
     * 卸货
     */
    public static final String CYCLE_COMMAND_UNLOAD_03 = "03";
    /**
     * 移动
     */
    public static final String CYCLE_COMMAND_MOVE_NOLOAD_04 = "04";
    /**
     * 接子车
     */
    public static final String CYCLE_PICK_UP_THE_CAR_05 = "05";
    /**
     * 卸子车
     */
    public static final String CYCLE_UNCAR_06 = "06";
    /**
     * 移载取货
     */
    public static final String CYCLE_TRANSPLANTING_PICKUP_07 = "07";
    /**
     * 移载卸货
     */
    public static final String CYCLE_TRANSPLANTING_THE_UNLOADING_08 = "08";
    /**
     * 空车上车
     */
    public static final String CYCLE_GO_ON_CAR_EMPTY_09 = "09";
    /**
     * 空车下车
     */
    public static final String CYCLE_GO_OFF_CAR_EMPTY_10 = "10";
    /**
     * 移动
     */
    public static final String CYCLE_COMMAND_MOVE_LOAD_11 = "11";
    /**
     * 载货上车
     */
    public static final String CYCLE_GO_ON_CAR_12 = "12";
    /**
     * 载货下车
     */
    public static final String CYCLE_GO_OFF_CAR_13 = "13";
    /**
     * 充电开始
     */
    public static final String CYCLE_CHARGE_START_14 = "14";
    /**
     * 充电完成
     */
    public static final String CYCLE_CHARGE_FINISH_15 = "15";
    /**
     * 盘点
     */
    public static final String CYCLE_TAKE_STOCK_16 = "16";
    /**
     * 理货
     */
    public static final String CYCLE_TALLY_17 = "17";
    /**
     * 入库
     */
    public static final String CYCLE_TYPE_PUT_IN_STORAGE_01 = "01";
    /**
     * 直行
     */
    public static final String CYCLE_TYPE_GO_STRAIGHT_02 = "02";
    /**
     * 整出库
     */
    public static final String CYCLE_TYPE_OUTPUT_ALL_STORAGE_03 = "03";
    /**
     * 拣选出库
     */
    public static final String CYCLE_TYPE_OUTPUT_PICK_STORAGE_04 = "04";
    /**
     * 补充出库
     */
    public static final String CYCLE_TYPE_OUTPUT_REPLENISH_STORAGE_05 = "05";
    /**
     * 回库
     */
    public static final String CYCLE_TYPE_CALL_BACK_STORAGE_06 = "06";
    /**
     * 充电开始
     */
    public static final String CYCLE_TYPE_CHARGE_UP_07 = "07";
    /**
     * 换层
     */
    public static final String CYCLE_TYPE_CHANGE_TIER_08 = "08";
    /**
     * 充电完成
     */
    public static final String CYCLE_TYPE_CHARGE_COMPLETE_09 = "09";
    /**
     * 移库
     */
    public static final String CYCLE_TYPE_MOVEMENT_11 = "11";
    /**
     * 盘点
     */
    public static final String CYCLE_TYPE_TAKE_STOCK_12 = "12";
    /**
     * 理货
     */
    public static final String CYCLE_TYPE_TALLY_15 = "15";
    /**
     * 载荷状态 无
     */
    public static final String LOAD_STATUS_NONE = "0";
    /**
     * 载荷状态 有托盘
     */
    public static final String LOAD_STATUS_HAVE = "1";
    /**
     * 载荷状态  有子车
     */
    public static final String LOAD_STATUS_HAVA_CAR = "2";
    /**
     * 载荷状态 子车托盘
     */
    public static final String LOAD_STATUS_HAVE_CAR_LOAD = "3";
    /**
     * 应答区分 正常接收
     */
    public static final String ACK_TYPE_NORMAL = "0";
    /**
     * 应答区分 异常接收
     */
    public static final String ACK_TYPE_EXCEPTION = "1";
    /**
     * 异常区分 正常
     */
    public static final String EXCEPTION_TYPE_NORMAL = "0";
    /**
     * 异常区分 缓存满
     */
    public static final String EXCEPTION_TYPE_CACHE_FULL = "1";
    /**
     * 异常区分 条件错误
     */
    public static final String EXCEPTION_TYPE_CONDITION_ERROR = "2";
    /**
     * 异常区分 数据错误
     */
    public static final String EXCEPTION_TYPE_DATA_ERROR = "3";
    /**
     * 完成区分 无完成信息
     */
    public static final String FINISH_TYPE_NONE = "0";
    /**
     * 完成区分 正常完成
     */
    public static final String FINISH_TYPE_NORMAL = "1";
    /**
     * 完成区分 异常完成
     */
    public static final String FINISH_TYPE_EXCEPTION = "2";
    /**
     * 完成代码 开始
     */
    public static final String FINISH_CODE_START = "1";
    /**
     * 完成代码 完成
     */
    public static final String FINISH_CODE_FINISHED = "2";
    /**
     * 完成代码 重复存放
     */
    public static final String FINISH_CODE_ = "3";
    /**
     * 完成代码 空出库
     */
    public static final String FINISH_CODE_NOTHING = "4";
    /**
     * 完成代码 货形匹配错误
     */
    public static final String FINISH_CODE_MTS_EXCEPTION = "5";
    /**
     * 完成代码 数据异常
     */
    public static final String FINISH_CODE_DATA_EXCEPTION = "9";

}
