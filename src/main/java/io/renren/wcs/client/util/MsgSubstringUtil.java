package io.renren.wcs.client.util;

import io.renren.wcs.client.contant.MsgConstant;
import org.apache.commons.lang.StringUtils;

/**
 * 消息截取工具
 *
 **/
public class MsgSubstringUtil {
    /**
     * 截取消息中的data数据部分
     *
     */
    public static String getMsgData(String msg) {
        return StringUtils.substring(msg, MsgConstant.DATA_START_INDEX, msg.length() - MsgConstant.BCC_LENGTH);
    }
    /**
     * @return java.lang.String
     * 截取消息中的BCC数据
     */
    public static String getMsgBcc(String msg) {
        return StringUtils.substring(msg, msg.length() - MsgConstant.BCC_LENGTH, msg.length());
    }
    /**
     * @return java.lang.String 命令种类
     * 获取命令种类
     */
    public static String getCommandType(String msg) {
        return StringUtils.substring(msg, MsgConstant.COMMAND_TYPE_START_INDEX, MsgConstant.COMMAND_TYPE_END_INDEX);
    }
}
