package io.renren.wcs.client.dto;


import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgReceiveService;

import java.util.Objects;

/**
 * Console→WCS 输送线Data报告
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:58
 * @Version: V1.0.0
 **/
public class MsgConveyorLineDataReportDTO extends MsgDTO {
    /**
     * 数据数量	1-9
     */
    private String dataNum;
    /**
     * BlockImpl No. 站台号或数据Block（站台号优先）
     */
    private String blockNo;
    /**
     * 储位数	1-9
     */
    private String storageNum;
    /**
     * mcKey
     */
    private String mcKey;
    /**
     * 托盘号 0000000000（15byte）
     */
    private String barcode;
    /**
     * 载荷状态	0-无载荷，1-有载荷
     */
    private String loadStatus;
    /**
     * 货形(高度) 仅有一种货形时为0
     */
    private String cargoHeight;
    /**
     * 货形(宽度) 仅有一种货形时为0
     */
    private String cargoWidth;
    /**
     * 重量	 000001-999999 (9999.99)
     */
    private String weight;

    public MsgConveyorLineDataReportDTO getMsgDTO(String msg) {
        MsgReceiveService msgReceiveService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.RECEIVE)).getMsgReceiveService(msg);
        return (MsgConveyorLineDataReportDTO) msgReceiveService.getMsgDTO(msg);
    }

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(String storageNum) {
        this.storageNum = storageNum;
    }

    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getCargoHeight() {
        return cargoHeight;
    }

    public void setCargoHeight(String cargoHeight) {
        this.cargoHeight = cargoHeight;
    }

    public String getCargoWidth() {
        return cargoWidth;
    }

    public void setCargoWidth(String cargoWidth) {
        this.cargoWidth = cargoWidth;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("数据数量：%s，blockNo：%s，储位数：%s，mcKey：%s，托盘号：%s，载荷状态：%s，货形高度：%s，货形宽度：%s，重量：%s"
                , dataNum, blockNo, storageNum, mcKey, barcode, loadStatus, cargoHeight, cargoWidth, weight);
    }

    @Override
    public String getNumString() {
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + dataNum + blockNo + storageNum + mcKey + barcode + loadStatus + cargoHeight + cargoWidth + weight + getBcc();
    }
}
