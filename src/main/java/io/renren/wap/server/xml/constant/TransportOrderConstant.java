package io.renren.wap.server.xml.constant;

/**
 * wms下发消息任务常量
 *
 * @Author: CalmLake
 * @Date: 2019/1/7  17:43
 * @Version: V1.0.0
 **/
public class TransportOrderConstant {
    /**
     * 类型-入库
     */
    public static final String TYPE_PUT_IN_STORAGE = "01";
    /**
     * 类型-直行
     */
    public static final String TYPE_GO_STRAIGHT= "02";
    /**
     * 类型-整出库
     */
    public static final String TYPE_OUT_PUT_STORAGE_ALL = "03";
    /**
     * 类型-拣选出库
     */
    public static final String TYPE_PICK_UP_STORAGE = "04";
    /**
     * 类型-补充出库
     */
    public static final String TYPE_ADD_STORAGE = "05";
    /**
     * 类型-移位
     */
    public static final String TYPE_MOVEMENT = "11";
    /**
     * 类型-理货
     */
    public static final String TYPE_TALLY = "15";
    /**
     * 类型-删除
     */
    public static final String TYPE_DELETE = "16";
}
