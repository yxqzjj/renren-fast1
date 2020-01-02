package io.renren.wap.yml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * wms配置信息
 *
 * @Author: CalmLake
 * @Date: 2019/3/29  16:37
 * @Version: V1.0.0
 **/
@Component
@PropertySource("classpath:SystemConfigInfo.yml")
@ConfigurationProperties(prefix = "system-config")
public class SystemConfigInfo {
    /**
     * 功能描述
     */
    private boolean wmsOpen;
    /**
     * 系统工作模式
     */
    private String mode;
    /**
     * 当前项目名称
     */
    private String company;
    /**
     * 自动回原点功能开关
     */
    private boolean autoBackLocation;
    /**
     * 自动上架LoadUnitAtId消息发送开关
     */
    private boolean autoLoadUnitAtId;
    /**
     * 堆垛机卸车卸货之后执行接车任务
     */
    private boolean autoGetCar;
    /**
     * 出库优先卸车
     */
    private boolean outStorageOffCar;
    /**
     * 堆垛机二次移动功能开关
     */
    private boolean mlRemove;
    /**
     * 消息重发时间间隔 秒
     */
    private int resendTimeInterval;
    /**
     * 消息重发最大次数
     */
    private int resendMmax;
    /**
     * 超时未工作时间（分钟）
     */
    private int waitWorkOvertime;
    /**
     * 任务分配查询时间间隔 秒
     */
    private int selectTaskingTime;
    /**
     * 子车充电完成电量
     */
    private int scChargeFinishKwh;
    /**
     * 子车可工作电量
     */
    private int scWorkMinKwh;
    /**
     * 子车最小充电电量
     */
    private int scChargeMinKwh;
    /**
     * 子车充电在直行巷道中的位置A
     */
    private String chargeLocationA;
    /**
     * 子车充电在直行巷道中的位置B
     */
    private String chargeLocationB;

    /**
     * 备用子车放置货架位置点
     */
    private String standbyCarLocation;
    /**
     * 子车临时放置货架位置点
     */
    private String temporaryLocation;
    /**
     * 穿梭车2换层目标货位
     */
    private String carLocation2;
    /**
     * 穿梭车3换层目标货位
     */
    private String carLocation3;
    /**
     * 自动切换备车功能开关
     */
    private boolean standbyCarSwitch;
    /**
     * 穿梭车电量预警区间逻辑开关
     */
    private boolean kwhLowerSwitch;
    /**
     * 不用主车放置货架位置点
     */
    private String nonuseCarLocation;
    /**
     * 23指令修改设备载荷状态开关
     */
    private boolean msgCycleOrderAckDtoUpdateLoad;
    /**
     * 心跳周期 秒
     */
    private int heartBeatAsk;
    /**
     * 心跳超时关闭标识
     */
    private boolean heartBeatAskOutCloseFlag;
    /**
     * 心跳超时时间 秒
     */
    private int heartBeatAskOutTime;
    /**
     * 状态周期 秒
     */
    private int machineStatusAsk;
    /**
     * 心跳询问开关
     */
    private boolean heartBeatAskSwitch;
    /**
     * 状态询问开关
     */
    private boolean machineStatusAskSwitch;

    public boolean isAutoBackLocation() {
        return autoBackLocation;
    }

    public void setAutoBackLocation(boolean autoBackLocation) {
        this.autoBackLocation = autoBackLocation;
    }

    public boolean isOutStorageOffCar() {
        return outStorageOffCar;
    }

    public void setOutStorageOffCar(boolean outStorageOffCar) {
        this.outStorageOffCar = outStorageOffCar;
    }

    public boolean isMlRemove() {
        return mlRemove;
    }

    public void setMlRemove(boolean mlRemove) {
        this.mlRemove = mlRemove;
    }

    public int getResendTimeInterval() {
        return resendTimeInterval;
    }

    public void setResendTimeInterval(int resendTimeInterval) {
        this.resendTimeInterval = resendTimeInterval;
    }

    public int getWaitWorkOvertime() {
        return waitWorkOvertime;
    }

    public void setWaitWorkOvertime(int waitWorkOvertime) {
        this.waitWorkOvertime = waitWorkOvertime;
    }

    public int getSelectTaskingTime() {
        return selectTaskingTime;
    }

    public void setSelectTaskingTime(int selectTaskingTime) {
        this.selectTaskingTime = selectTaskingTime;
    }

    public int getScChargeFinishKwh() {
        return scChargeFinishKwh;
    }

    public void setScChargeFinishKwh(int scChargeFinishKwh) {
        this.scChargeFinishKwh = scChargeFinishKwh;
    }

    public int getScWorkMinKwh() {
        return scWorkMinKwh;
    }

    public void setScWorkMinKwh(int scWorkMinKwh) {
        this.scWorkMinKwh = scWorkMinKwh;
    }

    public int getScChargeMinKwh() {
        return scChargeMinKwh;
    }

    public void setScChargeMinKwh(int scChargeMinKwh) {
        this.scChargeMinKwh = scChargeMinKwh;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isWmsOpen() {
        return wmsOpen;
    }

    public void setWmsOpen(boolean wmsOpen) {
        this.wmsOpen = wmsOpen;
    }

    public String getChargeLocationA() {
        return chargeLocationA;
    }

    public void setChargeLocationA(String chargeLocationA) {
        this.chargeLocationA = chargeLocationA;
    }

    public String getChargeLocationB() {
        return chargeLocationB;
    }

    public void setChargeLocationB(String chargeLocationB) {
        this.chargeLocationB = chargeLocationB;
    }

    public String getTemporaryLocation() {
        return temporaryLocation;
    }

    public void setTemporaryLocation(String temporaryLocation) {
        this.temporaryLocation = temporaryLocation;
    }

    public int getResendMmax() {
        return resendMmax;
    }

    public void setResendMmax(int resendMmax) {
        this.resendMmax = resendMmax;
    }

    public boolean isStandbyCarSwitch() {
        return standbyCarSwitch;
    }

    public void setStandbyCarSwitch(boolean standbyCarSwitch) {
        this.standbyCarSwitch = standbyCarSwitch;
    }

    public boolean isKwhLowerSwitch() {
        return kwhLowerSwitch;
    }

    public void setKwhLowerSwitch(boolean kwhLowerSwitch) {
        this.kwhLowerSwitch = kwhLowerSwitch;
    }

    public String getStandbyCarLocation() {
        return standbyCarLocation;
    }

    public void setStandbyCarLocation(String standbyCarLocation) {
        this.standbyCarLocation = standbyCarLocation;
    }

    public String getNonuseCarLocation() {
        return nonuseCarLocation;
    }

    public void setNonuseCarLocation(String nonuseCarLocation) {
        this.nonuseCarLocation = nonuseCarLocation;
    }

    public boolean isMsgCycleOrderAckDtoUpdateLoad() {
        return msgCycleOrderAckDtoUpdateLoad;
    }

    public void setMsgCycleOrderAckDtoUpdateLoad(boolean msgCycleOrderAckDtoUpdateLoad) {
        this.msgCycleOrderAckDtoUpdateLoad = msgCycleOrderAckDtoUpdateLoad;
    }

    public int getHeartBeatAsk() {
        return heartBeatAsk;
    }

    public void setHeartBeatAsk(int heartBeatAsk) {
        this.heartBeatAsk = heartBeatAsk;
    }

    public int getMachineStatusAsk() {
        return machineStatusAsk;
    }

    public void setMachineStatusAsk(int machineStatusAsk) {
        this.machineStatusAsk = machineStatusAsk;
    }

    public boolean isHeartBeatAskSwitch() {
        return heartBeatAskSwitch;
    }

    public void setHeartBeatAskSwitch(boolean heartBeatAskSwitch) {
        this.heartBeatAskSwitch = heartBeatAskSwitch;
    }

    public boolean isMachineStatusAskSwitch() {
        return machineStatusAskSwitch;
    }

    public void setMachineStatusAskSwitch(boolean machineStatusAskSwitch) {
        this.machineStatusAskSwitch = machineStatusAskSwitch;
    }

    public boolean isAutoGetCar() {
        return autoGetCar;
    }

    public void setAutoGetCar(boolean autoGetCar) {
        this.autoGetCar = autoGetCar;
    }

    public int getHeartBeatAskOutTime() {
        return heartBeatAskOutTime;
    }

    public void setHeartBeatAskOutTime(int heartBeatAskOutTime) {
        this.heartBeatAskOutTime = heartBeatAskOutTime;
    }

    public boolean isHeartBeatAskOutCloseFlag() {
        return heartBeatAskOutCloseFlag;
    }

    public void setHeartBeatAskOutCloseFlag(boolean heartBeatAskOutCloseFlag) {
        this.heartBeatAskOutCloseFlag = heartBeatAskOutCloseFlag;
    }

    public boolean isAutoLoadUnitAtId() {
        return autoLoadUnitAtId;
    }

    public void setAutoLoadUnitAtId(boolean autoLoadUnitAtId) {
        this.autoLoadUnitAtId = autoLoadUnitAtId;
    }

    public String getCarLocation2() {
        return carLocation2;
    }

    public void setCarLocation2(String carLocation2) {
        this.carLocation2 = carLocation2;
    }

    public String getCarLocation3() {
        return carLocation3;
    }

    public void setCarLocation3(String carLocation3) {
        this.carLocation3 = carLocation3;
    }
}
