package io.renren.wcs.client.dto;

import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgSendService;

import java.util.Objects;

/**
 * WCS→Console 数据删除指示
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:01
 * @Version: V1.0.0
 **/
public class MsgDeleteDataDTO extends MsgDTO {
    /**
     * mcKey 搬送信息的唯一标识
     */
    private String mcKey;
    /**
     * 机器名称
     */
    private String machineName;
    /**
     * Type	1-任务删除,2-强制完成
     */
    private String operationType;

    public byte[] msgDTOToBytes(MsgDeleteDataDTO msgDeleteDataDTO) {
        MsgSendService msgSendService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.SEND)).getMsgSendService(msgDeleteDataDTO);
        return msgSendService.msgDTOToBytes(msgDeleteDataDTO);
    }

    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getData(){
        return mcKey+machineName+operationType;
    }

    @Override
    public String toString() {
        return String.format("mcKey：%s，机器名称：%s，操作类型：%s", mcKey, machineName, operationType);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + mcKey + machineName +  operationType + getBcc();
    }
}
