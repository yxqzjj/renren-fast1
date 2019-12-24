package io.renren.wcs.service.receive;

import io.renren.wcs.client.dto.MsgConveyorLineDataReportDTO;
import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.service.MsgReceiveService;
import org.apache.commons.lang.StringUtils;

/**
 * MsgConveyorLineDataReportDTO 50 消息接收解析 (除去报文头和尾string长度49)  目前一次传输一条消息
 * @Author: CalmLake
 * @Date: 2018/11/18  16:04
 * @Version: V1.0.0
 **/
public class MsgConveyorLineDataReportReceiveServiceImpl implements MsgReceiveService {
    @Override
    public MsgDTO getMsgDTO(String msg) {
        MsgConveyorLineDataReportDTO msgConveyorLineDataReportDTO = new MsgConveyorLineDataReportDTO();
        String messageNumber = StringUtils.substring(msg, 0, 4);
        String commandType = StringUtils.substring(msg, 4, 6);
        String reSend = StringUtils.substring(msg, 6, 7);
        String sendTime = StringUtils.substring(msg, 7, 13);
        String dataNum = StringUtils.substring(msg, 13, 14);
        String blockNo = StringUtils.substring(msg, 14, 18);
        String storageNum = StringUtils.substring(msg, 18, 19);
        String mcKey = StringUtils.substring(msg, 19, 23);
        String barcode = StringUtils.substring(msg, 23, 38);
        String loadStatus = StringUtils.substring(msg, 38, 39);
        String cargoHeight = StringUtils.substring(msg, 39, 40);
        String cargoWidth = StringUtils.substring(msg, 40, 41);
        String weight = StringUtils.substring(msg, 41, 47);
        String bcc = StringUtils.substring(msg, 47, 49);
        msgConveyorLineDataReportDTO.setMessageNumber(messageNumber);
        msgConveyorLineDataReportDTO.setCommandType(commandType);
        msgConveyorLineDataReportDTO.setReSend(reSend);
        msgConveyorLineDataReportDTO.setSendTime(sendTime);
        msgConveyorLineDataReportDTO.setDataNum(dataNum);
        msgConveyorLineDataReportDTO.setBlockNo(blockNo);
        msgConveyorLineDataReportDTO.setStorageNum(storageNum);
        msgConveyorLineDataReportDTO.setMcKey(mcKey);
        msgConveyorLineDataReportDTO.setBarcode(barcode);
        msgConveyorLineDataReportDTO.setLoadStatus(loadStatus);
        msgConveyorLineDataReportDTO.setCargoHeight(cargoHeight);
        msgConveyorLineDataReportDTO.setCargoWidth(cargoWidth);
        msgConveyorLineDataReportDTO.setWeight(weight);
        msgConveyorLineDataReportDTO.setBcc(bcc);
        return msgConveyorLineDataReportDTO;
    }
}
