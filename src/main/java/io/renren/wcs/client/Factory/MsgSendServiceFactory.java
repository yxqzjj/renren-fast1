package io.renren.wcs.client.Factory;

import io.renren.wcs.client.dto.*;
import io.renren.wcs.service.MsgReceiveService;
import io.renren.wcs.service.MsgSendService;
import io.renren.wcs.service.send.*;

/**
 * 消息发送 抽象工厂
 * @Author: CalmLake
 * @Date: 2018/11/17  17:30
 * @Version: V1.0.0
 **/
public class MsgSendServiceFactory extends AbstractFactory {

    @Override
    public MsgSendService getMsgSendService(MsgDTO msgDTO) {
        if (null == msgDTO) {
            return null;
        }
        if (msgDTO instanceof MsgStartMachineryDTO) {
            return new MsgStartMachinerySendServiceImpl();
        } else if (msgDTO instanceof MsgStopMachineryDTO) {
            return new MsgStopMachinerySendServiceImpl();
        } else if (msgDTO instanceof MsgCycleOrderDTO) {
            return new MsgCycleOrderSendServiceImpl();
        } else if (msgDTO instanceof MsgCycleOrderFinishReportAckDTO) {
            return new MsgCycleOrderFinishReportAckSendServiceImpl();
        } else if (msgDTO instanceof MsgChangeStationModeDTO) {
            return new MsgChangeStationModeSendServiceImpl();
        } else if (msgDTO instanceof MsgDeleteDataDTO) {
            return new MsgDeleteDataSendServiceImpl();
        } else if (msgDTO instanceof MsgMachineryStatusOrderAskDTO) {
            return new MsgMachineryStatusOrderAskSendServiceImpl();
        } else if (msgDTO instanceof MsgHeartBeatSignalAskDTO) {
            return new MsgHeartBeatSignalAskSendServiceImpl();
        } else if (msgDTO instanceof MsgConveyorLineDataReportAckDTO) {
            return new MsgConveyorLineDataReportAckSendServiceImpl();
        }
        return null;
    }

    @Override
    public MsgReceiveService getMsgReceiveService(String msg) {
        return null;
    }
}
