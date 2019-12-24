package io.renren.wcs.client.dto;
import io.renren.wcs.client.Factory.FactoryConstant;
import io.renren.wcs.client.Factory.FactoryProducer;
import io.renren.wcs.service.MsgReceiveService;

import java.util.List;
import java.util.Objects;

/**
 * Console→WCS 设备状态应答
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  12:38
 * @Version: V1.0.0
 **/
public class MsgMachineryStatusOrderAckDTO extends MsgDTO {
    /**
     * 数据数量 1-9
     */
    private String dataNum;

    private List<MachineStatus> machineStatusList;

    public MsgMachineryStatusOrderAckDTO getMsgDTO(String msg) {
        MsgReceiveService msgReceiveService = Objects.requireNonNull(FactoryProducer.getFactory(FactoryConstant.RECEIVE)).getMsgReceiveService(msg);
        return (MsgMachineryStatusOrderAckDTO) msgReceiveService.getMsgDTO(msg);
    }

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public List<MachineStatus> getMachineStatusList() {
        return machineStatusList;
    }

    public void setMachineStatusList(List<MachineStatus> machineStatusList) {
        this.machineStatusList = machineStatusList;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (MachineStatus machineStatus : machineStatusList) {
            stringBuilder.append(machineStatus.toString());
        }
        return String.format("数据数量：%s，状态：%s", dataNum, stringBuilder.toString());
    }

    @Override
    public String getNumString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (MachineStatus machineStatus : machineStatusList) {
            stringBuilder.append(machineStatus.toString());
        }
        return getMessageNumber() + getCommandType() + getReSend() + getSendTime() + dataNum+ stringBuilder.toString() + getBcc();
    }

    public static class MachineStatus {
        /**
         * 机器名称 输送机为运行Block，其余设备为机器编号
         */
        private String machineName;
        /**
         * 宿主机器号 输送机为站台号或机器编号（站台号优先），其余设备为机器编号
         */
        private String hostMachineNo;
        /**
         * 货架排,001-100
         */
        private String row;
        /**
         * 货架列,001-100
         */
        private String line;
        /**
         * 货架层,001-100
         */
        private String tier;
        /**
         * 设备状态 运行0/1
         */
        private String status_running;
        /**
         * 设备状态   停止0/1
         */
        private String status_stopped;
        /**
         * 设备状态   异常0/1
         */
        private String status_exception;
        /**
         * 设备状态   紧急停止0/1
         */
        private String status_e_stop;
        /**
         * 设备状态   离线0/1
         */
        private String status_offline;
        /**
         * 设备状态   Battery OK 0/1
         */
        private String status_kWh_ok;
        /**
         * 设备状态   Battery 低下0/1
         */
        private String status_kWh_small;
        /**
         * 电池电量 001-100
         */
        private String kWh;
        /**
         * 异常代码 00-99
         */
        private String exceptionCode;

        public String getHostMachineNo() {
            return hostMachineNo;
        }

        public void setHostMachineNo(String hostMachineNo) {
            this.hostMachineNo = hostMachineNo;
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

        public String getStatus_running() {
            return status_running;
        }

        public void setStatus_running(String status_running) {
            this.status_running = status_running;
        }

        public String getStatus_stopped() {
            return status_stopped;
        }

        public void setStatus_stopped(String status_stopped) {
            this.status_stopped = status_stopped;
        }

        public String getStatus_exception() {
            return status_exception;
        }

        public void setStatus_exception(String status_exception) {
            this.status_exception = status_exception;
        }

        public String getStatus_e_stop() {
            return status_e_stop;
        }

        public void setStatus_e_stop(String status_e_stop) {
            this.status_e_stop = status_e_stop;
        }

        public String getStatus_offline() {
            return status_offline;
        }

        public void setStatus_offline(String status_offline) {
            this.status_offline = status_offline;
        }

        public String getStatus_kWh_ok() {
            return status_kWh_ok;
        }

        public void setStatus_kWh_ok(String status_kWh_ok) {
            this.status_kWh_ok = status_kWh_ok;
        }

        public String getStatus_kWh_small() {
            return status_kWh_small;
        }

        public void setStatus_kWh_small(String status_kWh_small) {
            this.status_kWh_small = status_kWh_small;
        }

        public String getkWh() {
            return kWh;
        }

        public void setkWh(String kWh) {
            this.kWh = kWh;
        }

        public String getExceptionCode() {
            return exceptionCode;
        }

        public void setExceptionCode(String exceptionCode) {
            this.exceptionCode = exceptionCode;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        @Override
        public String toString() {
            return String.format("机器名称：%s，宿主机器号：%s，排：%s，列：%s，层：%s，运行：%s，停止：%s，异常：%s，紧急停止：%s，离线：%s，电量ok：%s，电量低：%s，电量：%s,异常代码：%s"
                    , machineName, hostMachineNo, row, line, tier, status_running, status_stopped, status_exception, status_e_stop, status_offline, status_kWh_ok, status_kWh_small, kWh, exceptionCode);
        }


    }
}
