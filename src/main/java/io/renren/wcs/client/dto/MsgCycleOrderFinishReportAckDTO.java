package io.renren.wcs.client.dto;


import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgSendService;

import java.util.Objects;

/**
 * WCS→Console cycle完成应答
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:08
 * @Version: V1.0.0
 **/
public class MsgCycleOrderFinishReportAckDTO extends MsgDTO {
    /**
     * 数据block名称
     */
    private String blockName;
    /**
     * mcKey 搬送信息的唯一标识
     */
    private String mcKey;
    /**
     * cycleCommand 当前执行动作
     */
    private String cycleCommand;
    /**
     * 应答区分 0 正常接收,1 异常接收
     */
    private String ackType;

    public byte[] msgDTOToBytes(MsgCycleOrderFinishReportAckDTO msgCycleOrderFinishReportAckDTO) {
        MsgSendService msgSendService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.SEND)).getMsgSendService(msgCycleOrderFinishReportAckDTO);
        return msgSendService.msgDTOToBytes(msgCycleOrderFinishReportAckDTO);
    }

    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    public String getAckType() {
        return ackType;
    }

    public void setAckType(String ackType) {
        this.ackType = ackType;
    }

    public String getData() {
        return mcKey + ackType;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    @Override
    public String toString() {
        return String.format("mcKey：%s，blockName：%s，当前动作：%s，应答区分：%s", mcKey,blockName, cycleCommand, ackType);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + mcKey + blockName+cycleCommand + ackType + getBcc();
    }

    public String getCycleCommand() {
        return cycleCommand;
    }

    public void setCycleCommand(String cycleCommand) {
        this.cycleCommand = cycleCommand;
    }
}
