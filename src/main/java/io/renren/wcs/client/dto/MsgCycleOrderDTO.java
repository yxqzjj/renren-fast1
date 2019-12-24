package io.renren.wcs.client.dto;

import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgSendService;

import java.util.Objects;

/**
 * WCS→Console cycle指示
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  11:47
 * @Version: V1.0.0
 **/
public class MsgCycleOrderDTO extends MsgDTO {
    /**
     * mcKey,0000-9999
     */
    private String mcKey;
    /**
     * 机器名称
     */
    private String machineName;
    /**
     * Cycle命令
     */
    private String cycleCommand;
    /**
     * 作业区分
     */
    private String cycleType;
    /**
     * 货形(高度),默认0
     */
    private String height;
    /**
     * 货形(宽度),默认0
     */
    private String width;
    /**
     * 排，01-99
     */
    private String row;
    /**
     * 列，01-99
     */
    private String line;
    /**
     * 层，01-99
     */
    private String tier;
    /**
     * 站台
     */
    private String station;
    /**
     * 码头
     */
    private String dock;

    public byte[] msgDTOToBytes(MsgCycleOrderDTO msgCycleOrderDTO) {
        MsgSendService msgSendService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.SEND)).getMsgSendService(msgCycleOrderDTO);
        return msgSendService.msgDTOToBytes(msgCycleOrderDTO);
    }

    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getCycleCommand() {
        return cycleCommand;
    }

    public void setCycleCommand(String cycleCommand) {
        this.cycleCommand = cycleCommand;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    public String getData() {
        return mcKey + machineName + cycleCommand + cycleType + height + width + row + line + tier + station + dock;
    }

    @Override
    public String toString() {
        return String.format("McKey：%s，机器号：%s，cycle命令：%s，作业区分：%s，货形高度：%s，货形宽度：%s，排：%s，列：%s，层：%s，站台：%s，码头：%s", mcKey, machineName, cycleCommand, cycleType, height, width, row, line, tier, station, dock);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + mcKey + machineName + cycleCommand + cycleType + height + width + row + line + tier + station + dock + getBcc();
    }
}
