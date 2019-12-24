package io.renren.wcs.service.receive;

import io.renren.wcs.client.dto.MsgDTO;
import io.renren.wcs.client.dto.MsgDeleteDataAckDTO;
import io.renren.wcs.service.MsgReceiveService;
import org.apache.commons.lang.StringUtils;

/**
 * MsgDeleteDataAckDTO 24 消息接收解析  (除去报文头和尾string长度24)
 * @Author: CalmLake
 * @Date: 2018/11/18  16:05
 * @Version: V1.0.0
 **/
public class MsgDeleteDataAckReceiveServiceImpl implements MsgReceiveService {
    @Override
    public MsgDTO getMsgDTO(String msg) {
        MsgDeleteDataAckDTO msgDeleteDataAckDTO = new MsgDeleteDataAckDTO();
        String messageNumber = StringUtils.substring(msg, 0, 4);
        String commandType = StringUtils.substring(msg, 4, 6);
        String reSend = StringUtils.substring(msg, 6, 7);
        String sendTime = StringUtils.substring(msg, 7, 13);
        String mcKey = StringUtils.substring(msg, 13, 17);
        String machineName = StringUtils.substring(msg, 17, 21);
        String operationType = StringUtils.substring(msg, 21, 22);
        String bcc = StringUtils.substring(msg, 22, 24);
        msgDeleteDataAckDTO.setMessageNumber(messageNumber);
        msgDeleteDataAckDTO.setCommandType(commandType);
        msgDeleteDataAckDTO.setReSend(reSend);
        msgDeleteDataAckDTO.setSendTime(sendTime);
        msgDeleteDataAckDTO.setMcKey(mcKey);
        msgDeleteDataAckDTO.setMachineName(machineName);
        msgDeleteDataAckDTO.setOperationType(operationType);
        msgDeleteDataAckDTO.setBcc(bcc);
        return msgDeleteDataAckDTO;
    }
}
