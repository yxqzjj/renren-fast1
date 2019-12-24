package io.renren.wcs.client.dto;


import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgReceiveService;

import java.util.Objects;

/**
 * Console→WCS cycle完成报告
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:48
 * @Version: V1.0.0
 **/
public class MsgCycleOrderFinishReportDTO extends MsgDTO {
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
    /**
     * 载荷状态  0-无在荷，1-托盘在荷，2-子车在荷，3-子车，托盘在荷
     */
    private String loadStatus;
    /**
     * 完成区分  0-无完成信息，1-正常完成，2-异常完成
     */
    private String finishType;
    /**
     * 完成代码  1-开始,2-完成,3-重复存放,4-空出库,5-货形匹配错误,9-数据异常
     */
    private String finishCode;

    public MsgCycleOrderFinishReportDTO getMsgDTO(String msg) {
        MsgReceiveService msgReceiveService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.RECEIVE)).getMsgReceiveService(msg);
        return (MsgCycleOrderFinishReportDTO) msgReceiveService.getMsgDTO(msg);
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

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getFinishType() {
        return finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public String getFinishCode() {
        return finishCode;
    }

    public void setFinishCode(String finishCode) {
        this.finishCode = finishCode;
    }

    @Override
    public String toString() {
        return String.format("McKey：%s，机器号：%s，cycle命令：%s，作业区分：%s，货形高度：%s，货形宽度：%s，排：%s，列：%s，层：%s，站台：%s，码头：%s,载荷状态：%s，完成区分：%s，完成代码：%s",
                mcKey, machineName, cycleCommand, cycleType, height, width, row, line, tier, station, dock, loadStatus, finishType, finishCode);
    }

    public String getData() {
        return mcKey + machineName + cycleCommand + cycleType + height + width + row + line + tier + station + dock + loadStatus + finishType + finishCode;
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + mcKey + machineName + cycleCommand + cycleType + height + width + row + line + tier + station + dock + loadStatus + finishType + finishCode + getBcc();
    }
}
