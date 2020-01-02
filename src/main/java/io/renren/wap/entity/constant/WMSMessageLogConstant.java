package io.renren.wap.entity.constant;

/**
 * wms消息记录常量
 *
 * @Author: CalmLake
 * @Date: 2019/1/8  11:22
 * @Version: V1.0.0
 **/
public class WMSMessageLogConstant {
    /**
     * 状态 已接收
     */
    public static final Integer STATUS_RECEIVED = 1;
    /**
     * 状态 已回复
     */
    public static final Integer STATUS_RECEIVED_ACK = 2;
    /**
     * 状态 已发送
     */
    public static final Integer STATUS_SEND = 3;
    /**
     * 状态 已应答
     */
    public static final Integer STATUS_SEND_ACK = 4;
    /**
     * 类型 路径变更请求
     */
    public static final String TYPE_LOADUNITATID = "LoadUnitAtId";
    /**
     * 类型 任务下发指令
     */
    public static final String TYPE_TRANSPORTORDER = "TransportOrder";
    /**
     * 类型 任务接受指令
     */
    public static final String TYPE_ACCEPTTRANSPORTORDER = "AcceptTransportOrder";
    /**
     * 类型 状态报告
     */
    public static final String TYPE_MOVEMENTREPORT = "MovementReport";
    /**
     * 类型 数据删除指令
     */
    public static final String TYPE_CANCELTRANSPORTORDER = "CancelTransportOrder";
    /**
     * 类型 数据删除报告
     */
    public static final String TYPE_CANCELTRANSPORTORDERREPORT = "CancelTransportOrderReport";
    /**
     * 类型 路径报告
     */
    public static final String TYPE_ROUTESTATUS = "RouteStatus";
    /**
     * 类型 站台切换
     */
    public static final String TYPE_TRANSPORTMODECHANGE = "TransportModeChange";
    /**
     * 类型 站台切换报告
     */
    public static final String TYPE_TRANSPORTMODECHANGEREPORT = "TransportModeChangeReport";
    /**
     * 类型 查询站台状态
     */
    public static final String TYPE_QUERY_TRANSPORT_MODE = "QueryTransportMode";
}
