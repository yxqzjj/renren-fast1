package io.renren.wcs.client.contant;

import java.io.Serializable;

/**
 * 消息公共常量类
 * @Author: CalmLake
 * @Date: 2018/11/17  13:18
 * @Version: V1.0.0
 **/
public class MsgConstant implements Serializable {
    /**
     * 发送标识
     */
    public static final byte BYTE_TYPE_SEND = 1;
    /**
     * 接收标识
     */
    public static final byte BYTE_TYPE_RECEIVE = 2;
    /**
     * 发送标识
     */
    public static final int QUEUE_TYPE_SEND = 1;
    /**
     * 接收标识
     */
    public static final int QUEUE_TYPE_RECEIVE = 2;
    /**
     * 消息字符串中data开始的位置
     */
    public static final int DATA_START_INDEX = 13;
    /**
     * 消息字符串中命令类型开始的位置
     */
    public static final int COMMAND_TYPE_START_INDEX = 4;
    /**
     * 消息字符串中命令类型结束的位置
     */
    public static final int COMMAND_TYPE_END_INDEX = 6;
    /**
     * 消息中BCC长度
     */
    public static final int BCC_LENGTH = 2;
    /**
     * 消息报文头部 byte（开始）
     */
    public static final byte STX_BYTE = 0x02;
    /**
     * 消息报文尾部 byte（结束）
     */
    public static final byte ETX_BYTE = 0x03;
    /**
     * 消息报文头部 byte（开始）
     */
    public static final String STX_STRING = "2";
    /**
     * 消息报文尾部 byte（结束）
     */
    public static final String ETX_STRING = "3";
    /**
     * 消息发送标识
     */
    public static final String RESEND_SEND = "0";
    /**
     * 消息重新发送标识
     */
    public static final String RESEND_RESEND = "1";
    /**
     * wcs→console 设备启动
     */
    public static final String MSG_COMMAND_TYPE_START_MACHINERY = "01";
    /**
     * wcs→console 设备停止
     */
    public static final String MSG_COMMAND_TYPE_STOP_MACHINERY = "02";
    /**
     * WCS→Console cycle指示
     */
    public static final String MSG_COMMAND_TYPE_CYCLE_ORDER = "03";
    /**
     * WCS→Console 数据删除指示
     */
    public static final String MSG_COMMAND_TYPE_DELETE_DATA = "04";
    /**
     * WCS→Console cycle完成应答
     */
    public static final String MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT_ACK = "05";
    /**
     * WCS→Console 询问设备状态指示
     */
    public static final String MSG_COMMAND_TYPE_MACHINERY_STATUS_ORDER_ASK = "06";
    /**
     * WCS→Console 心跳信号
     */
    public static final String MSG_COMMAND_TYPE_HEART_BEAT_SIGNAL_ASK = "10";
    /**
     * Console→WCS cycle指示应答
     */
    public static final String MSG_COMMAND_TYPE_CYCLE_ORDER_ACK = "23";
    /**
     * Console→WCS 数据删除应答
     */
    public static final String MSG_COMMAND_TYPE_DELETE_DATA_ACK = "24";
    /**
     * Console→WCS设备状态应答
     */
    public static final String MSG_COMMAND_TYPE_MACHINERY_STATUS_ORDER_ACK = "26";
    /**
     * Console→WCS心跳信号应答
     */
    public static final String MSG_COMMAND_TYPE_HEART_BEAT_SIGNAL_ACK = "30";
    /**
     * Console→WCS cycle完成报告
     */
    public static final String MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT = "35";
    /**
     * WCS→Console站台模式切换
     */
    public static final String MSG_COMMAND_TYPE_CHANGE_STATION_MODE = "40";
    /**
     * Console→WCS 站台模式切换应答
     */
    public static final String MSG_COMMAND_TYPE_CHANGE_STATION_MODE_ACK = "42";
    /**
     * Console→WCS 输送线Data报告
     */
    public static final String MSG_COMMAND_TYPE_CONVEYORLINE_DATA_REPORT = "50";
    /**
     * WCS→Console 输送线Data 报告应答
     */
    public static final String MSG_COMMAND_TYPE_CONVEYORLINE_DATA_REPORT_ACK = "55";
}
