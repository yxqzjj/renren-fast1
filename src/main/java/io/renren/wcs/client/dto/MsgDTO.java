package io.renren.wcs.client.dto;

import java.io.Serializable;

/**
 * wcs和console 通讯协议 消息公共部分
 *
 * @Author: CalmLake
 * @Date: 2018/11/17  11:07
 * @Version: V1.0.0
 **/
public class MsgDTO implements Serializable {
    /**
     * plc名称
     */
    private String plcName;
    /**
     * 消息序号,0000-9999
     */
    private String messageNumber;
    /**
     * 命令类型
     */
    private String commandType;
    /**
     * 重发标识,0/1
     */
    private String reSend;
    /**
     * 送信時間,HHmmss
     */
    private String sendTime;
    /**
     * bcc数据校验
     */
    private String bcc;

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getReSend() {
        return reSend;
    }

    public void setReSend(String reSend) {
        this.reSend = reSend;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    @Override
    public String toString() {
        return messageNumber + commandType + reSend + sendTime + bcc;
    }

    public String getNumString() {
        return "";
    }

}
