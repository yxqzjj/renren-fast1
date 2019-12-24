package io.renren.wcs.client.Factory;


import io.renren.wcs.client.contant.MsgConstant;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.client.util.MsgSubstringUtil;
import io.renren.wcs.service.MsgReceiveService;
import io.renren.wcs.service.MsgSendService;
import io.renren.wcs.service.receive.*;

/**
 * 消息接收 抽象工厂
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  17:55
 * @Version: V1.0.0
 **/
public class MsgReceiveServiceFactory extends AbstractFactory {
    @Override
    public MsgSendService getMsgSendService(MsgDTO msgDTO) {
        return null;
    }

    @Override
    public MsgReceiveService getMsgReceiveService(String msg) {
        String commandType = MsgSubstringUtil.getCommandType(msg);
        switch (commandType) {
            case MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_ACK:
                return new MsgCycleOrderAckReceiveImpl();
            case MsgConstant.MSG_COMMAND_TYPE_DELETE_DATA_ACK:
                return new MsgDeleteDataAckReceiveServiceImpl();
            case MsgConstant.MSG_COMMAND_TYPE_MACHINERY_STATUS_ORDER_ACK:
                return new MsgMachineryStatusOrderAckReceiveServiceImpl();
            case MsgConstant.MSG_COMMAND_TYPE_HEART_BEAT_SIGNAL_ACK:
                return new MsgHeartBeatSignalAckReceiveServiceImpl();
            case MsgConstant.MSG_COMMAND_TYPE_CYCLE_ORDER_FINISH_REPORT:
                return new MsgCycleOrderFinishReportReceiveServiceImpl();
            case MsgConstant.MSG_COMMAND_TYPE_CHANGE_STATION_MODE_ACK:
                return new MsgChangeStationModeAckReceiveServiceImpl();
            case MsgConstant.MSG_COMMAND_TYPE_CONVEYORLINE_DATA_REPORT:
                return new MsgConveyorLineDataReportReceiveServiceImpl();
            default:
                return null;
        }
    }
}
