package io.renren.wap.client.dto;

import io.renren.wap.client.constant.FactoryConstant;
import io.renren.wap.client.factory.FactoryProducer;
import io.renren.wap.client.service.MsgSendService;

import java.util.List;
import java.util.Objects;

/**
 * wcs→console 设备启动
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  11:23
 * @Version: V1.0.0
 **/
public class MsgStartMachineryDTO extends MsgDTO {
    /**
     * 数据数量,001-100
     */
    private String dataNum;
    /**
     * 机器名称
     */
    private List<String> machineNameList;

    public byte[] msgDTOToBytes(MsgStartMachineryDTO msgStartMachineryDTO) {
        MsgSendService msgSendService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.SEND)).getMsgSendService(msgStartMachineryDTO);
        return msgSendService.msgDTOToBytes(msgStartMachineryDTO);
    }

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public List<String> getMachineNameList() {
        return machineNameList;
    }

    public void setMachineNameList(List<String> machineNameList) {
        this.machineNameList = machineNameList;
    }

    public String getData() {
        StringBuilder string = new StringBuilder();
        for (String str : machineNameList) {
            string.append(str);
        }
        return dataNum + string;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : machineNameList) {
            stringBuilder.append(string);
        }
        return String.format("数据数量：%s ，机器名称：%s ", dataNum, stringBuilder.toString());
    }

    @Override
    public String getNumString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : machineNameList) {
            stringBuilder.append(string);
        }
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + dataNum + stringBuilder.toString() + getBcc();
    }
}
