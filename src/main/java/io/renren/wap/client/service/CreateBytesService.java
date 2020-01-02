package io.renren.wap.client.service;

/**
 * 创建字节数组
 *
 * @Author: CalmLake
 * @Date: 2019/5/7  15:22
 * @Version: V1.0.0
 **/
public class CreateBytesService {

    /**
     * 伽力森堆垛机接收消息长度
     */
    private static final int ML_BYTE_LENGTH = 50;

    /**
     * 创建长度 为50的定长字节数组
     * @author CalmLake
     * @date 2019/5/7 15:26
     * @param bytes 真实消息字节数组
     * @return byte[]
     */
    public static byte[] createBytes(byte[] bytes) {
        byte[] bytesNew = new byte[ML_BYTE_LENGTH];
        for (int i = 0; i < ML_BYTE_LENGTH; i++) {
            if (i < bytes.length) {
                bytesNew[i] = bytes[i];
            } else {
                bytesNew[i] = 0x30;
            }
        }
        return bytesNew;
    }

}
