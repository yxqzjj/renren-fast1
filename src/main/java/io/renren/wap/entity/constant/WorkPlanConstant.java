package io.renren.wap.entity.constant;

/**
 * 工作计划常量
 *
 * @Author: CalmLake
 * @Date: 2019/1/7  17:25
 * @Version: V1.0.0
 **/
public class WorkPlanConstant {
    /**
     * 工作计划完成操作DB最大次数
     */
    public static final int FINISH_WORK_PLAN_OPERATION_MAX = 5;
    /**
     * 状态-等待
     */
    public static final int STATUS_WAIT = 1;
    /**
     * 状态-执行
     */
    public static final int STATUS_WORKING = 2;
    /**
     * 状态-完成
     */
    public static final int STATUS_FINISH = 3;
    /**
     * 状态-取消
     */
    public static final int STATUS_CANCEL = 4;
    /**
     * 工作计划类型-入库
     */
    public static final int TYPE_PUT_IN_STORAGE = 1;
    /**
     * 工作计划类型-出库
     */
    public static final int TYPE_OUT_PUT_STORAGE = 2;
    /**
     * 工作计划类型-充电开始
     */
    public static final int TYPE_CHARGE_UP = 6;
    /**
     * 工作计划类型-换层
     */
    public static final int TYPE_CHANGE_TIER = 8;
    /**
     * 工作计划类型-充电完成
     */
    public static final int TYPE_CHARGE_COMPLETE = 7;
    /**
     * 工作计划类型-移位
     */
    public static final int TYPE_MOVEMENT = 3;
    /**
     * 工作计划类型-理货
     */
    public static final int TYPE_TALLY = 4;
    /**
     * 工作计划类型-盘点
     */
    public static final int TYPE_TAKE_STOCK = 5;
    /**
     * 工作计划类型-返回原点
     */
    public static final int TYPE_GO_BACK_DEFAULT_LOCATION = 9;
    /**
     * 工作计划类型-卸车
     */
    public static final int TYPE_OFF_CAR = 10;
    /**
     * 工作计划类型-接车
     */
    public static final int TYPE_GET_CAR = 11;
    /**
     * 工作计划类型-移动
     */
    public static final int TYPE_MOVE = 12;
}
