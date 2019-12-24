package io.renren.wcs.client.dto;

import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgReceiveService;

import java.util.Objects;

/**
 * Console→WCS 站台模式切换应答
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:55
 * @Version: V1.0.0
 **/
public class MsgChangeStationModeAckDTO extends MsgDTO {
    /**
     * 站台号
     */
    private String station;
    /**
     * 模式 01-入库,02-出库
     */
    private String mode;

    public MsgChangeStationModeAckDTO getMsgDTO(String msg) {
        MsgReceiveService msgReceiveService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.RECEIVE)).getMsgReceiveService(msg);
        return (MsgChangeStationModeAckDTO) msgReceiveService.getMsgDTO(msg);
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return String.format("站台号：%s，模式：%s", station, mode);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + station + mode + getBcc();
    }
}
