package io.renren.wcs.client.contant;

/**
 * 设备信息常量
 *
 * @Author: CalmLake
 * @Date: 2019/1/9  11:50
 * @Version: V1.0.0
 **/
public class MachineConstant {
    /**
     * 输送线数据plc命名规则 BL
     */
    public static final String TYPE_PLC_NAME_BL = "BL";
    /**
     * 输送线数据block命名规则 纯数字
     */
    public static final String TYPE_CL = "00";
    /**
     * 堆垛机数据block命名规则 以 ML 开头数字结尾
     */
    public static final String TYPE_ML = "ML";
    /**
     * 升降机数据block命名规则 以 AL 开头数字结尾
     */
    public static final String TYPE_AL = "AL";
    /**
     * 母车数据block命名规则 以 MC 开头数字结尾
     */
    public static final String TYPE_MC = "MC";
    /**
     * 穿梭车数据block命名规则 以 SC 开头数字结尾
     */
    public static final String TYPE_SC = "SC";
    /**
     * RGV数据block命名规则 以 RGV 开头数字结尾
     */
    public static final String TYPE_RGV = "RGV";

    /**
     * 输送线
     */
    public static final Byte BYTE_TYPE_CL = 5;
    /**
     * 堆垛机
     */
    public static final Byte BYTE_TYPE_ML = 3;
    /**
     * 升降机
     */
    public static final Byte BYTE_TYPE_AL = 4;
    /**
     * 母车
     */
    public static final Byte BYTE_TYPE_MC = 2;
    /**
     * 穿梭车
     */
    public static final Byte BYTE_TYPE_SC = 1;
    /**
     * RGV
     */
    public static final Byte BYTE_TYPE_RGV = 6;

}
