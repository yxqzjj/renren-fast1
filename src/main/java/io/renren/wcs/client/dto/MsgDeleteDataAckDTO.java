package io.renren.wcs.client.dto;


import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgReceiveService;

import java.util.Objects;

/**
 * Console→WCS 数据删除应答
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:36
 * @Version: V1.0.0
 **/
public class MsgDeleteDataAckDTO extends MsgDTO {
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

    public MsgDeleteDataAckDTO getMsgDTO(String msg) {
        MsgReceiveService msgReceiveService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.RECEIVE)).getMsgReceiveService(msg);
        return (MsgDeleteDataAckDTO) msgReceiveService.getMsgDTO(msg);
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

    @Override
    public String toString() {
        return String.format("mcKey：%s，机器名称：%s，操作类型：%s", mcKey, machineName, operationType);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + mcKey + machineName +  operationType + getBcc();
    }
}
