package io.renren.wcs.client.util;

import io.renren.wcs.client.contant.MsgConstant;

import java.util.List;

/**
 * 消息处理  数据转化
 * @Author: CalmLake
 * @Date: 2018/11/17  16:30
 * @Version: V1.0.0
 **/
public class MsgMakeBytesUtil {
    /**
     * @return byte[]
     * 消息制作  设备启动01/设备停止02
     * @author CalmLake
     * @date 2018/11/17 18:20
     * @Param [messageNumber, commandType, reSend, sendTime, dataNum, machineNoList, bcc]
     */
    public static byte[] makeStartOrStopMachineryBytes(String messageNumber, String commandType, String reSend, String sendTime, String dataNum, List<String> machineNoList, String bcc) {
        StringBuilder message = new StringBuilder();
        message.append(MsgConstant.STX_STRING);
        message.append(messageNumber);
        message.append(commandType);
        message.append(reSend);
        message.append(sendTime);
        message.append(dataNum);
        for (String machineNoStr : machineNoList) {
            message.append(machineNoStr);
        }
        message.append(bcc);
        message.append(MsgConstant.ETX_STRING);
        return message.toString().getBytes();
    }

    /**
     * 修改报文头尾
     *
     * @author CalmLake
     * @date 2018/11/20 0:00
     * @Param [bytes]
     */
    public static void replaceBytesSTXAndETX(byte[] bytes) {
        bytes[0] = MsgConstant.STX_BYTE;
        bytes[bytes.length - 1] = MsgConstant.ETX_BYTE;
    }
}
