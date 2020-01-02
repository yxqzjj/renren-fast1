package io.renren.wap.entity.constant;

/**
 * 任务常量
 *
 * @Author: CalmLake
 * @Date: 2019/1/22  9:51
 * @Version: V1.0.0
 **/
public class TaskingConstant {
    /**
     * 使用堆垛机或母车数 0
     */
    public static final short ML_MC_ZERO = 0;
    /**
     * 使用堆垛机或母车数 1
     */
    public static final short ML_MC_ONE = 1;
    /**
     * 使用堆垛机或母车数 2
     */
    public static final short ML_MC_TWO = 2;
    /**
     * 设备均为输送线
     */
    public static final Byte MACHINE_TYPE_CL_CL = 0;
    /**
     * 设备为输送线和堆垛机/母车
     */
    public static final Byte MACHINE_TYPE_CL_M = 1;
    /**
     * 设备为输送线和提升机
     */
    public static final Byte MACHINE_TYPE_CL_AL = 2;
    /**
     * 设备为堆垛机/母车和穿梭车
     */
    public static final Byte MACHINE_TYPE_M_SC = 3;
}
