package io.renren.wcs.client.dto;

import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgSendService;

import java.util.Objects;

/**
 * WCS→Console 询问设备状态指示
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:13
 * @Version: V1.0.0
 **/
public class MsgMachineryStatusOrderAskDTO extends MsgDTO {
    /**
     * 状态请求
     */
    public static final String STATUS_REQUEST_STATUS="0";
    /**
     * 设备下线
     */
    public static final String STATUS_REQUEST_OFFLINE="1";
    /**
     * 设备上线
     */
    public static final String STATUS_REQUEST_ONLINE="2";
    /**
     * 异常解除
     */
    public static final String STATUS_REQUEST_EXCEPTION_HANDING="3";
    /**
     * 机器名称 输送机为运行Block，其余设备为机器编号  0000表示询问所所有设备
     */
    private String machineName;
    /**
     * 状态 0-设备状态请求，1-离线，2-恢复，3-异常解除
     */
    private String status;

    public byte[] msgDTOToBytes(MsgMachineryStatusOrderAskDTO msgMachineryStatusOrderAskDTO) {
        MsgSendService msgSendService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.SEND)).getMsgSendService(msgMachineryStatusOrderAskDTO);
        return msgSendService.msgDTOToBytes(msgMachineryStatusOrderAskDTO);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getData() {
        return machineName + status;
    }

    @Override
    public String toString() {
        return String.format("机器名称：%s，状态：%s", machineName, status);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + machineName + status + getBcc();
    }
}
