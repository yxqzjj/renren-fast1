package io.renren.wcs.client.util;


import org.apache.commons.lang.StringUtils;

/**
 * 序号生成
 * @Author: CalmLake
 * @Date: 2018/11/17  15:38
 * @Version: V1.0.0
 **/
public class CreateSequenceNumberSingleton {
    /**
     * 消息序号
     */
    private int messageNumber = 0;
    private static CreateSequenceNumberSingleton ourInstance = new CreateSequenceNumberSingleton();

    public static CreateSequenceNumberSingleton getInstance() {
        return ourInstance;
    }

    /**
     * 获取新的消息序号
     *
     * @return String
     */
    public synchronized String getMessageNumber() {
        int maxNumber = 9999;
        if (maxNumber == messageNumber) {
            messageNumber = 0;
        }
        return StringUtils.leftPad(String.valueOf(messageNumber++), 4, '0');
    }

}
