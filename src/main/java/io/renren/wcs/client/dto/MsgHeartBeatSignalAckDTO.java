package io.renren.wcs.client.dto;
import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgReceiveService;

import java.util.Objects;

/**
 * Console→WCS 心跳信号应答
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:45
 * @Version: V1.0.0
 **/
public class MsgHeartBeatSignalAckDTO extends MsgDTO {
    /**
     * 心跳消息内容
     */
    private String wcsNo;
    /**
     * plc名称
     * (plc名称与设备名称不一致 需谨记容易混乱   因输送线设备较多设备名称与plc名称不一致  其它设备（穿梭车 堆垛机 升降机 母车等）设备名称与plc一致)
     */
    private String consoleNo;
    /**
     * 心跳周期，秒
     */
    private String heartBeat;

    public MsgHeartBeatSignalAckDTO getMsgDTO(String msg) {
        MsgReceiveService msgReceiveService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.RECEIVE)).getMsgReceiveService(msg);
        return (MsgHeartBeatSignalAckDTO) msgReceiveService.getMsgDTO(msg);
    }

    public String getWcsNo() {
        return wcsNo;
    }

    public void setWcsNo(String wcsNo) {
        this.wcsNo = wcsNo;
    }

    public String getConsoleNo() {
        return consoleNo;
    }

    public void setConsoleNo(String consoleNo) {
        this.consoleNo = consoleNo;
    }

    public String getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(String heartBeat) {
        this.heartBeat = heartBeat;
    }

    @Override
    public String toString() {
        return String.format("wcs编号：%s，plc名称：%s，心跳周期：%s", wcsNo, consoleNo, heartBeat);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + wcsNo+ consoleNo+ heartBeat + getBcc();
    }
}
