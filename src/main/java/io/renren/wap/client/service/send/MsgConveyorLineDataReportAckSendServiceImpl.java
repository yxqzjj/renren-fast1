package io.renren.wap.client.service.send;


import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.client.dto.MsgConveyorLineDataReportAckDTO;
import io.renren.wap.client.dto.MsgDTO;
import io.renren.wap.client.service.MsgSendService;

/**
 * @ClassName: MsgConveyorLineDataReportAckSendServiceImpl
 * @Description: MsgConveyorLineDataReportDTO 55 消息发送封装
 * @Author: CalmLake
 * @Date: 2018/11/17  18:04
 * @Version: V1.0.0
 **/
public class MsgConveyorLineDataReportAckSendServiceImpl implements MsgSendService {
    @Override
    public byte[] msgDTOToBytes(MsgDTO msgDTO) {
        MsgConveyorLineDataReportAckDTO msgConveyorLineDataReportAckDTO = (MsgConveyorLineDataReportAckDTO) msgDTO;
        String messageNumber = msgConveyorLineDataReportAckDTO.getMessageNumber();
        String commandType = msgConveyorLineDataReportAckDTO.getCommandType();
        String reSend = msgConveyorLineDataReportAckDTO.getReSend();
        String sendTime = msgConveyorLineDataReportAckDTO.getSendTime();
        String dataNum= msgConveyorLineDataReportAckDTO.getDataNum();
        String blockNo= msgConveyorLineDataReportAckDTO.getBlockNo();
        String storageNum= msgConveyorLineDataReportAckDTO.getStorageNum();
        String mcKey= msgConveyorLineDataReportAckDTO.getMcKey();
        String barcode= msgConveyorLineDataReportAckDTO.getBarcode();
        String loadStatus= msgConveyorLineDataReportAckDTO.getLoadStatus();
        String cargoHeight= msgConveyorLineDataReportAckDTO.getCargoHeight();
        String cargoWidth= msgConveyorLineDataReportAckDTO.getCargoWidth();
        String bcc = msgConveyorLineDataReportAckDTO.getBcc();
        StringBuilder message = new StringBuilder();
        message.append(MsgConstant.STX_STRING);
        message.append(messageNumber);
        message.append(commandType);
        message.append(reSend);
        message.append(sendTime);
        message.append(dataNum);
        message.append(blockNo);
        message.append(storageNum);
        message.append(mcKey);
        message.append(barcode);
        message.append(loadStatus);
        message.append(cargoHeight);
        message.append(cargoWidth);
        message.append(bcc);
        message.append(MsgConstant.ETX_STRING);
        return message.toString().getBytes();
    }
}
