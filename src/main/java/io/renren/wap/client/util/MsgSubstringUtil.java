package io.renren.wap.client.util;


import io.renren.wap.cache.SystemCache;
import io.renren.wap.client.constant.MsgConstant;
import io.renren.wap.entity.constant.CompanyConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * 消息截取工具
 *
 * @Author: CalmLake
 * @Date: 2018/11/18  13:01
 * @Version: V1.0.0
 **/
public class MsgSubstringUtil {
    /**
     * 转换成实际托盘号
     *
     * @param msgBarcode 50消息中的托盘号
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/4/1 16:34
     */
    public static String getRealBarcodeString(String msgBarcode) {
        //  输送线50托盘码错误时发送数据
        String barcodeError = "?";
        if (msgBarcode.contains(barcodeError)) {
            return msgBarcode;
        } else if (CompanyConstant.SYS_NAME_COMPANY_YONG_XIANG.equals(SystemCache.SYS_NAME_COMPANY)) {
            return yongXiangBarcode(msgBarcode);
        } else if (CompanyConstant.SYS_NAME_COMPANY_KERISOM.equals(SystemCache.SYS_NAME_COMPANY)) {
            return kerisomBarcode(msgBarcode);
        } else if (CompanyConstant.SYS_NAME_COMPANY_YAN_SHI_QU.equals(SystemCache.SYS_NAME_COMPANY)) {
            return "999999";
        } else {
            return msgBarcode;
        }
    }

    /**
     * 伽力森托盘号截取
     *
     * @param msgBarcode 15位托盘号
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/6/25 14:02
     */
    private static String kerisomBarcode(String msgBarcode) {
        return StringUtils.substring(msgBarcode, 0, msgBarcode.indexOf("_"));
    }

    /**
     * 永祥托盘号截取
     *
     * @param msgBarcode 15位托盘号
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/6/25 14:00
     */
    private static String yongXiangBarcode(String msgBarcode) {
        return StringUtils.substring(msgBarcode, 0, msgBarcode.length() - 3);
    }

    /**
     * 转换成消息中长15的托盘号
     *
     * @param realBarcode 实际托盘号
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/4/1 16:34
     */
    public static String getMsgBarcodeString(String realBarcode) {
        return StringUtils.leftPad(realBarcode, 15, "0");
    }

    /**
     * @return java.lang.String 命令种类
     * 获取命令种类
     * @author CalmLake
     * @date 2018/11/18 13:03
     * @Param [msg] 消息体
     */
    public static String getCommandType(String msg) {
        return StringUtils.substring(msg, MsgConstant.COMMAND_TYPE_START_INDEX, MsgConstant.COMMAND_TYPE_END_INDEX);
    }

    /**
     * @return java.lang.String
     * 截取消息中的data数据部分
     * @author CalmLake
     * @date 2018/11/19 22:52
     * @Param [msg]
     */
    public static String getMsgData(String msg) {
        return StringUtils.substring(msg, MsgConstant.DATA_START_INDEX, msg.length() - MsgConstant.BCC_LENGTH);
    }

    /**
     * @return java.lang.String
     * 截取消息中的BCC数据
     * @author CalmLake
     * @date 2018/11/19 22:52
     * @Param [msg]
     */
    public static String getMsgBcc(String msg) {
        return StringUtils.substring(msg, msg.length() - MsgConstant.BCC_LENGTH, msg.length());
    }
}
