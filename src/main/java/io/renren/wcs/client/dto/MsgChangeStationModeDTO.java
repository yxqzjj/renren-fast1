package io.renren.wcs.client.dto;



import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgSendService;

import java.util.Objects;

/**
 * WCS→Console站台模式切换
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:54
 * @Version: V1.0.0
 **/
public class MsgChangeStationModeDTO extends MsgDTO {
    /**
     * 站台号
     */
    private String station;
    /**
     * 模式 01-入库,02-出库
     */
    private String mode;

    public byte[] msgDTOToBytes(MsgChangeStationModeDTO msgChangeStationModeDTO) {
        MsgSendService msgSendService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.SEND)).getMsgSendService(msgChangeStationModeDTO);
        return msgSendService.msgDTOToBytes(msgChangeStationModeDTO);
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

    public String getData(){
       return station + mode;
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
