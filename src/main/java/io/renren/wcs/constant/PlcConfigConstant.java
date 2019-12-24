package io.renren.wcs.constant;

/**
 * 功能描述
 *
 * @Author: CalmLake
 * @Date: 2019/2/28  14:49
 * @Version: V1.0.0
 **/
public class PlcConfigConstant {
    /**
     * 启用
     */
    public static final Byte STATUS_USE = 0;
    /**
     * 正在连接
     */
    public static final Byte STATUS_CONNECTING = 1;
    /**
     * 已连接
     */
    public static final Byte STATUS_CONNECTED = 2;
    /**
     * 禁用
     */
    public static final Byte STATUS_BAN = 3;

    /**
     * 获取连接状态字符串
     *
     * @param status 连接状态
     * @return java.lang.String
     * @author CalmLake
     * @date 2019/9/5 13:23
     */
    public static String getStatusString(Byte status) {
        switch (status) {
            case 0:
                return "启用";
            case 1:
                return "正在连接";
            case 2:
                return "已连接";
            case 3:
                return "禁用";
            default:
                return "";
        }
    }
}
