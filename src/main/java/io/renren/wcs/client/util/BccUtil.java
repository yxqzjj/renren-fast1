package io.renren.wcs.client.util;

/**
 * bcc操作
 *
 **/
public class BccUtil {
    /**
     * bcc是否一致
     * @param data 需校验数据
     * @param bcc bcc
     * @return true/false
     */
    public static boolean isBcc(String data, String bcc) {
        return getBcc(data).equals(bcc);
    }

    /**
     * 制作该数据对应的bcc值
     * @param data 需校验数据
     * @return bcc
     */
    public static String getBcc(String data) {
        if ("".equals(data) || null == data) {
            return "";
        } else {
            int bcc = 0;
            for (int i = 0; i < data.length(); i++) {
                bcc = bcc ^ data.charAt(i);
            }
            String bccStr = Integer.toHexString(bcc);
            if (bccStr.length() == 1) {
                bccStr = "0" + bccStr;
            }
            return bccStr.toUpperCase();
        }
    }
}
